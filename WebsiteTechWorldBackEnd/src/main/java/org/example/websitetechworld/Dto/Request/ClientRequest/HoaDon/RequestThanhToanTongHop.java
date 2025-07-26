package org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon;

import lombok.Data;
import org.example.websitetechworld.Dto.Request.ClientRequest.SanPhamClientRequest.ChiTietSanPhamRequest;
import org.example.websitetechworld.Enum.GiaoHang.ShippingMethod;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RequestThanhToanTongHop {
    private TenHinhThuc hinhThucThanhToan;
    private BigDecimal soTienKhachDua;
    private BigDecimal thanhTien;
    private BigDecimal phiShip;
    private ShippingMethod shippingMethod;
    private String sdtNguoiNhan;
    private String tenNguoiNhan;
    private String diaChiGiaoHang;
    private List<ChiTietSanPhamRequest> sanPhamRequests;
}
