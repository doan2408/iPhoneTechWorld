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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhanHoiDanhGiaAdminService {

    private final PhanHoiDanhGiaRepository repository;

    private final NhanVienRepository nhanVienRepository;

    private final DanhGiaSanPhamRepository danhSachSanPhamRepository;


    public PhanHoiDanhGiaAdminResponse create(Integer idDanhGia ,PhanHoiDanhGiaAdminRequest dto) {
        DanhGiaSanPham danhGiaSanPham = danhSachSanPhamRepository.findById(idDanhGia)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại"));

        Optional<PhanHoiDanhGia> existing = repository.findByDanhGiaSanPham_IdDanhGia(idDanhGia);
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

        PhanHoiDanhGia saved = repository.save(phanHoi);

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
        PhanHoiDanhGia phanHoi = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phản hồi không tồn tại"));

        phanHoi.setNoiDungPhanHoi(dto.getNoiDungPhanHoi());
        NhanVien nhanVien = nhanVienRepository.findById(dto.getIdNhanVien())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        phanHoi.setNhanVien(nhanVien);

        return repository.save(phanHoi);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
