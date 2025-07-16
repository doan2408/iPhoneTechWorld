package org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderClientResponse {
    private Integer idHoaDon;
    private String maVanDon;
    private TrangThaiGiaoHang trangThaiGiaoHang;

}
