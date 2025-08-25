package org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderClientResponse {
    private Integer idHoaDon;
    private String maHoaDon;
    private String maVanDon;
    private String trangThaiGiaoHang;
    private String trangThaiThanhToan;
    private BigDecimal thanhTien;
    private LocalDateTime ngayDatHang;
    private LocalDate ngayNhanHang;
    private List<MyOrderProductClientResponse> myOrderClientResponseList;

    public MyOrderClientResponse (Integer idHoaDon, String maVanDon, String trangThaiGiaoHang, String trangThaiThanhToan, BigDecimal thanhTien, LocalDateTime ngayDatHang, List<MyOrderProductClientResponse> myOrderClientResponseList) {
        this.idHoaDon = idHoaDon;
        this.maVanDon = maVanDon;
        this.trangThaiGiaoHang = trangThaiGiaoHang;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.thanhTien = thanhTien;
        this.ngayDatHang = ngayDatHang;
        this.myOrderClientResponseList = myOrderClientResponseList;
    }
}
