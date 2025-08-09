package org.example.websitetechworld.Services.AdminServices.PhanHoiAdminService;


import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhanHoiDanhGiaAdminRequest.PhanHoiDanhGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhanHoiDanhGiaAdminResponse.PhanHoiDanhGiaAdminResponse;
import org.example.websitetechworld.Entity.DanhGiaSanPham;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Entity.PhanHoiDanhGia;
import org.example.websitetechworld.Repository.DanhGiaSanPhamRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.example.websitetechworld.Repository.PhanHoiDanhGiaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhanHoiDanhGiaAdminService {

    private final NhanVienRepository nhanVienRepository;

    private final DanhGiaSanPhamRepository danhGiaSanPhamRepository;

    private final PhanHoiDanhGiaRepository phanHoiDanhGiaRepository;


    public PhanHoiDanhGiaAdminResponse create(Integer idDanhGia ,PhanHoiDanhGiaAdminRequest dto) {
        DanhGiaSanPham danhGiaSanPham = danhGiaSanPhamRepository.findById(idDanhGia)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại"));

        Optional<PhanHoiDanhGia> existing = phanHoiDanhGiaRepository.findByDanhGiaSanPham_IdDanhGia(idDanhGia);
        if (existing.isPresent()) {
            throw new RuntimeException("Đánh giá này đã được phản hồi. Vui lòng chỉnh sửa phản hồi hiện có.");
        }

        String noiDung = dto.getNoiDungPhanHoi();
        if (noiDung == null || noiDung.trim().isEmpty()) {
            throw new RuntimeException("Nội dung phản hồi không được để trống.");
        }
        if (noiDung.length() > 500) {
            throw new RuntimeException("Nội dung phản hồi tối đa 500 ký tự.");
        }

        NhanVien nhanVien = nhanVienRepository.findById(dto.getIdNhanVien())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        // 5. Lưu phản hồi
        PhanHoiDanhGia phanHoi = new PhanHoiDanhGia();
        phanHoi.setDanhGiaSanPham(danhGiaSanPham);
        phanHoi.setNoiDungPhanHoi(noiDung);
        phanHoi.setNgayPhanHoi(LocalDateTime.now());
        phanHoi.setNhanVien(nhanVien);

        PhanHoiDanhGia saved = phanHoiDanhGiaRepository.save(phanHoi);

        // 6. Trả về response
        PhanHoiDanhGiaAdminResponse response = new PhanHoiDanhGiaAdminResponse();
        response.setIdPhanHoi(saved.getIdPhanHoi());
        response.setDanhGiaSanPham(saved.getDanhGiaSanPham().getIdDanhGia());
        response.setNoiDungPhanHoi(saved.getNoiDungPhanHoi());
        response.setNgayPhanHoi(saved.getNgayPhanHoi());
        response.setIdNhanVien(saved.getNhanVien().getId());
        return response;
    }

    public PhanHoiDanhGia update(Integer id, PhanHoiDanhGiaAdminRequest dto) {
        PhanHoiDanhGia phanHoi = phanHoiDanhGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phản hồi không tồn tại"));

        phanHoi.setNoiDungPhanHoi(dto.getNoiDungPhanHoi());
        NhanVien nhanVien = nhanVienRepository.findById(dto.getIdNhanVien())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        phanHoi.setNhanVien(nhanVien);

        return phanHoiDanhGiaRepository.save(phanHoi);
    }

    public void delete(Integer id) {
        phanHoiDanhGiaRepository.deleteById(id);
    }

    public List<PhanHoiDanhGiaAdminResponse> getPhanHoiByDanhGia(Integer idDanhGia) {
        findDanhGia(idDanhGia); // Kiểm tra đánh giá tồn tại
        return phanHoiDanhGiaRepository.findByDanhGiaSanPhamIdDanhGia(idDanhGia)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PhanHoiDanhGiaAdminResponse convertToResponse(PhanHoiDanhGia phanHoi) {
        PhanHoiDanhGiaAdminResponse response = new PhanHoiDanhGiaAdminResponse();
        response.setIdPhanHoi(phanHoi.getIdPhanHoi());
        response.setNoiDungPhanHoi(phanHoi.getNoiDungPhanHoi());
        response.setNgayPhanHoi(phanHoi.getNgayPhanHoi());
        response.setDanhGiaSanPham(phanHoi.getDanhGiaSanPham().getIdDanhGia());
        response.setIdNhanVien(phanHoi.getNhanVien().getId());
        return response;
    }

    private DanhGiaSanPham findDanhGia(Integer id) {
        return danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Đánh giá với ID " + id + " không tồn tại"));
    }

    private NhanVien findNhanVien(Integer id) {
        return nhanVienRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nhân viên với ID " + id + " không tồn tại"));
    }

    private PhanHoiDanhGia findPhanHoi(Integer id) {
        return phanHoiDanhGiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Phản hồi với ID " + id + " không tồn tại"));
    }
}
