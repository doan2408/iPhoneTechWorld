package org.example.websitetechworld.Mapper.Client;

import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderProductClientResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyReviewClientResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.HoaDon.HanhDongLichSuHoaDon;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MyOrderClientMapper {

    public MyOrderClientResponse toMyOrderClientResponse(HoaDon hoaDon){
        MyOrderClientResponse dto = new MyOrderClientResponse();
        dto.setIdHoaDon(hoaDon.getId());
        dto.setMaHoaDon(hoaDon.getMaHoaDon());
        dto.setMaVanDon(hoaDon.getMaVanDon());
        if (hoaDon.getTrangThaiDonHang() != null ){
            dto.setTrangThaiGiaoHang(hoaDon.getTrangThaiDonHang().getDisplayName());
        }
        if (hoaDon.getTrangThaiThanhToan() != null){
            dto.setTrangThaiThanhToan(hoaDon.getTrangThaiThanhToan().getDisplayName());
        }
        dto.setThanhTien(hoaDon.getThanhTien());
        dto.setNgayDatHang(hoaDon.getNgayDatHang());
        dto.setMyOrderClientResponseList(
                hoaDon.getChiTietHoaDons().stream().map(this::toMyOrderProductClientResponse)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    public MyOrderProductClientResponse toMyOrderProductClientResponse(ChiTietHoaDon chiTietHoaDon){
        MyOrderProductClientResponse dto = new MyOrderProductClientResponse();
        dto.setIdSanPhamChiTiet(chiTietHoaDon.getIdSanPhamChiTiet().getId()); //cường
        dto.setTenSanPham(chiTietHoaDon.getTenSanPham());
        dto.setGiaSanPham(chiTietHoaDon.getDonGia());
        Set<HinhAnh> hinhAnhs  = chiTietHoaDon.getIdSanPhamChiTiet().getHinhAnhs();
        if (hinhAnhs != null && !hinhAnhs.isEmpty()){
            HinhAnh hinhAnhDauTien = hinhAnhs.iterator().next();
            String urlDauTien = hinhAnhDauTien.getUrl();
            dto.setUrlImage(urlDauTien);
        }
        dto.setSoLuong(chiTietHoaDon.getSoLuong());
        dto.setDungLuongRom(chiTietHoaDon.getIdSanPhamChiTiet().getIdRom().getDungLuong());
        dto.setColorName(chiTietHoaDon.getIdSanPhamChiTiet().getIdMau().getTenMau());
        return dto;
    }


    public MyReviewClientResponse toMyReviewClientResponse(HoaDon hoaDon){
        MyReviewClientResponse dto = new MyReviewClientResponse();
        dto.setIdHoaDon(hoaDon.getId());
        dto.setMaVanDon(hoaDon.getMaVanDon());
        if (hoaDon.getTrangThaiDonHang() != null ){
            dto.setTrangThaiGiaoHang(hoaDon.getTrangThaiDonHang().getDisplayName());
        }
        if (hoaDon.getTrangThaiThanhToan() != null){
            dto.setTrangThaiThanhToan(hoaDon.getTrangThaiThanhToan().getDisplayName());
        }
        dto.setThanhTien(hoaDon.getThanhTien());
        dto.setNgayThanhToan(hoaDon.getNgayThanhToan());
        if (hoaDon.getLichSuHoaDons() != null) {
            hoaDon.getLichSuHoaDons().stream()
                    .filter(ls -> ls.getHanhDong() == HanhDongLichSuHoaDon.THANH_TOAN) // enum COMPLETE
                    .findFirst()
                    .ifPresent(ls -> dto.setNgayNhanhang(ls.getThoiGianThayDoi())); // hoặc ls.getNgayThucHien()
        }
        dto.setMyOrderClientResponseList(
                hoaDon.getChiTietHoaDons().stream().map(this::toMyOrderProductClientResponse)
                        .collect(Collectors.toList())
        );
        return dto;
    }

}
