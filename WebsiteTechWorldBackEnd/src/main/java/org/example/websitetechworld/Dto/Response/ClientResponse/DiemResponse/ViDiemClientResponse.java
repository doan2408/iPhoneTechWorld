package org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViDiemClientResponse {
    private Integer idViDiem;
    private Integer idKhachHang;
    private BigDecimal diemKhaDung;
    private BigDecimal diemSapHetHan;
}
