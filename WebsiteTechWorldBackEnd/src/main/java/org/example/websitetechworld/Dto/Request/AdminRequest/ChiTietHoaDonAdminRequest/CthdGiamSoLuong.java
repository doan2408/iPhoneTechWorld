package org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CthdGiamSoLuong {
    private List<String> imeisToReturn;
}
