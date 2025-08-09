package org.example.websitetechworld.Dto.Response.AdminResponse.KhuyenMaiAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhuyenMaiAdminResponse {

    private Integer id;
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private String moTa;
    private Integer phanTramGiam;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private DoiTuongApDung doiTuongApDung;
    private String trangThai;
    private List<Integer> idSanPhamChiTietList;
}
