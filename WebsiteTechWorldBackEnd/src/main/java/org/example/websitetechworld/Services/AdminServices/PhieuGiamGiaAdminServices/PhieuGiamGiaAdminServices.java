package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.persistence.EntityManager;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
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
import java.util.*;
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

        TypeMap<PhieuGiamGiaAdminRequest, PhieuGiamGia> requestMapper = modelMapper.createTypeMap(PhieuGiamGiaAdminRequest.class, PhieuGiamGia.class);
        requestMapper.addMappings(mapper -> mapper.skip(PhieuGiamGia::setKhachHangGiamGias));
    }

    public Page<PhieuGiamGiaAdminResponse> getPagePhieuGiamGia(
            String search, TrangThaiPGG trangThai, LocalDate ngayBatDau, LocalDate ngayKetThuc,
            int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PhieuGiamGia> pagePhieuGiamGia = phieuGiamGiaRepository.findAll(search, trangThai, ngayBatDau, ngayKetThuc, pageable);
        return pagePhieuGiamGia.map(this::mapToResponse);
    }

    public PhieuGiamGiaAdminResponse getPhieuGiamGia(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));
        return mapToResponse(phieuGiamGia);
    }

    private PhieuGiamGiaAdminResponse mapToResponse(PhieuGiamGia phieuGiamGia) {
        PhieuGiamGiaAdminResponse response = modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
        if (!phieuGiamGia.getIsGlobal()) {
            List<Integer> khachHangIds = phieuGiamGia.getKhachHangGiamGias().stream()
                    .map(khgg -> khgg.getIdKhachHang().getId())
                    .collect(Collectors.toList());
            response.setKhachHangIds(khachHangIds);

            // Suy ra khachHangMoi và khachHangCu
            List<Integer> newCustomerIds = khachHangRepository.findNewCustomers().stream()
                    .map(KhachHang::getId)
                    .toList();
            List<Integer> oldCustomerIds = khachHangRepository.findOldCustomers().stream()
                    .map(KhachHang::getId)
                    .toList();
            response.setKhachHangMoi(!Collections.disjoint(khachHangIds, newCustomerIds));
            response.setKhachHangCu(!Collections.disjoint(khachHangIds, oldCustomerIds));
        } else {
            response.setKhachHangIds(List.of());
            response.setKhachHangMoi(false);
            response.setKhachHangCu(false);
        }
        return response;
    }

    @Transactional
    public PhieuGiamGiaAdminResponse addPhieuGiamGia(PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        // Kiểm tra ngày
        if (phieuGiamGiaAdminRequest.getNgayBatDau().isAfter(phieuGiamGiaAdminRequest.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        // Kiểm tra logic isGlobal và khachHangIds/khachHangMoi/khachHangCu
        if (!phieuGiamGiaAdminRequest.getIsGlobal() &&
                (phieuGiamGiaAdminRequest.getKhachHangIds() == null || phieuGiamGiaAdminRequest.getKhachHangIds().isEmpty()) &&
                !phieuGiamGiaAdminRequest.getKhachHangMoi() &&
                !phieuGiamGiaAdminRequest.getKhachHangCu()) {
            throw new IllegalArgumentException("Phiếu giảm giá riêng tư phải có ít nhất một khách hàng hoặc nhóm khách hàng được chọn");
        }

        // Ánh xạ DTO sang entity
        PhieuGiamGia phieuGiamGia = modelMapper.map(phieuGiamGiaAdminRequest, PhieuGiamGia.class);
        phieuGiamGia.setKhachHangGiamGias(new LinkedHashSet<>());
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        // Xử lý khách hàng nếu không global
        if (!phieuGiamGia.getIsGlobal()) {
            Set<Integer> allKhachHangIds = new HashSet<>();
            if (phieuGiamGiaAdminRequest.getKhachHangIds() != null) {
                allKhachHangIds.addAll(phieuGiamGiaAdminRequest.getKhachHangIds());
            }

            // Thêm khách mới
            if (phieuGiamGiaAdminRequest.getKhachHangMoi()) {
                List<Integer> newCustomerIds = khachHangRepository.findNewCustomers().stream()
                        .map(KhachHang::getId)
                        .toList();
                allKhachHangIds.addAll(newCustomerIds);
            }

            // Thêm khách cũ
            if (phieuGiamGiaAdminRequest.getKhachHangCu()) {
                List<Integer> oldCustomerIds = khachHangRepository.findOldCustomers().stream()
                        .map(KhachHang::getId)
                        .toList();
                allKhachHangIds.addAll(oldCustomerIds);
            }

            // Kiểm tra tồn tại
            List<Integer> invalidIds = allKhachHangIds.stream()
                    .filter(id -> !khachHangRepository.existsById(id))
                    .toList();
            if (!invalidIds.isEmpty()) {
                throw new IllegalArgumentException("Các ID khách hàng không tồn tại: " + invalidIds);
            }

            // Lưu khách hàng vào KhachHangGiamGia
            for (Integer khachHangId : allKhachHangIds) {
                KhachHang khachHang = khachHangRepository.findById(khachHangId)
                        .orElseThrow(() -> new IllegalArgumentException("Khách hàng ID " + khachHangId + " không tồn tại"));
                createNewKhachHangGiamGia(phieuGiamGia, khachHang);
            }
        }

        return mapToResponse(phieuGiamGia);
    }

    @Transactional
    public PhieuGiamGiaAdminResponse updatePhieuGiamGia(Integer id, PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        // Kiểm tra ngày
        if (phieuGiamGiaAdminRequest.getNgayBatDau().isAfter(phieuGiamGiaAdminRequest.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        // Kiểm tra logic isGlobal và khachHangIds/khachHangMoi/khachHangCu
        if (!phieuGiamGiaAdminRequest.getIsGlobal() &&
                (phieuGiamGiaAdminRequest.getKhachHangIds() == null || phieuGiamGiaAdminRequest.getKhachHangIds().isEmpty()) &&
                !phieuGiamGiaAdminRequest.getKhachHangMoi() &&
                !phieuGiamGiaAdminRequest.getKhachHangCu()) {
            throw new IllegalArgumentException("Phiếu giảm giá riêng tư phải có ít nhất một khách hàng hoặc nhóm khách hàng được chọn");
        }

        // Ánh xạ DTO sang entity
        modelMapper.map(phieuGiamGiaAdminRequest, phieuGiamGia);

        // Xử lý mối quan hệ KhachHangGiamGia
        if (phieuGiamGia.getIsGlobal()) {
            List<KhachHangGiamGia> unusedRecords = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
            khachHangGiamGiaRepository.deleteAll(unusedRecords);
            phieuGiamGia.getKhachHangGiamGias().clear();
        } else {
            Set<Integer> newKhachHangIds = new HashSet<>();
            if (phieuGiamGiaAdminRequest.getKhachHangIds() != null) {
                newKhachHangIds.addAll(phieuGiamGiaAdminRequest.getKhachHangIds());
            }

            // Thêm khách mới
            if (phieuGiamGiaAdminRequest.getKhachHangMoi()) {
                List<Integer> newCustomerIds = khachHangRepository.findNewCustomers().stream()
                        .map(KhachHang::getId)
                        .toList();
                newKhachHangIds.addAll(newCustomerIds);
            }

            // Thêm khách cũ
            if (phieuGiamGiaAdminRequest.getKhachHangCu()) {
                List<Integer> oldCustomerIds = khachHangRepository.findOldCustomers().stream()
                        .map(KhachHang::getId)
                        .toList();
                newKhachHangIds.addAll(oldCustomerIds);
            }

            // Kiểm tra tồn tại
            List<Integer> invalidIds = newKhachHangIds.stream()
                    .filter(khId -> !khachHangRepository.existsById(khId))
                    .toList();
            if (!invalidIds.isEmpty()) {
                throw new IllegalArgumentException("Các ID khách hàng không tồn tại: " + invalidIds);
            }

            // Lấy danh sách khách hàng hiện tại
            Set<Integer> existingKhachHangIds = phieuGiamGia.getKhachHangGiamGias().stream()
                    .map(khgg -> khgg.getIdKhachHang().getId())
                    .collect(Collectors.toSet());

            // Thêm khách hàng mới
            for (Integer khachHangId : newKhachHangIds) {
                if (!existingKhachHangIds.contains(khachHangId)) {
                    KhachHang khachHang = khachHangRepository.findById(khachHangId)
                            .orElseThrow(() -> new IllegalArgumentException("Khách hàng ID " + khachHangId + " không tồn tại"));
                    createNewKhachHangGiamGia(phieuGiamGia, khachHang);
                }
            }

            // Xóa các bản ghi không còn trong danh sách mới
            List<KhachHangGiamGia> recordsToRemove = phieuGiamGia.getKhachHangGiamGias().stream()
                    .filter(khgg -> !newKhachHangIds.contains(khgg.getIdKhachHang().getId()) && !khgg.getIsUser())
                    .toList();
            for (KhachHangGiamGia record : recordsToRemove) {
                phieuGiamGia.getKhachHangGiamGias().remove(record);
                khachHangGiamGiaRepository.delete(record);
            }
        }

        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);
        return mapToResponse(phieuGiamGia);
    }

    @Transactional
    public String deletePhieuGiamGia(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá"));

        boolean hasUsed = khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIsUser(phieuGiamGia, true);
        if (hasUsed) {
            throw new IllegalStateException("Không thể xóa phiếu giảm giá đã được sử dụng");
        }

        khachHangGiamGiaRepository.deleteByIdPhieuGiamGiaId(id);
        phieuGiamGiaRepository.delete(phieuGiamGia);
        return "Đã xóa thành công phiếu giảm giá có id = " + id;
    }

    public Page<KhachHangGiamGiaResponse> getAllKhachHang (String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> khachHangPage = (search == null || search.isEmpty()) ?
                khachHangRepository.findTrangThai_Active(pageable) :
                khachHangRepository.findByTenKhachHangContainingIgnoreCaseAndTrangThai_Active(search, pageable);
        return khachHangPage.map(kh -> new KhachHangGiamGiaResponse(kh.getId(),kh.getMaKhachHang(), kh.getTenKhachHang()));
    }

    private void createNewKhachHangGiamGia(PhieuGiamGia phieuGiamGia, KhachHang khachHang) {
        KhachHangGiamGia khachHangGiamGia = new KhachHangGiamGia();
        khachHangGiamGia.setIdPhieuGiamGia(phieuGiamGia);
        khachHangGiamGia.setIdKhachHang(khachHang);
        khachHangGiamGia.setIsUser(false);
        khachHangGiamGia.setNgayCap(LocalDate.now());
        phieuGiamGia.getKhachHangGiamGias().add(khachHangGiamGia);
        khachHangGiamGiaRepository.save(khachHangGiamGia);
    }

    @Transactional
    public void updateTrangThaiPhieuGiamGia() {
        LocalDate now = LocalDate.now();
        List<PhieuGiamGia> phieuGiamGias = phieuGiamGiaRepository.findAll();
        for (PhieuGiamGia entity : phieuGiamGias) {
            if (now.isBefore(entity.getNgayBatDau())) {
                entity.setTrangThai(TrangThaiPGG.NOT_STARTED);
            } else if (now.isAfter(entity.getNgayKetThuc())) {
                entity.setTrangThai(TrangThaiPGG.EXPIRED);
            } else {
                entity.setTrangThai(TrangThaiPGG.ACTIVE);
            }
            phieuGiamGiaRepository.save(entity);
        }
    }
}