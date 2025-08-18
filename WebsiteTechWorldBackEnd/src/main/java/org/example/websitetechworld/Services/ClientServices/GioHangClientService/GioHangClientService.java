package org.example.websitetechworld.Services.ClientServices.GioHangClientService;

import org.example.websitetechworld.Dto.Request.ClientRequest.GioHangClientRequest.GioHangClientRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.GioHangClientResponse.GioHangClientResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GioHangClientService {

    private final GioHangRepository gioHangRepository;
    private final GioHangChiTietRepository gioHangChiTietRepository;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final HoaDonRepository hoaDonRepository;

    public GioHangClientService(GioHangRepository gioHangRepository, GioHangChiTietRepository gioHangChiTietRepository, KhachHangRepository khachHangRepository, SanPhamChiTietRepository sanPhamChiTietRepository, HoaDonRepository hoaDonRepository) {
        this.gioHangRepository = gioHangRepository;
        this.gioHangChiTietRepository = gioHangChiTietRepository;
        this.khachHangRepository = khachHangRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
    }

    public GioHangClientResponse layGioHang (Integer idKhachHang) {
        GioHang gioHang = gioHangRepository.findByIdKhachHangId(idKhachHang);
        if (gioHang == null) {
            return new GioHangClientResponse();
        }
        return mapToResponseDTO(gioHang);
    }

    @Transactional
    public GioHangClientResponse themSanPhamVaoGio(GioHangClientRequest request) {
        GioHang gioHang = gioHangRepository.findByIdKhachHangId(request.getIdKhachHang());
        if (request.getSoLuong() <= 0) {
            throw new IllegalStateException("Số lượng phải lớn hơn 0!");
        }
        if (gioHang == null) {
            gioHang = new GioHang();
            KhachHang khachHang = khachHangRepository.findById(request.getIdKhachHang())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng có id: " + request.getIdKhachHang()));
            gioHang.setIdKhachHang(khachHang);
            gioHang = gioHangRepository.save(gioHang);
        }

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy spct có id: " + request.getIdSanPhamChiTiet()));

        GioHangChiTiet item = gioHangChiTietRepository.findByIdGioHangAndIdSanPhamChiTiet(gioHang, sanPhamChiTiet);
        if (item != null) {
            if (item.getSoLuong() + request.getSoLuong() > item.getIdSanPhamChiTiet().getSoLuong()) {
                throw new IllegalStateException("Trong giỏ hàng đã có " + item.getSoLuong() + " sản phẩm này");
            }
            item.setSoLuong(item.getSoLuong() + request.getSoLuong());
        } else {
            item = new GioHangChiTiet();
            item.setIdGioHang(gioHang);
            item.setIdSanPhamChiTiet(sanPhamChiTiet);
            item.setSoLuong(request.getSoLuong());
        }
        item.setGia(sanPhamChiTiet.getGiaBan());
        item.setNgayThem(LocalDateTime.now());
        gioHangChiTietRepository.save(item);

        return mapToResponseDTO(gioHangRepository.findById(gioHang.getId()).orElseThrow());
    }

    @Transactional
    public GioHangClientResponse suaSoLuongSanPham (Integer idGioHangChiTiet, Integer soLuong) {
        GioHangChiTiet item = gioHangChiTietRepository.findById(idGioHangChiTiet)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng chi tiết có id: " + idGioHangChiTiet));
        if (soLuong > item.getIdSanPhamChiTiet().getSoLuong()) {
            throw new IllegalStateException("Số lượng tồn kho không đủ");
        }
        item.setSoLuong(soLuong);
        gioHangChiTietRepository.save(item);
        return mapToResponseDTO(gioHangRepository.findById(item.getIdGioHang().getId()).orElseThrow());
    }

    @Transactional
    public void xoaSanPhamKhoiGio (Integer idSanPhamChiTiet) {
        gioHangChiTietRepository.deleteById(idSanPhamChiTiet);
    }

    @Transactional
    public void xoaGioHang (Integer idKhachHang) {
        GioHang gioHang = gioHangRepository.findByIdKhachHangId(idKhachHang);
        if (gioHang != null) {
            gioHangChiTietRepository.deleteAll(gioHang.getGioHangChiTiets());
        }
    }
    @Transactional
    public void xoaAllGioHang (SanPhamChiTiet sanPhamChiTiet) {
        gioHangChiTietRepository.deleteByIdSanPhamChiTiet_Id(sanPhamChiTiet.getId());
    }

    private GioHangClientResponse mapToResponseDTO (GioHang gioHang) {
        GioHangClientResponse response = new GioHangClientResponse();
        response.setIdGioHang(gioHang.getId());
        response.setIdKhachHang(gioHang.getIdKhachHang().getId());
        response.setItems(gioHang.getGioHangChiTiets().stream().map(item -> {
            GioHangClientResponse.GioHangChiTietResponse itemDTO = new GioHangClientResponse.GioHangChiTietResponse();
            itemDTO.setIdGioHangChiTiet(item.getId());
            itemDTO.setIdSanPhamChiTiet(item.getIdSanPhamChiTiet().getId());
            itemDTO.setTenSanPham(item.getIdSanPhamChiTiet().getIdSanPham().getTenSanPham());
            itemDTO.setPhienBan(String.format("%s, %s",
                    item.getIdSanPhamChiTiet().getIdMau().getTenMau(),
                    item.getIdSanPhamChiTiet().getIdRom().getDungLuong()));
            itemDTO.setImageUrl(item.getIdSanPhamChiTiet().getHinhAnhs().stream()
                    .findFirst().map(HinhAnh::getUrl).orElse(""));
            itemDTO.setGiaTruocKhuyenMai(item.getGia());
            itemDTO.setGia(tinhGiaKhuyenMai(item.getIdSanPhamChiTiet().getId(), gioHang.getIdKhachHang().getId()));
            itemDTO.setSoLuong(item.getSoLuong());
            itemDTO.setSoLuongTon(item.getIdSanPhamChiTiet().getSoLuong());
            itemDTO.setNgayThem(item.getNgayThem());
            return itemDTO;
        }).collect(Collectors.toList()));
        return response;
    }

    private BigDecimal tinhGiaKhuyenMai(Integer idSpct, Integer selectedIdKhachHang) {
        try {
            if (idSpct == null) {
                return BigDecimal.ZERO;
            }
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(idSpct)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm chi tiết với ID: " + idSpct));

            BigDecimal giaGoc = spct.getGiaBan();
            if (giaGoc == null) {
                return BigDecimal.ZERO;
            }
            KhuyenMai khuyenMai = spct.getIdKhuyenMai();
            if (khuyenMai == null) {
                return giaGoc;
            }
            if (khuyenMai.getTrangThai() != TrangThaiKhuyenMai.ACTIVE) {
                return giaGoc;
            }
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(khuyenMai.getNgayBatDau()) || now.isAfter(khuyenMai.getNgayKetThuc())) {
                return giaGoc;
            }

            Integer discountValue = Optional.ofNullable(khuyenMai.getPhanTramGiam()).orElse(0);
            BigDecimal tyLeGiam = BigDecimal.valueOf(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            DoiTuongApDung doiTuong = khuyenMai.getDoiTuongApDung();
            if (doiTuong == DoiTuongApDung.ALL) {
                return tinhGiaKhuyenMai(giaGoc, tyLeGiam);
            }
            if (selectedIdKhachHang == null || selectedIdKhachHang == 0) {
                return giaGoc;
            }
            boolean khachHangCu = hoaDonRepository.countHoaDonByIdKhachHang(selectedIdKhachHang) > 0;
            boolean khongHopLe =
                    (doiTuong == DoiTuongApDung.NEW_CUSTOMER && khachHangCu) ||
                            (doiTuong == DoiTuongApDung.OLD_CUSTOMER && !khachHangCu);
            if (khongHopLe) {
                return giaGoc;
            }

            return tinhGiaKhuyenMai(giaGoc, tyLeGiam);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal tinhGiaKhuyenMai (BigDecimal giaGoc, BigDecimal tyLeGiam) {
        return giaGoc.subtract(giaGoc.multiply(tyLeGiam)).max(BigDecimal.ZERO);
    }
}
