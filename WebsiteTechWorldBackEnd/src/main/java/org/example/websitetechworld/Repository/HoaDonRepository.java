package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.GiaoHang.ShippingMethod;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    Page<HoaDon> findByIsDeleteFalseOrIsDeleteIsNull(Pageable pageable);
    Integer countByTrangThaiThanhToan(TrangThaiThanhToan trangThaiThanhToan);

    @Query (value = """
                    SELECT
                        SUM(thanh_tien) AS tong_doanh_thu
                    FROM
                        hoa_don
                    WHERE
                        is_delete = 0
                        AND trang_thai_thanh_toan = 'COMPLETED'
                        AND ngay_thanh_toan IS NOT NULL
                    """, nativeQuery = true)
    BigDecimal doanhThuThang();

    @Transactional
    @Modifying
    @Query("UPDATE HoaDon hd SET hd.tenNguoiNhan = :tenNguoiNhan, hd.sdtNguoiNhan = :sdtNguoiNhan, " +
            "hd.diaChiGiaoHang = :diaChiGiaoHang, hd.phiShip = :phiShip, hd.isShipping = :isShipping, " +
            "hd.maVanDon = :maVanDon, hd.thanhTien = :thanhTien, hd.shippingMethod = :shippingMethod, " +
            "hd.trangThaiDonHang = :trangThaiDonHang, hd.ngayDatHang = :ngayDatHang " +
            "WHERE hd.id = :id")
    void updateInvoice(@Param("id") Integer id,
                       @Param("tenNguoiNhan") String tenNguoiNhan,
                       @Param("sdtNguoiNhan") String sdtNguoiNhan,
                       @Param("diaChiGiaoHang") String diaChiGiaoHang,
                       @Param("phiShip") BigDecimal phiShip,
                       @Param("isShipping") Boolean isShipping,
                       @Param("maVanDon") String maVanDon,
                       @Param("thanhTien") BigDecimal thanhTien,
                       @Param("shippingMethod") ShippingMethod shippingMethod,
                       @Param("trangThaiDonHang") TrangThaiGiaoHang trangThaiDonHang,
                       @Param("ngayDatHang") LocalDate ngayDatHang);

    public List<HoaDon> findByIdKhachHang_Id(Integer userLoginId);


    @Query("SELECT hd.id FROM HoaDon hd WHERE hd.maVanDon = :maVanDon ")
    public List<Integer> findIdHoaDonByMVD(@Param("maVanDon") String maVanDon);
}
