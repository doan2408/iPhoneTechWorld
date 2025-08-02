package org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.HangThanhVien;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HangClientResponse {
    private Integer id;
    private HangKhachHang tenHang;
    private Integer diemHienTai;
    private Integer diemTu;
    private Integer diemDen;
    private Integer diemConThieu;
}
