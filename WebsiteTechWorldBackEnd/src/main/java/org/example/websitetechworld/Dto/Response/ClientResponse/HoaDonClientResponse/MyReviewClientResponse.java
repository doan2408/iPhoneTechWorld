package org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyReviewClientResponse {
    private Integer idHoaDon;
    private String maVanDon;
    private String trangThaiGiaoHang;
    private String trangThaiThanhToan;
    private BigDecimal thanhTien;
    private LocalDateTime ngayThanhToan;
    private Boolean daDanhGia;
    private Boolean coPhanHoi;
    private List<MyOrderProductClientResponse> myOrderClientResponseList;
}
