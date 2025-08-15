package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.LoaiDiem;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPhatHanh;
import org.example.websitetechworld.Events.PhieuGiamGiaUpdatedEvent;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaAdminService {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final LichSuDiemRepository lichSuDiemRepository;
    private final ModelMapper modelMapper;
    private final AsyncEmailService asyncEmailService;
    private final ApplicationEventPublisher eventPublisher;

    public PhieuGiamGiaAdminService(PhieuGiamGiaRepository phieuGiamGiaRepository,
                                    KhachHangGiamGiaRepository khachHangGiamGiaRepository,
                                    KhachHangRepository khachHangRepository,
                                    ModelMapper modelMapper,
                                    LichSuDiemRepository lichSuDiemRepository,
                                    AsyncEmailService asyncEmailService, ApplicationEventPublisher eventPublisher) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangGiamGiaRepository = khachHangGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.lichSuDiemRepository = lichSuDiemRepository;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        cauHinhModelMapper();
        this.asyncEmailService = asyncEmailService;
    }

    private void cauHinhModelMapper () {
        modelMapper.typeMap(PhieuGiamGiaAdminRequest.class, PhieuGiamGia.class);
        modelMapper.typeMap(PhieuGiamGia.class, PhieuGiamGiaAdminResponse.class);
    }

    public Page<PhieuGiamGiaAdminResponse> layDanhSachPhieuGiamGia (String timKiem, TrangThaiPGG trangThai,
                                                                  LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                                  int trang, int kichThuoc, String sapXepTheo, String huongSapXep) {
        Sort sapXep = huongSapXep.equalsIgnoreCase("desc") ? Sort.by(sapXepTheo).descending() : Sort.by(sapXepTheo).ascending();
        Pageable phanTrang = PageRequest.of(trang, kichThuoc, sapXep);

        LocalDateTime ngayBatDauDateTime = (ngayBatDau != null) ? ngayBatDau.atStartOfDay() : null;
        LocalDateTime ngayKetThucDateTime = (ngayKetThuc != null) ? ngayKetThuc.atTime(23, 59, 59, 998) : null;

        Page<PhieuGiamGia> trangPhieuGiamGia = phieuGiamGiaRepository.findAll(timKiem, trangThai, ngayBatDauDateTime, ngayKetThucDateTime, phanTrang);
        return trangPhieuGiamGia.map(this::anhXaPhieuGiamGia);
    }

    public PhieuGiamGiaAdminResponse layPhieuGiamGiaTheoId (Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));
        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    @Transactional
    public PhieuGiamGiaAdminResponse themPhieuGiamGia (PhieuGiamGiaAdminRequest request) {
        kiemTraNgayHopLe(request.getNgayBatDau(), request.getNgayKetThuc());

        LocalDateTime currentDate = LocalDateTime.now();
        if (request.getNgayBatDau().isBefore(currentDate)) {
            throw new IllegalStateException("Ngày bắt đầu phải sau ngày hiện tại");
        }

        if (request.getMaGiamGia() == null || request.getMaGiamGia().trim().isEmpty()) {
            request.setMaGiamGia(taoMaGiamGiaNgauNhien());
        }

        PhieuGiamGia phieuGiamGia = modelMapper.map(request, PhieuGiamGia.class);
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);
        eventPublisher.publishEvent(new PhieuGiamGiaUpdatedEvent(phieuGiamGia.getId()));

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    @Transactional
    public PhieuGiamGiaAdminResponse capNhatPhieuGiamGia (Integer id, PhieuGiamGiaAdminRequest request) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        if (!phieuGiamGia.getMaGiamGia().equalsIgnoreCase(request.getMaGiamGia())) {
            throw new IllegalArgumentException("Mã giảm giá không được thay đổi");
        }

        if (isHangToiThieuModified(phieuGiamGia, request)) {
            phieuGiamGia.setHangToiThieu(request.getHangToiThieu());
        }

        kiemTraNgayHopLe(request.getNgayBatDau(), request.getNgayKetThuc());

        if (hasNotStarted(phieuGiamGia.getNgayBatDau())) {
            modelMapper.map(request, phieuGiamGia);
        } else {

            phieuGiamGia.setTenGiamGia(request.getTenGiamGia());
            phieuGiamGia.setSoLuong(request.getSoLuong());
            phieuGiamGia.setNgayKetThuc(request.getNgayKetThuc());
            phieuGiamGia.setSoDiemCanDeDoi(request.getSoDiemCanDeDoi());
            phieuGiamGia.setDieuKienApDung(request.getDieuKienApDung());
            phieuGiamGia.setTrangThaiPhieuGiamGia(request.getTrangThaiPhieuGiamGia());
            phieuGiamGia.setTrangThaiPhatHanh(request.getTrangThaiPhatHanh());
        }
        phieuGiamGia.setId(id);
        xuLyKhachHangChuaSuDung(phieuGiamGia);
        eventPublisher.publishEvent(new PhieuGiamGiaUpdatedEvent(phieuGiamGia.getId()));

        return modelMapper.map(phieuGiamGiaRepository.save(phieuGiamGia), PhieuGiamGiaAdminResponse.class);
    }

    @Transactional
    public Map<String, Object> xoaPhieuGiamGia(Integer id) {
        Map<String, Object> response = new HashMap<>();

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        if (!hasNotStarted(phieuGiamGia.getNgayBatDau())) {
            response.put("message", "Không thể xóa phiếu giảm giá đã bắt đầu.");
            response.put("code", 0);
            return response;
        }
        if (khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIsUser(phieuGiamGia, true)) {
            response.put("message", "Không thể xóa phiếu giảm giá đã được sử dụng");
            response.put("code", 0);
            return response;
        }
        xuLyKhachHangChuaSuDung(phieuGiamGia);
        khachHangGiamGiaRepository.deleteByIdPhieuGiamGiaId(id);
        phieuGiamGiaRepository.delete(phieuGiamGia);
        eventPublisher.publishEvent(new PhieuGiamGiaUpdatedEvent(phieuGiamGia.getId()));

        response.put("message", "Đã xóa thành công phiếu giảm giá với ID: " + id);
        response.put("code", 1);
        return response;
    }

    private String taoMaGiamGiaNgauNhien() {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        while (phieuGiamGiaRepository.existsByMaGiamGia(code)) {
            code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
        return code;
    }

    private boolean isHangToiThieuModified(PhieuGiamGia existing, PhieuGiamGiaAdminRequest request) {
        return (existing.getHangToiThieu() != null && !existing.getHangToiThieu().equals(request.getHangToiThieu())) ||
                (existing.getHangToiThieu() == null && request.getHangToiThieu() != null);
    }

    private boolean hasNotStarted(LocalDateTime time) {
        return LocalDateTime.now().isBefore(time);
    }

    @Async
    public void notifyAffectedCustomers(PhieuGiamGia phieu, KhachHang khachHang) {
        try {
            asyncEmailService.sendEmailAsync(
                    khachHang.getEmail(), "Thông báo về phiếu giảm giá",
                    String.format("""
                        Kính gửi %s,
                        
                        Phiếu giảm giá %s đã được cập nhật/hủy bỏ. Vui lòng kiểm tra lại trong hệ thống.
                        
                        TechWorld""", khachHang.getTenKhachHang(), phieu.getTenGiamGia()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void capNhatTrangThaiPhieuGiamGia (LocalDateTime hienTai) {
        List<PhieuGiamGia> danhSachPhieu = phieuGiamGiaRepository.findAllByTrangThaiPhieuGiamGiaIn(List.of(TrangThaiPGG.NOT_STARTED, TrangThaiPGG.ACTIVE));
        for (PhieuGiamGia phieu : danhSachPhieu) {
            TrangThaiPGG trangThaiMoi = xacDinhTrangThaiPhieu(phieu, hienTai);
            if (phieu.getTrangThaiPhieuGiamGia() != trangThaiMoi) {
                phieu.setTrangThaiPhieuGiamGia(trangThaiMoi);
                phieuGiamGiaRepository.save(phieu);
            }
        }
    }

    private void kiemTraNgayHopLe (LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        if (ngayBatDau.isAfter(ngayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    @Transactional
    public void xuLyKhachHangChuaSuDung (PhieuGiamGia phieuGiamGia) {
        List<KhachHangGiamGia> banGhiChuaSuDung = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
        for (KhachHangGiamGia khgg : banGhiChuaSuDung) {
            KhachHang khachHang = khgg.getIdKhachHang();
            doiPhieuThahDiem (phieuGiamGia, khachHang);
            notifyAffectedCustomers(phieuGiamGia, khachHang);
        }
        khachHangGiamGiaRepository.deleteAll(banGhiChuaSuDung);
    }

    private void doiPhieuThahDiem (PhieuGiamGia phieuGiamGia, KhachHang khachHang) {
        ViDiem viDiem = khachHang.getViDiem();
        if (viDiem != null && phieuGiamGia != null) {
            viDiem.setDiemKhaDung(viDiem.getDiemKhaDung()
                    .add(BigDecimal.valueOf(50)));
            khachHang.setViDiem(viDiem);
            khachHangRepository.save(khachHang);

            LichSuDiem lichSuDiem = new LichSuDiem();
            lichSuDiem.setViDiem(viDiem);
            lichSuDiem.setSoDiem(BigDecimal.valueOf(50));
            lichSuDiem.setLoaiDiem(LoaiDiem.CONG);
            lichSuDiem.setGhiChu("Hoàn điểm từ mã: " + phieuGiamGia.getMaGiamGia());
            OffsetDateTime thoiGianHienTai = OffsetDateTime.now(ZoneId.systemDefault());
            lichSuDiem.setThoiGian(thoiGianHienTai);
            lichSuDiem.setHanSuDung(thoiGianHienTai.plusYears(1));
            lichSuDiemRepository.save(lichSuDiem);
        }
    }

    private void taoKhachHangGiamGia (PhieuGiamGia phieuGiamGia, KhachHang khachHang, Integer trangThai) {
        KhachHangGiamGia khachHangGiamGia = new KhachHangGiamGia();
        khachHangGiamGia.setIdPhieuGiamGia(phieuGiamGia);
        khachHangGiamGia.setIdKhachHang(khachHang);
        khachHangGiamGia.setIsUser(false);
        khachHangGiamGia.setNgayCap(LocalDate.now());
        khachHangGiamGia.setTrangThai(trangThai);
        khachHangGiamGiaRepository.save(khachHangGiamGia);
    }

    private TrangThaiPGG xacDinhTrangThaiPhieu(PhieuGiamGia phieu, LocalDateTime hienTai) {
        if (hienTai.isBefore(phieu.getNgayBatDau())) return TrangThaiPGG.NOT_STARTED;
        if (hienTai.isAfter(phieu.getNgayKetThuc())) return TrangThaiPGG.EXPIRED;
        return TrangThaiPGG.ACTIVE;
    }

    private PhieuGiamGiaAdminResponse anhXaPhieuGiamGia (PhieuGiamGia phieuGiamGia) {
        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    //Hoa don


    // Show the vouchers in client when redeeming points
    public Page<PhieuGiamGiaAdminResponse> getVoucherRedeeming (String timKiem, TrangThaiPGG trangThai,
                                                                    LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                                    HangKhachHang hangKhachHang,
                                                                    int trang, int kichThuoc, String sapXepTheo, String huongSapXep) {
        Sort sapXep = huongSapXep.equalsIgnoreCase("desc") ? Sort.by(sapXepTheo).descending() : Sort.by(sapXepTheo).ascending();
        Pageable phanTrang = PageRequest.of(trang, kichThuoc, sapXep);
        Page<PhieuGiamGia> trangPhieuGiamGia = phieuGiamGiaRepository.getDoiVoucher(timKiem, trangThai, ngayBatDau, ngayKetThuc, hangKhachHang, phanTrang);
        return trangPhieuGiamGia.map(this::anhXaPhieuGiamGia);
    }

    @Transactional
    public Map<String, Object> giftPhieuGiamGia (Integer id, String type) {
        Map<String, Object> result = new HashMap<>();

            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

            if (phieuGiamGia.getTrangThaiPhieuGiamGia() == TrangThaiPGG.EXPIRED) {
                throw new IllegalStateException("Phiếu giảm giá đã hết hạn");
            }

            if (phieuGiamGia.getTrangThaiPhatHanh() != TrangThaiPhatHanh.ISSUED) {
                throw new IllegalStateException("Phiếu giảm giá chưa được phát hành");
            }

            int trangThaiKhachHangGiamGia = 0;
            List<KhachHang> eligibleCustomers = switch (type.toUpperCase()) {
                case "BIRTHDAY" -> {
                    LocalDateTime now = LocalDateTime.now();
                    trangThaiKhachHangGiamGia = 2;
                    yield khachHangRepository.findByMonthOfNgaySinh(now.getMonthValue());
                }
                case "NEW_CUSTOMER" -> {
                    trangThaiKhachHangGiamGia = 3;
                    yield khachHangRepository.findNewCustomers();
                }
                default -> throw new IllegalArgumentException("Loại tặng phiếu không hợp lệ: " + type);
            };

            if (eligibleCustomers.isEmpty()) {
                result.put("message", "Không tìm thấy khách hàng phù hợp để tặng phiếu");
                result.put("code", 0);
                return result;
            }

            List<KhachHang> customersToGift = eligibleCustomers.stream().toList();

            if (customersToGift.isEmpty()) {
                result.put("message", "Không có khách hàng nào đủ điều kiện nhận phiếu");
                result.put("code", 0);
                return result;
            }

            for (KhachHang khachHang : customersToGift) {
                KhachHangGiamGia existingRecord = khachHangGiamGiaRepository
                        .findByIdPhieuGiamGiaAndIdKhachHangAndIsUser(phieuGiamGia, khachHang, false);

                if (existingRecord != null) {
                    doiPhieuThahDiem (phieuGiamGia, khachHang);
                } else {
                    taoKhachHangGiamGia(phieuGiamGia, khachHang, trangThaiKhachHangGiamGia);
                }
                sendGiftNotification(khachHang, phieuGiamGia, false);
            }

            result.put("code", 1);
            result.put("message", String.format("Đã tặng phiếu giảm giá %s cho %d khách hàng %s thành công",
                    phieuGiamGia.getMaGiamGia(), customersToGift.size(), type.equalsIgnoreCase("BIRTHDAY") ? "sinh nhật" : "mới"));

        return result;
    }

    @Async
    protected void sendGiftNotification(KhachHang khachHang, PhieuGiamGia phieuGiamGia, Boolean isTangPhieu) {
        if (khachHang.getEmail() == null || khachHang.getEmail().isBlank()) {
            return;
        }
        try {
            String subject = phieuGiamGia.getMaGiamGia() + " - Quà tặng từ TechWorld";

            String giaTri = phieuGiamGia.getLoaiGiamGia().equalsIgnoreCase("Phần trăm")
                    ? phieuGiamGia.getGiaTriGiamGia() + "%"
                    : phieuGiamGia.getGiaTriGiamGia() + " VNĐ";

            String moTaToiDa = phieuGiamGia.getLoaiGiamGia().equalsIgnoreCase("Phần trăm") &&
                    phieuGiamGia.getGiaTriGiamGiaToiDa() != null
                    ? " (tối đa " + phieuGiamGia.getGiaTriGiamGiaToiDa() + " VNĐ)"
                    : "";

            String thongBao = isTangPhieu
                    ? """
                                <p>Vui lòng sử dụng mã này khi thanh toán trên hệ thống của chúng tôi.</p>
                            """
                    : String.format("""
                                <p>Phiếu giảm giá <strong>%s</strong> đã được chuyển đổi thành <strong>%s điểm</strong> 
                                    vào ví điểm của bạn do phiếu đã được bạn đổi trước đó.</p>
                                <p>Số điểm hiện tại: <strong>%s</strong></p>
                            """,
                            phieuGiamGia.getMaGiamGia(),
                            50.00,
                            khachHang.getViDiem().getDiemKhaDung()
            );

            String message = String.format("""
                        <html>
                            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                                <p>Kính gửi <strong>%s</strong>,</p>
            
                                <p>Chúc mừng bạn đã nhận được phiếu giảm giá <strong>%s</strong> từ <strong>TechWorld</strong>!</p>
            
                                <ul>
                                    <li><strong>Tên khuyến mãi:</strong> %s</li>
                                    <li><strong>Giá trị:</strong> %s%s</li>
                                    <li><strong>Hạn sử dụng:</strong> Từ %s đến %s</li>
                                    <li><strong>Điều kiện:</strong> %s</li>
                                </ul>
        
                                %s
            
                                <p>Trân trọng,<br/>
                                <strong>TechWorld</strong></p>
                            </body>
                        </html>
                        """,
                        khachHang.getTenKhachHang(),
                        phieuGiamGia.getMaGiamGia(),
                        phieuGiamGia.getTenGiamGia(),
                        giaTri,
                        moTaToiDa,
                        formatterDate(phieuGiamGia.getNgayBatDau()),
                        formatterDate(phieuGiamGia.getNgayKetThuc()),
                        phieuGiamGia.getDieuKienApDung(),
                        thongBao
            );

            asyncEmailService.sendEmailAsync(khachHang.getEmail(), subject, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatterDate (LocalDateTime localDateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}