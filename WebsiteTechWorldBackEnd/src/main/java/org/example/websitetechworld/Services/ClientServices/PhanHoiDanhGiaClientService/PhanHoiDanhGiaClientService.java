package org.example.websitetechworld.Services.ClientServices.PhanHoiDanhGiaClientService;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhanHoiDanhGiaAdminResponse.PhanHoiDanhGiaAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.PhanHoiDanhGiaClientResponse.PhanHoiDanhGiaClientResponse;
import org.example.websitetechworld.Entity.PhanHoiDanhGia;
import org.example.websitetechworld.Repository.PhanHoiDanhGiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhanHoiDanhGiaClientService {
    private final PhanHoiDanhGiaRepository phanHoiDanhGiaRepository;

    public List<PhanHoiDanhGiaClientResponse> getPhanHoiDanhTheoDanhGia(Integer idDanhGia) {
        // Lấy danh sách phản hồi từ repository
        List<PhanHoiDanhGia> phanHoiList = phanHoiDanhGiaRepository.findListByIdDanhGia(idDanhGia);

        // Chuyển đổi từ entity sang DTO
        return phanHoiList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PhanHoiDanhGiaClientResponse mapToResponse(PhanHoiDanhGia phanHoi) {
        PhanHoiDanhGiaClientResponse response = new PhanHoiDanhGiaClientResponse();
        response.setIdPhanHoi(phanHoi.getIdPhanHoi());
        response.setIdNhanVien(phanHoi.getNhanVien().getId());
        response.setDanhGiaSanPham(phanHoi.getDanhGiaSanPham().getIdDanhGia());
        response.setNoiDungPhanHoi(phanHoi.getNoiDungPhanHoi());
        response.setNgayPhanHoi(phanHoi.getNgayPhanHoi());
        return response;
    }
}
