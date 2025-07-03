package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamChiTietAdminService {

    private final SanPhamChiTietRepository sanPhamChiTietRepo;
    private final SanPhamRepository sanPhamRepo;
    private final MauSacRepository mauSacRepo;
    private final RamRepository ramRepo;
    private final RomRepository romRepo;
    private final ManHinhRepository manHinhRepo;
    private final HeDieuHanhRepository heDieuHanhRepo;
    private final PinRepository pinRepo;
    private final CpuRepository cpuRepo;
    private final CameraTruocRepository cameraTruocRepo;
    private final CameraSauRepository cameraSauRepo;
    private final XuatXuRepository xuatXuRepo;
    private final LoaiRepository loaiRepo;


    private void mapRequestToEntity(SanPhamChiTietAdminRepuest req, SanPhamChiTiet entity) {
        entity.setMaSanPhamChiTiet(req.getMaSanPhamChiTiet());
        entity.setSoLuong(req.getSoLuong());
        entity.setGiaBan(req.getGiaBan());

        entity.setIdSanPham(sanPhamRepo.findById(req.getIdSanPham()).orElse(null));
        entity.setIdMau(mauSacRepo.findById(req.getIdMau()).orElse(null));
        entity.setIdRom(romRepo.findById(req.getIdRom()).orElse(null));
    }


    private SanPhamChiTietResponse mapEntityToResponse(SanPhamChiTiet entity) {
        SanPhamChiTietResponse response = new SanPhamChiTietResponse();

        response.setId(entity.getId());
        response.setMaSanPhamChiTiet(entity.getMaSanPhamChiTiet());
        response.setSoLuongSPCT(entity.getSoLuong());
        response.setGiaBan(entity.getGiaBan());

        if (entity != null) {
            response.setId(entity.getId());
            response.setMaSanPhamChiTiet(entity.getMaSanPhamChiTiet());
            response.setSoLuongSPCT(entity.getSoLuong());
            response.setGiaBan(entity.getGiaBan());
        }

        MauSac mauSac = entity.getIdMau();
        if (mauSac != null) {
            response.setTenMau(mauSac.getTenMau());
        }

        Rom rom = entity.getIdRom();
        if (rom != null) {
            response.setDungLuongRom(rom.getDungLuong());
            response.setLoaiRom(rom.getLoai());
            response.setTocDoDocGhiRom(rom.getTocDoDocGhi());
            response.setNhaSanXuatRom(rom.getNhaSanXuat());
            response.setNamRaMatRom(rom.getNamRaMat());
        }

        if (entity.getImeis() != null && !entity.getImeis().isEmpty()) {
            Imei firstImei = entity.getImeis().iterator().next();
            ImeiAdminResponse imeiResponse = new ImeiAdminResponse();
            imeiResponse.setSoImei(firstImei.getSoImei());
            imeiResponse.setTrangThaiImei(firstImei.getTrangThaiImei());
            response.setImeis(new LinkedHashSet<>(Collections.singletonList(imeiResponse)));
        } else {
            response.setImeis(new LinkedHashSet<>());
        }

        // Ánh xạ hinhAnhs (chỉ lấy phần tử đầu tiên)
        if (entity.getHinhAnhs() != null && !entity.getHinhAnhs().isEmpty()) {
            HinhAnh firstImage = entity.getHinhAnhs().iterator().next();
            HinhAnhAdminResponse hinhAnhResponse = new HinhAnhAdminResponse();
            hinhAnhResponse.setUrl(firstImage.getUrl());
            hinhAnhResponse.setImagePublicId(firstImage.getImagePublicId());
            response.setHinhAnhs(new LinkedHashSet<>(Collections.singletonList(hinhAnhResponse)));
        } else {
            response.setHinhAnhs(new LinkedHashSet<>());
        }

        return response;
    }


    public SanPhamChiTiet getChiTietById(Integer id) {
        return sanPhamChiTietRepo.findFullById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với ID: " + id));
    }

    //detail
    public SanPhamChiTietResponse getSanPhamChiTiet(SanPhamChiTiet entity) {

        SanPhamChiTietResponse response = mapEntityToResponse(entity);

        return response;
    }

    @Transactional
    public SanPhamChiTietResponse createSanPhamChiTiet(SanPhamChiTietAdminRepuest req) {
        SanPhamChiTiet spct = new SanPhamChiTiet();
        mapRequestToEntity(req, spct);

        SanPhamChiTiet save = sanPhamChiTietRepo.save(spct);

        return mapEntityToResponse(save);
    }

    @Transactional
    public SanPhamChiTietResponse updateSanPhamChiTiet(Integer id, SanPhamChiTietAdminRepuest spctReq) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phảm chi tiết ID: " + id));

        mapRequestToEntity(spctReq, sanPhamChiTiet);

        SanPhamChiTiet save = sanPhamChiTietRepo.save(sanPhamChiTiet);

        return mapEntityToResponse(save);
    }

    public Page<SanPhamChiTietHienThiResponse> getAllSanPhamChiTiet(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return sanPhamChiTietRepo.findByIdSanPham_TrangThaiSanPham(TrangThaiSanPham.ACTIVE,pageable).map(SanPhamChiTietHienThiResponse::converDto);
    }


}
