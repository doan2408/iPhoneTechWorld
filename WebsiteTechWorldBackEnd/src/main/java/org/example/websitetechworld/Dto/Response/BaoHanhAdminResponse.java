package org.example.websitetechworld.Dto.Response;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BaoHanhAdminResponse {

        private Integer id;

        private Integer idKhachHang;

        private Integer idSanPhamChiTiet; // Thay vì sử dụng `SanPhamChiTiet`, bạn có thể chỉ lấy ID

        private LocalDate ngayBatDau;

        private LocalDate ngayKetThuc;

        private Integer idLoaiBaoHanh; // Thay vì `LoaiBaoHanh`, bạn có thể chỉ lấy ID

        private String trangThaiBaoHanh; // Sử dụng String hoặc một Enum nếu cần

    }
