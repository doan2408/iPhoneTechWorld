package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.persistence.EntityManager;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.KhachHangGiamGia;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Repository.KhachHangGiamGiaRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaAdminServices {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    public PhieuGiamGiaAdminServices(
            PhieuGiamGiaRepository phieuGiamGiaRepository,
            KhachHangGiamGiaRepository khachHangGiamGiaRepository,
            KhachHangRepository khachHangRepository,
            ModelMapper modelMapper,
            EntityManager entityManager) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangGiamGiaRepository = khachHangGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;

        configureModelMapper();
    }

    private void configureModelMapper() {
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
            mapper.map(PhieuGiamGia::getSoLuong, PhieuGiamGiaAdminResponse::setSoLuong);
            mapper.map(PhieuGiamGia::getSoDiemCanDeDoi, PhieuGiamGiaAdminResponse::setSoDiemCanDeDoi);
            mapper.map(PhieuGiamGia::getIsGlobal, PhieuGiamGiaAdminResponse::setIsGlobal);
            mapper.map(PhieuGiamGia::getTrangThai, PhieuGiamGiaAdminResponse::setTrangThai);
        });
    }

    public Page<PhieuGiamGiaAdminResponse> getPagePhieuGiamGia(
            String search, TrangThaiPGG trangThai, LocalDate ngayBatDau, LocalDate ngayKetThuc,
            int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
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

        return pagePhieuGiamGia.map(entity -> {
            PhieuGiamGiaAdminResponse response = modelMapper.map(entity, PhieuGiamGiaAdminResponse.class);
            if (entity.getIsGlobal()) {
                response.setKhachHangIds(khachHangGiamGiaRepository.findKhachHangIdsByPhieuGiamGiaId(entity));
            } else {
                response.setKhachHangIds(List.of());
            }
            return response;
        });
    }

    public PhieuGiamGiaAdminResponse getPhieuGiamGia(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));
        PhieuGiamGiaAdminResponse response = modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
        if (phieuGiamGia.getIsGlobal()) {
            response.setKhachHangIds(khachHangGiamGiaRepository.findKhachHangIdsByPhieuGiamGiaId(phieuGiamGia));
        } else {
            response.setKhachHangIds(List.of());
        }
        return response;
    }

    @Transactional
    public PhieuGiamGiaAdminResponse addPhieuGiamGia(PhieuGiamGiaAdminResponse phieuGiamGiaResponse) {
        if (phieuGiamGiaResponse.getIsGlobal() && (phieuGiamGiaResponse.getKhachHangIds() == null || phieuGiamGiaResponse.getKhachHangIds().isEmpty())) {
            throw new IllegalArgumentException("Phiếu giảm giá riêng tư phải có ít nhất một khách hàng được chọn");
        }
        if (phieuGiamGiaResponse.getNgayBatDau().isAfter(phieuGiamGiaResponse.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        PhieuGiamGia phieuGiamGia = modelMapper.map(phieuGiamGiaResponse, PhieuGiamGia.class);
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        if (phieuGiamGia.getIsGlobal()) {

            for (Integer khachHangId : phieuGiamGiaResponse.getKhachHangIds()) {
                if (!khachHangRepository.existsById(khachHangId)) {
                    throw new IllegalArgumentException("Khách hàng ID " + khachHangId + " không tồn tại");
                }
            }
            saveKhachHangGiamGia(phieuGiamGia, phieuGiamGiaResponse.getKhachHangIds());
        }
        PhieuGiamGiaAdminResponse response = modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
        response.setKhachHangIds(phieuGiamGiaResponse.getKhachHangIds());
        return response;
    }

    @Transactional
    public PhieuGiamGiaAdminResponse updatePhieuGiamGia(Integer id, PhieuGiamGiaAdminResponse phieuGiamGiaResponse) {

        if (phieuGiamGiaResponse.getIsGlobal() && (phieuGiamGiaResponse.getKhachHangIds() == null || phieuGiamGiaResponse.getKhachHangIds().isEmpty())) {
            throw new IllegalArgumentException("Phiếu giảm giá riêng tư phải có ít nhất một khách hàng được chọn");
        }
        if (phieuGiamGiaResponse.getNgayBatDau().isAfter(phieuGiamGiaResponse.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));
        modelMapper.map(phieuGiamGiaResponse, phieuGiamGia);
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        if (phieuGiamGia.getIsGlobal()) {

            List<KhachHangGiamGia> existingRecords = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
            Set<Integer> existingKhachHangIds = existingRecords.stream()
                    .map(khgg -> khachHangGiamGiaRepository.getKhachHangId(khgg))
                    .collect(Collectors.toSet());
            Set<Integer> newKhachHangIds = new HashSet<>(phieuGiamGiaResponse.getKhachHangIds());

            for (Integer khachHangId : newKhachHangIds) {
                if (!khachHangRepository.existsById(khachHangId)) {
                    throw new IllegalArgumentException("Khách hàng ID " + khachHangId + " không tồn tại");
                }
            }

            for (Integer khachHangId : newKhachHangIds) {

                if (existingKhachHangIds.contains(khachHangId) || khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIdKhachHang(phieuGiamGia, khachHangId)) {

                    KhachHangGiamGia existingRecord = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIdKhachHang(phieuGiamGia, khachHangId);
                    if (existingRecord != null && existingRecord.getIsUser()) {
                        throw new IllegalStateException("Khách hàng ID " + khachHangId + " đã sử dụng phiếu này");
                    }
                } else {
                    createNewKhachHangGiamGia(phieuGiamGia, khachHangId);
                }
            }

            for (KhachHangGiamGia record : existingRecords) {
                Integer khachHangId = khachHangGiamGiaRepository.getKhachHangId(record);
                if (!newKhachHangIds.contains(khachHangId)) {
                    khachHangGiamGiaRepository.delete(record);
                }
            }
        } else {

            List<KhachHangGiamGia> unusedRecords = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
            khachHangGiamGiaRepository.deleteAll(unusedRecords);
        }
        
        PhieuGiamGiaAdminResponse response = modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
        response.setKhachHangIds(phieuGiamGiaResponse.getKhachHangIds());
        return response;
    }

    @Transactional
    public String deletePhieuGiamGia(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));

        khachHangGiamGiaRepository.deleteByIdPhieuGiamGiaId(id);
        phieuGiamGiaRepository.delete(phieuGiamGia);
        return "Đã xóa thành công phiếu giảm giá có id = " + id;
    }

    public List<KhachHangGiamGiaResponse> getAllKhachHang() {
        return khachHangRepository.findAll().stream()
                .map(kh -> new KhachHangGiamGiaResponse(kh.getId(), kh.getTenKhachHang()))
                .collect(Collectors.toList());
    }

    private void createNewKhachHangGiamGia(PhieuGiamGia phieuGiamGia, Integer khachHangId) {
        KhachHangGiamGia khachHangGiamGia = new KhachHangGiamGia();
        khachHangGiamGia.setIdPhieuGiamGia(phieuGiamGia);
        KhachHang khachHang = new KhachHang();
        khachHang.setId(khachHangId);
        khachHangGiamGia.setIdKhachHang(khachHang);
        khachHangGiamGia.setIsUser(false);
        khachHangGiamGia.setNgayCap(LocalDate.now());
        khachHangGiamGiaRepository.save(khachHangGiamGia);
    }

    private void saveKhachHangGiamGia(PhieuGiamGia phieuGiamGia, List<Integer> khachHangIds) {
        if (khachHangIds != null) {
            for (Integer khachHangId : khachHangIds) {
                createNewKhachHangGiamGia(phieuGiamGia, khachHangId);
            }
        }
    }
}