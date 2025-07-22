package org.example.websitetechworld.Services.AdminServices.DanhGiaSanPhamAdminService;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Response.AdminResponse.DanhGiaSanPhamAdminResponse.DanhGiaSanPhamAdminResponse;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;
import org.example.websitetechworld.Repository.DanhGiaSanPhamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DanhGiaSanPhamAdminService {
    private final DanhGiaSanPhamRepository danhGiaSanPhamRepository;

    public Page<DanhGiaSanPhamAdminResponse> layTatCaDanhGiaAdmin(Pageable pageable) {
        Page<Object[]> pageResult = danhGiaSanPhamRepository.findAllDanhGia(pageable);

        List<DanhGiaSanPhamAdminResponse> danhGiaList = pageResult.getContent().stream().map(row -> {
            Integer idDanhGia = ((Number) row[0]).intValue(); // idDanhGia

            DanhGiaSanPhamAdminResponse dto = new DanhGiaSanPhamAdminResponse();
            dto.setIdDanhGia(idDanhGia);
            dto.setTenKhachHang((String) row[1]);              // tenKhachHang
            dto.setMaSanPhamChiTiet((String) row[2]);          // maSanPhamChiTiet
            dto.setTenSanPham((String) row[3]);                // tenSanPham
            dto.setSoSao((Integer) row[4]);                    // soSao
            dto.setNoiDung((String) row[5]);                   // noiDung
            dto.setNgayDanhGia(((Timestamp) row[6]).toLocalDateTime()); // ngayDanhGia
            dto.setTrangThaiDanhGia(TrangThaiDanhGia.valueOf((String) row[7])); // trangThaiDanhGia

            // Lấy danh sách ảnh & video tương ứng
            dto.setAnhUrls(danhGiaSanPhamRepository.findAnhByDanhGiaId(idDanhGia));
            dto.setVideoUrls(danhGiaSanPhamRepository.findVideoByDanhGiaId(idDanhGia));

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(danhGiaList, pageable, pageResult.getTotalElements());
    }
}
