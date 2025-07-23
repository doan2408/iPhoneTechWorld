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
public class HangClientResponse {
    private Integer id;
    private String tenHang;
    private Integer diemTu;
    private Integer diemDen;
}
