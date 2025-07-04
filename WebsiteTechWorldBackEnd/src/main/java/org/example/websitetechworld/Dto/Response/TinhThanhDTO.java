package org.example.websitetechworld.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TinhThanhDTO {
    private Integer id;
    private String ten;
    private BigDecimal phiShip;
}
