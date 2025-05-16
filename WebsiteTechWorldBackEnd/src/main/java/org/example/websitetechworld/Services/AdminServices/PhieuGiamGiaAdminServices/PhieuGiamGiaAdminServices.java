package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.persistence.EntityManager;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Repository.PhieuGiamGiaRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhieuGiamGiaAdminServices {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    public PhieuGiamGiaAdminServices(PhieuGiamGiaRepository phieuGiamGiaRepository, ModelMapper modelMapper, EntityManager entityManager) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;

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
            mapper.map(PhieuGiamGia::getSoDiemCanDeDoi, PhieuGiamGiaAdminResponse::setSoDiemCanDeDoi);
            mapper.map(PhieuGiamGia::getSoDiemCanDeDoi, PhieuGiamGiaAdminResponse::setHangToiThieu);
            mapper.map(PhieuGiamGia::getIsGlobal, PhieuGiamGiaAdminResponse::setIsGlobal);
            mapper.map(PhieuGiamGia::getTrangThai, PhieuGiamGiaAdminResponse::setTrangThai);
        });
    }

    public List<PhieuGiamGiaAdminResponse> getPagePhieuGiamGia (Integer pageNo, Integer pageSize){

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return phieuGiamGiaRepository.findAll(pageable).stream()
                .map(i -> modelMapper.map(i, PhieuGiamGiaAdminResponse.class)).toList();
    }

    public PhieuGiamGiaAdminResponse getPhieuGiamGia (Integer id) {

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    @Transactional
    public PhieuGiamGiaAdminResponse addPhieuGiamGia (PhieuGiamGiaAdminResponse phieuGiamGiaResponse) {

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.save(modelMapper.map(phieuGiamGiaResponse, PhieuGiamGia.class));
        entityManager.refresh(phieuGiamGia);

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    public PhieuGiamGiaAdminResponse updatePhieuGiamGia (Integer id, PhieuGiamGiaAdminResponse phieuGiamGiaResponse) {

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));

        phieuGiamGia.setTenKhuyenMai(phieuGiamGiaResponse.getTenKhuyenMai());
        phieuGiamGia.setLoaiKhuyenMai(phieuGiamGiaResponse.getLoaiKhuyenMai());
        phieuGiamGia.setGiaTriKhuyenMai(phieuGiamGiaResponse.getGiaTriKhuyenMai());
        phieuGiamGia.setGiaTriDonHangToiThieu(phieuGiamGiaResponse.getGiaTriDonHangToiThieu());
        phieuGiamGia.setGiaTriKhuyenMaiToiDa(phieuGiamGiaResponse.getGiaTriKhuyenMaiToiDa());
        phieuGiamGia.setNgayBatDau(phieuGiamGiaResponse.getNgayBatDau());
        phieuGiamGia.setNgayKetThuc(phieuGiamGiaResponse.getNgayKetThuc());
        phieuGiamGia.setDieuKienApDung(phieuGiamGiaResponse.getDieuKienApDung());
        phieuGiamGia.setHangToiThieu(phieuGiamGiaResponse.getHangToiThieu());
        phieuGiamGia.setSoLuong(phieuGiamGiaResponse.getSoLuong());
        phieuGiamGia.setSoDiemCanDeDoi(phieuGiamGiaResponse.getSoDiemCanDeDoi());
        phieuGiamGia.setIsGlobal(phieuGiamGiaResponse.getIsGlobal());
        phieuGiamGia.setTrangThai(phieuGiamGiaResponse.getTrangThai());

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    public String deletePhieuGiamGia (Integer id) {

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));
        phieuGiamGiaRepository.delete(phieuGiamGia);

        return "Đã xóa thành công phiếu giảm giá có id = " + id;
    }
}
