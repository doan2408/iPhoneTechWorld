package org.example.websitetechworld.Services.AdminServices.DanhGiaSanPhamAdminService;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Response.AdminResponse.DanhGiaSanPhamAdminResponse.DanhGiaSanPhamAdminResponse;
import org.example.websitetechworld.Entity.DanhGiaSanPham;
import org.example.websitetechworld.Entity.PhanHoiDanhGia;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;
import org.example.websitetechworld.Repository.DanhGiaSanPhamRepository;
import org.example.websitetechworld.Repository.PhanHoiDanhGiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DanhGiaSanPhamAdminService {
    private final DanhGiaSanPhamRepository danhGiaSanPhamRepository;
    private final PhanHoiDanhGiaRepository phanHoiDanhGiaRepository;

    public Page<DanhGiaSanPhamAdminResponse> layTatCaDanhGiaAdmin(Pageable pageable) {
        Page<Object[]> pageResult = danhGiaSanPhamRepository.findAllDanhGia(pageable);

        List<DanhGiaSanPhamAdminResponse> danhGiaList = pageResult.getContent().stream().map(row -> {
            DanhGiaSanPhamAdminResponse dto = new DanhGiaSanPhamAdminResponse();
            dto.setIdDanhGia(((Number) row[0]).intValue());
            dto.setTenKhachHang((String) row[1]);
            dto.setMaSanPhamChiTiet((String) row[2]);
            dto.setTenSanPham((String) row[3]);
            dto.setTenMau((String) row[4]);
            dto.setDungLuongRom((String) row[5]);
            dto.setDungLuongRam((String) row[6]);
            dto.setSoSao(row[7] != null ? ((Number) row[7]).intValue() : null);
            dto.setNoiDung((String) row[8]);
            dto.setAnhUrls(row[9] != null ? Arrays.asList(((String) row[9]).split(",\\s*")) : Collections.emptyList());
            dto.setVideoUrls(row[10] != null ? Arrays.asList(((String) row[10]).split(",\\s*")) : Collections.emptyList());
            dto.setNoiDungPhanHoi(row[11] != null ? (String) row[11] : "");
            dto.setNgayDanhGia(row[12] != null ? ((Timestamp) row[12]).toLocalDateTime() : null);
            dto.setTrangThaiDanhGia(row[13] != null ? TrangThaiDanhGia.valueOf((String) row[13]) : null);

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(danhGiaList, pageable, pageResult.getTotalElements());
    }

    @Transactional
    public void pheDuyetDanhGia(Integer id) {
        Optional<DanhGiaSanPham> optional = danhGiaSanPhamRepository.findById(id);
        if (optional.isPresent()) {
            DanhGiaSanPham danhGia = optional.get();
            danhGia.setTrangThaiDanhGia(TrangThaiDanhGia.APPROVED);
            danhGiaSanPhamRepository.save(danhGia);
        } else {
            throw new RuntimeException("Không tìm thấy đánh giá với ID: " + id);
        }
    }

    // Từ chối đánh giá
    @Transactional
    public void tuChoiDanhGia(Integer id) {
        Optional<DanhGiaSanPham> optional = danhGiaSanPhamRepository.findById(id);
        if (optional.isPresent()) {
            DanhGiaSanPham danhGia = optional.get();
            danhGia.setTrangThaiDanhGia(TrangThaiDanhGia.REFUSE);
            danhGiaSanPhamRepository.save(danhGia);
        } else {
            throw new RuntimeException("Không tìm thấy đánh giá với ID: " + id);
        }
    }

    // Xóa đánh giá
    @Transactional
    public void xoaDanhGia(Integer id) {
        if (danhGiaSanPhamRepository.existsById(id)) {
            danhGiaSanPhamRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy đánh giá với ID: " + id);
        }
    }

    // Phản hồi đánh giá
    @Transactional
    public void phanHoiDanhGia(Integer id, Map<String, String> request) {
        Optional<DanhGiaSanPham> optional = danhGiaSanPhamRepository.findById(id);
        if (optional.isPresent()) {
            DanhGiaSanPham danhGia = optional.get();
            PhanHoiDanhGia phanHoi = new PhanHoiDanhGia();
            phanHoi.setDanhGiaSanPham(danhGia);
            phanHoi.setNoiDung(request.get("noiDungPhanHoi"));
            phanHoi.setNgayPhanHoi(LocalDateTime.now());
            phanHoiDanhGiaRepository.save(phanHoi);
        } else {
            throw new RuntimeException("Không tìm thấy đánh giá với ID: " + id);
        }
    }

}
