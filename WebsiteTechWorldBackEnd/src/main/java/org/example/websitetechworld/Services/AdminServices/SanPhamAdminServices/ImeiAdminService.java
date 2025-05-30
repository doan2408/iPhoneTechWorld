package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.Loai;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Repository.ImeiReposiory;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ImeiAdminService {
    private final ImeiReposiory imeiReposiory;
    private final SanPhamChiTietRepository sanPhamChiTietRepo;

    private final ModelMapper modelMapper;

    public ImeiAdminResponse convert(Imei imei) {
        return modelMapper.map(imei, ImeiAdminResponse.class);
    }

    public Page<ImeiAdminResponse> getAllImei(Pageable pageable) {
        Page<Imei> imeiList = imeiReposiory.findAll(pageable);
        return imeiList.map(this::convert);
    }

    @Transactional
    public ImeiAdminResponse createImei(ImeiAdminRequest req) {
        Imei imei = imeiReposiory.save(modelMapper.map(req, Imei.class));
        return convert(imei);
    }

    @Transactional
    public ImeiAdminResponse updateImei(Integer id, ImeiAdminRequest req) {
        Imei imei = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        imei.setSoImei(req.getSoImei());
        imei.setNhaMang(req.getNhaMang());
        imei.setTrangThaiImei(req.getTrangThaiImei());
        imei.setIdSanPhamChiTiet(sanPhamChiTietRepo.findById(req.getIdSanPhamChiTiet()).orElse(null));

        imeiReposiory.save(modelMapper.map(imei, Imei.class));

        return convert(imei);
    }

    @Transactional
    public ImeiAdminResponse deleteImei(Integer id) {
        Imei imei = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        imeiReposiory.deleteById(id);

        return convert(imei);
    }

    public ImeiAdminResponse detailImei(Integer id) {
        Imei imei = imeiReposiory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Imei ID: " + id));
        return convert(imei);
    }
}
