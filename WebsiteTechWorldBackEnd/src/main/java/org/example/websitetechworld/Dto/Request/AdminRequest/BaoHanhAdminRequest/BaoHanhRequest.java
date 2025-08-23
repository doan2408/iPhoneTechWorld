package org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaoHanhRequest {
    private Integer idBaoHanh;

    @NotNull(message = "Khách hàng không được để trống")
    private Integer idKhachHang;

    @NotNull(message = "IMEI đã bán không được để trống")
    private Integer idImeiDaBan;

    @NotNull(message = "Loại bảo hành không được để trống")
    private Integer idLoaiBaoHanh;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @PastOrPresent(message = "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày hiện tại")
    private Date ngayBatDau;

//    @NotNull(message = "Ngày kết thúc không được để trống")
//    @Future(message = "Ngày kết thúc phải lớn hơn ngày hiện tại")
    private Date ngayKetThuc;

    @NotNull(message = "Trạng thái bảo hành không được để trống")
    private TrangThaiBaoHanh trangThaiBaoHanh;


    public static BaoHanh convertDto(BaoHanhRequest request) {
        BaoHanh baoHanh = new BaoHanh();

        baoHanh.setId(request.getIdBaoHanh());

        // Khách hàng
        if (request.getIdKhachHang() != null) {
            KhachHang kh = new KhachHang();
            kh.setId(request.getIdKhachHang());
            baoHanh.setIdKhachHang(kh);
        }

        // Imei
        if (request.getIdImeiDaBan() != null) {
            ImeiDaBan imeiDaBan = new ImeiDaBan();
            imeiDaBan.setId(request.getIdImeiDaBan());
            baoHanh.setIdImeiDaBan(imeiDaBan);
        }

        // Loại bảo hành
        if (request.getIdLoaiBaoHanh() != null) {
            LoaiBaoHanh loai = new LoaiBaoHanh();
            loai.setId(request.getIdLoaiBaoHanh());
            baoHanh.setIdLoaiBaoHanh(loai);
        }

        // Ngày tháng
        baoHanh.setNgayBatDau(request.getNgayBatDau());
        baoHanh.setNgayKetThuc(request.getNgayKetThuc());

        // Trạng thái
        if (request.getTrangThaiBaoHanh() != null) {
            baoHanh.setTrangThaiBaoHanh(request.getTrangThaiBaoHanh());
        }

        return baoHanh;
    }
}
