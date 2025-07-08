package org.example.websitetechworld.Services.ClientServices.GioHangClientService;

import org.example.websitetechworld.Dto.Request.ClientRequest.GioHangClientRequest.GioHangClientRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.GioHangClientResponse.GioHangClientResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Repository.GioHangChiTietRepository;
import org.example.websitetechworld.Repository.GioHangRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class GioHangClientService {

    private final GioHangRepository gioHangRepository;
    private final GioHangChiTietRepository gioHangChiTietRepository;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public GioHangClientService(GioHangRepository gioHangRepository, GioHangChiTietRepository gioHangChiTietRepository, KhachHangRepository khachHangRepository, SanPhamChiTietRepository sanPhamChiTietRepository) {
        this.gioHangRepository = gioHangRepository;
        this.gioHangChiTietRepository = gioHangChiTietRepository;
        this.khachHangRepository = khachHangRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
    }

    public GioHangClientResponse layGioHang (Integer idKhachHang) {
        GioHang gioHang = gioHangRepository.findByIdKhachHangId(idKhachHang);
        if (gioHang == null) {
            return new GioHangClientResponse();
        }
        return mapToResponseDTO(gioHang);
    }

    @Transactional
    public GioHangClientResponse themSanPhamVaoGio (GioHangClientRequest request) {
        GioHang gioHang = gioHangRepository.findByIdKhachHangId(request.getIdKhachHang());
        if (gioHang == null) {
            gioHang = new GioHang();
            KhachHang khachHang = khachHangRepository.findById(request.getIdKhachHang())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng có id: " + request.getIdKhachHang()));
            gioHang.setIdKhachHang(khachHang);
            gioHang = gioHangRepository.save(gioHang);
        }

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy spct có id: " + request.getIdSanPhamChiTiet()));

        GioHangChiTiet item = new GioHangChiTiet();
        item.setIdGioHang(gioHang);
        item.setIdSanPhamChiTiet(sanPhamChiTiet);
        item.setSoLuong(request.getSoLuong());
        item.setGia(sanPhamChiTiet.getGiaBan());
        item.setNgayThem(LocalDate.now());
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
    public void xoaSanPhamKhoiGio (Integer idGioHangChiTiet) {
        gioHangChiTietRepository.deleteById(idGioHangChiTiet);
    }

    @Transactional
    public void xoaGioHang (Integer idKhachHang) {
        GioHang gioHang = gioHangRepository.findByIdKhachHangId(idKhachHang);
        if (gioHang != null) {
            gioHangChiTietRepository.deleteAll(gioHang.getGioHangChiTiets());
        }
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
            itemDTO.setGia(item.getGia());
            itemDTO.setSoLuong(item.getSoLuong());
            itemDTO.setSoLuongTon(item.getIdSanPhamChiTiet().getSoLuong());
            itemDTO.setNgayThem(item.getNgayThem());
            return itemDTO;
        }).collect(Collectors.toList()));
        return response;
    }
}
