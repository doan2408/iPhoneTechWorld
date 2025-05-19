package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan;

import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImeiDaBanAdminServices {


    // tao imei da ban
    public List<ImeiDaBan> generateImeiDaBan(ChiTietHoaDon chiTietHoaDon, List<Imei> imeis) {
        return imeis.stream().map(imei -> {
            ImeiDaBan imeiDaBan = new ImeiDaBan();
            imeiDaBan.setIdHoaDonChiTiet(chiTietHoaDon);
            imeiDaBan.setSoImei(imei.getSoImei());
            imeiDaBan.setTrangThai(TrangThaiImei.RESERVED);
            return imeiDaBan;
        }).toList();
    }
}
