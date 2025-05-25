package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.persistence.EntityManager;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Repository.PhieuGiamGiaRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    //hien thi pgg
    public Page<PhieuGiamGiaAdminResponse> getPagePhieuGiamGia (
            String search,
            TrangThaiPGG trangThai,
            LocalDate ngayBatDau,
            LocalDate ngayKetThuc,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDate now = LocalDate.now();
        Page<PhieuGiamGia> pagePhieuGiamGia = phieuGiamGiaRepository.findAll(search, trangThai, ngayBatDau, ngayKetThuc, pageable);
        pagePhieuGiamGia.forEach(entity -> {
            if (now.isBefore(entity.getNgayBatDau())) {
                entity.setTrangThai(TrangThaiPGG.NOT_STARTED);
            } else if (now.isAfter(entity.getNgayKetThuc())) {
                entity.setTrangThai(TrangThaiPGG.EXPIRED);
            } else {
                entity.setTrangThai(TrangThaiPGG.ACTIVE);
            }
            phieuGiamGiaRepository.save(entity);
        });

        return pagePhieuGiamGia.map(entity -> modelMapper.map(entity, PhieuGiamGiaAdminResponse.class));
    }

    //detail pgg
    public PhieuGiamGiaAdminResponse getPhieuGiamGia (Integer id) {

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    //add pgg
    @Transactional
    public PhieuGiamGiaAdminResponse addPhieuGiamGia (PhieuGiamGiaAdminResponse phieuGiamGiaResponse) {

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.save(modelMapper.map(phieuGiamGiaResponse, PhieuGiamGia.class));
        entityManager.refresh(phieuGiamGia);

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    //update pgg
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

        return modelMapper.map(phieuGiamGiaRepository.save(phieuGiamGia), PhieuGiamGiaAdminResponse.class);
    }


    //delete pgg
    public String deletePhieuGiamGia (Integer id) {

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));
        phieuGiamGiaRepository.delete(phieuGiamGia);

        return "Đã xóa thành công phiếu giảm giá có id = " + id;
    }
}
