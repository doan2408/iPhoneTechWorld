package org.example.websitetechworld.Mapper.Admin.BaoHanh;

import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhOfProductResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.YeuCauBaoHanhAdminResponse;
import org.example.websitetechworld.Entity.BaoHanh;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Entity.LoaiBaoHanh;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class BaoHanhAdminMappper {

    public YeuCauBaoHanhAdminResponse toYeuCauBaoHanhAdminResponse(ImeiDaBan imeiDaBan){
        YeuCauBaoHanhAdminResponse response = new YeuCauBaoHanhAdminResponse();
        response.setIdImei(imeiDaBan.getId());
        response.setSoImei(imeiDaBan.getSoImei());
        if (imeiDaBan.getIdHoaDonChiTiet() != null){
            response.setTenSanPham(imeiDaBan.getIdHoaDonChiTiet().getTenSanPham());
            if (imeiDaBan.getIdHoaDonChiTiet().getIdSanPhamChiTiet() != null){
                if (imeiDaBan.getIdHoaDonChiTiet().getIdSanPhamChiTiet().getIdMau() != null){
                    response.setMau(imeiDaBan.getIdHoaDonChiTiet().getIdSanPhamChiTiet().getIdMau().getTenMau());
                }
                if (imeiDaBan.getIdHoaDonChiTiet().getIdSanPhamChiTiet().getIdRom() != null){
                    response.setDungLuong(imeiDaBan.getIdHoaDonChiTiet().getIdSanPhamChiTiet().getIdRom().getDungLuong());
                }
            }
            if (imeiDaBan.getIdHoaDonChiTiet().getIdHoaDon() != null){
                response.setNgayMuaHang(imeiDaBan.getIdHoaDonChiTiet().getIdHoaDon().getNgayTaoHoaDon());
            }
        }
        response.setLstBaoHanh(
                imeiDaBan.getIdBaoHanh() == null ? Collections.emptyList() :
                        imeiDaBan.getIdBaoHanh().stream()
                                .map(this::toBaoHanhOfProductResponse)
                                .collect(Collectors.toList())
        );
        return response;
    }

    public BaoHanhOfProductResponse toBaoHanhOfProductResponse(BaoHanh baoHanh){
        BaoHanhOfProductResponse response = new BaoHanhOfProductResponse();
        response.setIdBaoHanh(baoHanh.getId());
        if (baoHanh.getIdLoaiBaoHanh() != null){
            response.setTenLoaiBaoHanh(baoHanh.getIdLoaiBaoHanh().getTenLoaiBaoHanh());
            response.setThoiGianThang(baoHanh.getIdLoaiBaoHanh().getThoiGianThang());
            response.setIdLoaiBaoHanh(baoHanh.getIdLoaiBaoHanh().getId());
        }
        response.setNgayHetHan(baoHanh.getNgayKetThuc());
        response.setTrangThaiBaoHanh(baoHanh.getTrangThaiBaoHanh());
        return response;
    }
}
