package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.persistence.EntityManager;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.KhachHangGiamGia;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Repository.KhachHangGiamGiaRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.PhieuGiamGiaRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaAdminServices {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    public PhieuGiamGiaAdminServices(
            PhieuGiamGiaRepository phieuGiamGiaRepository,
            KhachHangGiamGiaRepository khachHangGiamGiaRepository,
            KhachHangRepository khachHangRepository,
            ModelMapper modelMapper,
            EntityManager entityManager) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangGiamGiaRepository = khachHangGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;

        configureModelMapper();
    }

    private void configureModelMapper() {
        // Ánh xạ từ PhieuGiamGia sang PhieuGiamGiaAdminResponse
        TypeMap<PhieuGiamGia, PhieuGiamGiaAdminResponse> propertyMapper = modelMapper.createTypeMap(PhieuGiamGia.class, PhieuGiamGiaAdminResponse.class);
        propertyMapper.addMappings(mapper -> {
            mapper.map(PhieuGiamGia::getId, PhieuGiamGiaAdminResponse::setId);
            mapper.map(PhieuGiamGia::getMaGiamGia, PhieuGiamGiaAdminResponse::setMaGiamGia);
            mapper.map(PhieuGiamGia::getTenKhuyenMai, PhieuGiamGiaAdminResponse::setTenKhuyenMai);
            mapper.map(PhieuGiamGia::getLoaiKhuyenMai, PhieuGiamGiaAdminResponse::setLoaiKhuyenMai);
            mapper.map(PhieuGiamGia::getGiaTriKhuyenMai, PhieuGiamGiaAdminResponse::setGiaTriKhuyenMai);
            mapper.map(PhieuGiamGia::getGiaTriDonHangToiThieu, PhieuGiamGiaAdminResponse::setGiaTriDonHangToiThieu);
            mapper.map(PhieuGiamGia::getGiaTriKhuyenMaiToiDa, PhieuGiamGiaAdminResponse::setGiaTriKhuyenMaiToiDa);
            mapper.map(PhieuGiamGia::getNgayBatDau, PhieuGiamGiaAdminResponse::setNgayBatDau);
            mapper.map(PhieuGiamGia::getNgayKetThuc, PhieuGiamGiaAdminResponse::setNgayKetThuc);
            mapper.map(PhieuGiamGia::getDieuKienApDung, PhieuGiamGiaAdminResponse::setDieuKienApDung);
            mapper.map(PhieuGiamGia::getHangToiThieu, PhieuGiamGiaAdminResponse::setHangToiThieu);
            mapper.map(PhieuGiamGia::getSoLuong, PhieuGiamGiaAdminResponse::setSoLuong);
            mapper.map(PhieuGiamGia::getSoDiemCanDeDoi, PhieuGiamGiaAdminResponse::setSoDiemCanDeDoi);
            mapper.map(PhieuGiamGia::getIsGlobal, PhieuGiamGiaAdminResponse::setIsGlobal);
            mapper.map(PhieuGiamGia::getTrangThai, PhieuGiamGiaAdminResponse::setTrangThai);
        });

        // Ánh xạ từ PhieuGiamGiaAdminRequest sang PhieuGiamGia, bỏ qua khachHangGiamGias
        TypeMap<PhieuGiamGiaAdminRequest, PhieuGiamGia> requestMapper = modelMapper.createTypeMap(PhieuGiamGiaAdminRequest.class, PhieuGiamGia.class);
        requestMapper.addMappings(mapper -> mapper.skip(PhieuGiamGia::setKhachHangGiamGias));
    }

    public Page<PhieuGiamGiaAdminResponse> getPagePhieuGiamGia(
            String search, TrangThaiPGG trangThai, LocalDate ngayBatDau, LocalDate ngayKetThuc,
            int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PhieuGiamGia> pagePhieuGiamGia = phieuGiamGiaRepository.findAll(search, trangThai, ngayBatDau, ngayKetThuc, pageable);
        return pagePhieuGiamGia.map(entity -> {
            PhieuGiamGiaAdminResponse response = modelMapper.map(entity, PhieuGiamGiaAdminResponse.class);
            if (!entity.getIsGlobal()) {
                List<Integer> khachHangIds = entity.getKhachHangGiamGias().stream()
                        .map(khgg -> khgg.getIdKhachHang().getId())
                        .collect(Collectors.toList());
                response.setKhachHangIds(khachHangIds);
            } else {
                response.setKhachHangIds(List.of());
            }
            return response;
        });
    }

    public PhieuGiamGiaAdminResponse getPhieuGiamGia(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));
        PhieuGiamGiaAdminResponse response = modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
        if (!phieuGiamGia.getIsGlobal()) {
            List<Integer> khachHangIds = phieuGiamGia.getKhachHangGiamGias().stream()
                    .map(khgg -> khgg.getIdKhachHang().getId())
                    .collect(Collectors.toList());
            response.setKhachHangIds(khachHangIds);
        } else {
            response.setKhachHangIds(List.of());
        }
        return response;
    }

    @Transactional
    public PhieuGiamGiaAdminResponse addPhieuGiamGia(PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        // Kiểm tra ngày bắt đầu và ngày kết thúc
        if (phieuGiamGiaAdminRequest.getNgayBatDau().isAfter(phieuGiamGiaAdminRequest.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        // Kiểm tra logic isGlobal và khachHangIds
        if (!phieuGiamGiaAdminRequest.getIsGlobal() &&
                (phieuGiamGiaAdminRequest.getKhachHangIds() == null || phieuGiamGiaAdminRequest.getKhachHangIds().isEmpty())) {
            throw new IllegalArgumentException("Phiếu giảm giá riêng tư phải có ít nhất một khách hàng được chọn");
        }

        // Ánh xạ DTO sang entity
        PhieuGiamGia phieuGiamGia = modelMapper.map(phieuGiamGiaAdminRequest, PhieuGiamGia.class);

        // Đảm bảo khachHangGiamGias rỗng trước khi lưu
        phieuGiamGia.setKhachHangGiamGias(new LinkedHashSet<>());

        // Lưu entity vào database
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        // Nếu không phải global, lưu danh sách khách hàng được áp dụng
        if (!phieuGiamGia.getIsGlobal() && phieuGiamGiaAdminRequest.getKhachHangIds() != null) {
            List<Integer> invalidIds = phieuGiamGiaAdminRequest.getKhachHangIds().stream()
                    .filter(id -> !khachHangRepository.existsById(id))
                    .collect(Collectors.toList());
            if (!invalidIds.isEmpty()) {
                throw new IllegalArgumentException("Các ID khách hàng không tồn tại: " + invalidIds);
            }

            for (Integer khachHangId : phieuGiamGiaAdminRequest.getKhachHangIds()) {
                KhachHang khachHang = khachHangRepository.findById(khachHangId)
                        .orElseThrow(() -> new IllegalArgumentException("Khách hàng ID " + khachHangId + " không tồn tại"));
                createNewKhachHangGiamGia(phieuGiamGia, khachHang);
            }
        }

        // Ánh xạ entity sang response DTO
        PhieuGiamGiaAdminResponse response = modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);

        // Gán khachHangIds cho response
        if (!phieuGiamGia.getIsGlobal()) {
            List<Integer> khachHangIds = phieuGiamGia.getKhachHangGiamGias().stream()
                    .map(khgg -> khgg.getIdKhachHang().getId())
                    .collect(Collectors.toList());
            response.setKhachHangIds(khachHangIds);
        } else {
            response.setKhachHangIds(List.of());
        }

        return response;
    }

    @Transactional
    public PhieuGiamGiaAdminResponse updatePhieuGiamGia(Integer id, PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        // Tìm phiếu giảm giá
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        // Kiểm tra ngày bắt đầu và ngày kết thúc
        if (phieuGiamGiaAdminRequest.getNgayBatDau().isAfter(phieuGiamGiaAdminRequest.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        // Kiểm tra logic isGlobal và khachHangIds
        if (!phieuGiamGiaAdminRequest.getIsGlobal() &&
                (phieuGiamGiaAdminRequest.getKhachHangIds() == null || phieuGiamGiaAdminRequest.getKhachHangIds().isEmpty())) {
            throw new IllegalArgumentException("Phiếu giảm giá riêng tư phải có ít nhất một khách hàng được chọn");
        }

        // Ánh xạ DTO sang entity (trừ khachHangGiamGias)
        modelMapper.map(phieuGiamGiaAdminRequest, phieuGiamGia);

        // Xử lý mối quan hệ KhachHangGiamGia
        if (phieuGiamGia.getIsGlobal()) {
            // Nếu isGlobal = true, xóa tất cả bản ghi KhachHangGiamGia chưa sử dụng
            List<KhachHangGiamGia> unusedRecords = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
            khachHangGiamGiaRepository.deleteAll(unusedRecords);
            phieuGiamGia.getKhachHangGiamGias().clear();
        } else {
            // Nếu isGlobal = false, cập nhật danh sách khách hàng
            Set<Integer> newKhachHangIds = new HashSet<>(phieuGiamGiaAdminRequest.getKhachHangIds());

            // Kiểm tra tồn tại của khách hàng
            List<Integer> invalidIds = newKhachHangIds.stream()
                    .filter(khId -> !khachHangRepository.existsById(khId))
                    .collect(Collectors.toList());
            if (!invalidIds.isEmpty()) {
                throw new IllegalArgumentException("Các ID khách hàng không tồn tại: " + invalidIds);
            }

            // Lấy danh sách khách hàng hiện tại
            Set<Integer> existingKhachHangIds = phieuGiamGia.getKhachHangGiamGias().stream()
                    .map(khgg -> khgg.getIdKhachHang().getId())
                    .collect(Collectors.toSet());

            // Thêm các khách hàng mới
            for (Integer khachHangId : newKhachHangIds) {
                if (!existingKhachHangIds.contains(khachHangId)) {
                    KhachHang khachHang = khachHangRepository.findById(khachHangId)
                            .orElseThrow(() -> new IllegalArgumentException("Khách hàng ID " + khachHangId + " không tồn tại"));
                    createNewKhachHangGiamGia(phieuGiamGia, khachHang);
                }
            }

            // Xóa các bản ghi KhachHangGiamGia không còn trong danh sách mới (chưa sử dụng)
            List<KhachHangGiamGia> recordsToRemove = phieuGiamGia.getKhachHangGiamGias().stream()
                    .filter(khgg -> !newKhachHangIds.contains(khgg.getIdKhachHang().getId()) && !khgg.getIsUser())
                    .collect(Collectors.toList());
            for (KhachHangGiamGia record : recordsToRemove) {
                phieuGiamGia.getKhachHangGiamGias().remove(record);
                khachHangGiamGiaRepository.delete(record);
            }
        }

        // Lưu entity
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        // Ánh xạ entity sang response DTO
        PhieuGiamGiaAdminResponse response = modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);

        // Gán khachHangIds cho response
        if (!phieuGiamGia.getIsGlobal()) {
            List<Integer> khachHangIds = phieuGiamGia.getKhachHangGiamGias().stream()
                    .map(khgg -> khgg.getIdKhachHang().getId())
                    .collect(Collectors.toList());
            response.setKhachHangIds(khachHangIds);
        } else {
            response.setKhachHangIds(List.of());
        }

        return response;
    }

    @Transactional
    public String deletePhieuGiamGia(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));

        // Kiểm tra xem phiếu có đang được sử dụng
        boolean hasUsed = khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIsUser(phieuGiamGia, true);
        if (hasUsed) {
            throw new IllegalStateException("Không thể xóa phiếu giảm giá đã được sử dụng");
        }

        khachHangGiamGiaRepository.deleteByIdPhieuGiamGiaId(id);
        phieuGiamGiaRepository.delete(phieuGiamGia);
        return "Đã xóa thành công phiếu giảm giá có id = " + id;
    }

    public List<KhachHangGiamGiaResponse> getAllKhachHang() {
        return khachHangRepository.findAll().stream()
                .map(kh -> new KhachHangGiamGiaResponse(kh.getId(), kh.getTenKhachHang()))
                .collect(Collectors.toList());
    }

    private void createNewKhachHangGiamGia(PhieuGiamGia phieuGiamGia, KhachHang khachHang) {
        KhachHangGiamGia khachHangGiamGia = new KhachHangGiamGia();
        khachHangGiamGia.setIdPhieuGiamGia(phieuGiamGia);
        khachHangGiamGia.setIdKhachHang(khachHang);
        khachHangGiamGia.setIsUser(false);
        khachHangGiamGia.setNgayCap(LocalDate.now());
        phieuGiamGia.getKhachHangGiamGias().add(khachHangGiamGia);
        khachHangGiamGiaRepository.save(khachHangGiamGia);
    }

    private void saveKhachHangGiamGia(PhieuGiamGia phieuGiamGia, List<Integer> khachHangIds) {
        if (khachHangIds != null) {
            for (Integer khachHangId : khachHangIds) {
                KhachHang khachHang = khachHangRepository.findById(khachHangId)
                        .orElseThrow(() -> new IllegalArgumentException("Khách hàng ID " + khachHangId + " không tồn tại"));
                createNewKhachHangGiamGia(phieuGiamGia, khachHang);
            }
        }
    }

    @Transactional
    public void updateTrangThaiPhieuGiamGia() {
        LocalDate now = LocalDate.now();
        List<PhieuGiamGia> phieuGiamGias = phieuGiamGiaRepository.findAll();
        for (PhieuGiamGia entity : phieuGiamGias) {
            if (now.isBefore(entity.getNgayBatDau())) {
                entity.setTrangThai(TrangThaiPGG.NOT_STARTED);
            } else if (now.isAfter(entity.getNgayKetThuc())) {
                entity.setTrangThai(TrangThaiPGG.EXPIRED);
            } else {
                entity.setTrangThai(TrangThaiPGG.ACTIVE);
            }
            phieuGiamGiaRepository.save(entity);
        }
    }
}