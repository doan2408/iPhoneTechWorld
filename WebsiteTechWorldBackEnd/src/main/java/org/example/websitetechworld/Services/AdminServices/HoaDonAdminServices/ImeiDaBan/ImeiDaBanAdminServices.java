package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiDaBangAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiTrangHoaDonResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.ImeiDaBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImeiDaBanAdminServices {
    private final ImeiDaBanRepository imeiDaBanRepository;

    public ImeiDaBanAdminServices(ImeiDaBanRepository imeiDaBanRepository) {
        this.imeiDaBanRepository = imeiDaBanRepository;
    }

    // tao imei da ban
    public List<ImeiDaBan> generateImeiDaBan(ChiTietHoaDon chiTietHoaDon, List<Imei> imeis,TrangThaiImei trangThaiImei) {
        return imeis.stream().map(imei -> {
            ImeiDaBan imeiDaBan = new ImeiDaBan();
            imeiDaBan.setIdHoaDonChiTiet(chiTietHoaDon);
            imeiDaBan.setSoImei(imei.getSoImei());
            imeiDaBan.setTrangThai(trangThaiImei);
            return imeiDaBan;
        }).toList();
    }

    public List<ImeiDaBangAdminResponse> getImeiDaBanByIdCthd(Integer idCthd) {
        List<ImeiDaBan> imeiEntities = imeiDaBanRepository.findByIdHoaDonChiTiet_Id(idCthd);
        return imeiEntities.stream()
                .map(entity -> new ImeiDaBangAdminResponse(entity.getId(), entity.getSoImei(),entity.getTrangThai().getDisplayName(), entity.getIdHoaDonChiTiet().getId()))
                .collect(Collectors.toList());
    }

    public Page<ImeiTrangHoaDonResponse> imeiTrangHoaDonList(int pageNo, int pageSize,Integer idHoaDon) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return imeiDaBanRepository.imeiTrongHdct(idHoaDon,pageable);
    }

    public List<ImeiTrangHoaDonResponse> imeiTrangHoaDonList(Integer idHoaDon) {
        return imeiDaBanRepository.imeiTrongHdct(idHoaDon);
    }
}
