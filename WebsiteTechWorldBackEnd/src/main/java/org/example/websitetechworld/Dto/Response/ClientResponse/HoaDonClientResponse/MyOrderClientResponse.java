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
    private String maVanDon;
    private String trangThaiGiaoHang;
    private String trangThaiThanhToan;
    private BigDecimal thanhTien;
    private LocalDateTime ngayDatHang;
    private List<MyOrderProductClientResponse> myOrderClientResponseList;
}
