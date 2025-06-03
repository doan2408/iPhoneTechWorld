package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.XuatXuAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.XuatXuAdminResponse;
import org.example.websitetechworld.Entity.XuatXu;
import org.example.websitetechworld.Repository.XuatXuRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class XuatXuAdminService {
    private final XuatXuRepository xuatXuRepo;
    private final ModelMapper modelMapper;

    private XuatXuAdminResponse convert(XuatXu xuatXu) {
        return modelMapper.map(xuatXu, XuatXuAdminResponse.class);
    }

    private List<XuatXuAdminResponse> convertList(List<XuatXu> xuatXus) {
        return xuatXus.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Page<XuatXuAdminResponse> getAllXuatXu(Pageable pageable) {
        Page<XuatXu> xuatXuPage = xuatXuRepo.findAll(pageable);
        return xuatXuPage.map(this::convert);
    }

    public List<XuatXuAdminResponse> getAllXuatXuList() {
        List<XuatXu> xuatXus = xuatXuRepo.findAll();
        return convertList(xuatXus);
    }

    @Transactional
    public XuatXuAdminResponse createXuatXu(XuatXuAdminRequest xuatXuAdminRequest) {
        XuatXu xuatXu = xuatXuRepo.save(modelMapper.map(xuatXuAdminRequest, XuatXu.class));

        return convert(xuatXu);
    }

    @Transactional
    public XuatXuAdminResponse updateXuatXu(Integer id, XuatXuAdminRequest xuatXuAdminRequest) {
        XuatXu update = xuatXuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ ID: " + id));
        modelMapper.map(xuatXuAdminRequest, update);
        xuatXuRepo.save(update);

        return convert(update);
    }

    @Transactional
    public XuatXuAdminResponse deleteXuatXu(Integer id) {
        XuatXu delete = xuatXuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ ID: " + id));
        xuatXuRepo.deleteById(id);
        return convert(delete);
    }

    public XuatXuAdminResponse detailXuatXu(Integer id) {
        XuatXu detail = xuatXuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ ID: " + id));
        return convert(detail);
    }
}
