package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ThongKeRepository extends JpaRepository<HoaDon, Integer> {

    // Tổng doanh thu theo khoảng thời gian và trạng thái
    @Query(value = """
    SELECT SUM(tong_tien) 
    FROM hoa_don 
    WHERE ngay_thanh_toan BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    BigDecimal doanhThuTheoKhoang(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate

    );


    // Số đơn hàng theo khoảng thời gian
    @Query(value = """
    SELECT COUNT(*) 
    FROM hoa_don 
    WHERE ngay_thanh_toan BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    Integer soDonHangTheoKhoang(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate

    );


    @Query(value = """
    SELECT SUM(cthd.so_luong)
    FROM chi_tiet_hoa_don cthd
    JOIN hoa_don hd ON cthd.id_hoa_don = hd.id_hoa_don
    WHERE hd.ngay_thanh_toan BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    Integer soSanPhamTheoKhoang(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate

    );


    @Query(value = "SELECT COUNT(*) FROM khach_hang", nativeQuery = true)
    Integer tongSoKhachHang();


    @Query(value = """
    SELECT TOP 3 
        sp.ten_san_pham, 
        SUM(cthd.so_luong) AS tong_so_luong_ban
    FROM chi_tiet_hoa_don cthd
    JOIN hoa_don hd ON cthd.id_hoa_don = hd.id_hoa_don
    JOIN san_pham_chi_tiet spct ON cthd.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
    JOIN san_pham sp ON spct.id_san_pham = sp.id_san_pham
    WHERE hd.trang_thai_thanh_toan = 'COMPLETED'
    GROUP BY sp.ma_san_pham, sp.ten_san_pham
    ORDER BY tong_so_luong_ban DESC
    """, nativeQuery = true)
    List<Map<String, Object>> sanPhamBanChay();



    @Query(value = """
    SELECT TOP 3 
        sp.ten_san_pham, 
        SUM(cthd.so_luong) AS tong_so_luong_ban,
        SUM(cthd.so_luong * spct.gia_ban) AS tong_doanh_thu
    FROM chi_tiet_hoa_don cthd
    JOIN hoa_don hd ON cthd.id_hoa_don = hd.id_hoa_don
    JOIN san_pham_chi_tiet spct ON cthd.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
    JOIN san_pham sp ON spct.id_san_pham = sp.id_san_pham
    
      AND hd.ngay_tao_hoa_don BETWEEN :startDate AND :endDate
    GROUP BY sp.ma_san_pham, sp.ten_san_pham
    ORDER BY tong_so_luong_ban DESC
    """, nativeQuery = true)
    List<Map<String, Object>> sanPhamBanChayTheoNgay(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    @Query(value = """
            SELECT TOP 5 
                hd.ma_hoa_don, 
                kh.ten_khach_hang, 
                hd.ngay_tao_hoa_don, 
                hd.tong_tien, 
                hd.trang_thai_thanh_toan
            FROM hoa_don hd
            JOIN khach_hang kh ON hd.id_khach_hang = kh.id_khach_hang
            ORDER BY hd.ngay_tao_hoa_don DESC
            """, nativeQuery = true)
    List<Map<String, Object>> donHangMoiNhat();


    @Query(value = """
                WITH Ngay AS (
                    SELECT DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS start_date
                    UNION ALL
                    SELECT DATEADD(DAY, 1, start_date)
                    FROM Ngay
                    WHERE start_date < EOMONTH(GETDATE())
                )
                SELECT 
                    start_date,
                    ISNULL(SUM(hd.tong_tien), 0) AS tong_doanh_thu
                FROM Ngay
                LEFT JOIN hoa_don hd ON CAST(hd.ngay_tao_hoa_don AS DATE) = start_date
                GROUP BY Ngay.start_date
                ORDER BY Ngay.start_date
                OPTION (MAXRECURSION 100)
            """, nativeQuery = true)
    List<Map<String, Object>> thongKeDoanhThuTheoNgay();

    @Query(value = """
    WITH Thang AS (
        SELECT CAST(DATEFROMPARTS(YEAR(GETDATE()), 1, 1) AS DATE) AS dau_thang
        UNION ALL
        SELECT DATEADD(MONTH, 1, dau_thang)
        FROM Thang
        WHERE MONTH(dau_thang) < MONTH(GETDATE())
    )
    SELECT 
        FORMAT(dau_thang, 'yyyy-MM') AS thang,
        ISNULL(SUM(hd.tong_tien), 0) AS tong_doanh_thu
    FROM Thang
    LEFT JOIN hoa_don hd 
        ON YEAR(hd.ngay_tao_hoa_don) = YEAR(GETDATE())
       AND MONTH(hd.ngay_tao_hoa_don) = MONTH(dau_thang)
      AND hd.trang_thai_thanh_toan = 'COMPLETED'
    GROUP BY FORMAT(dau_thang, 'yyyy-MM')
    ORDER BY thang
    OPTION (MAXRECURSION 100)
    """, nativeQuery = true)
    List<Map<String, Object>> thongKeDoanhThuTheoThang();

    @Query(value = """
    WITH Thang AS (
        SELECT CAST(DATEFROMPARTS(YEAR(GETDATE()), 1, 1) AS DATE) AS dau_thang
        UNION ALL
        SELECT DATEADD(MONTH, 1, dau_thang)
        FROM Thang
        WHERE MONTH(dau_thang) < MONTH(GETDATE())
    )
    SELECT 
        FORMAT(dau_thang, 'yyyy-MM') AS thang,
        ISNULL(COUNT(hd.id_hoa_don), 0) AS so_don_bi_huy
    FROM Thang
    LEFT JOIN hoa_don hd 
        ON YEAR(hd.ngay_tao_hoa_don) = YEAR(GETDATE())
       AND MONTH(hd.ngay_tao_hoa_don) = MONTH(dau_thang)
       AND hd.trang_thai_don_hang = 'RETURNS'
    GROUP BY FORMAT(dau_thang, 'yyyy-MM')
    ORDER BY thang
    OPTION (MAXRECURSION 100)
    """, nativeQuery = true)
    List<Map<String, Object>> thongKeDonHuyTheoThang();

    @Query(value = """
                SELECT 
                    kh.ma_khach_hang,
                    kh.ten_khach_hang,
                    COUNT(hd.ma_hoa_don) AS so_lan_mua,
                    SUM(hd.tong_tien) AS tong_doanh_thu
                FROM hoa_don hd
                JOIN khach_hang kh ON hd.id_khach_hang = kh.id_khach_hang
                GROUP BY kh.ma_khach_hang, kh.ten_khach_hang
                ORDER BY tong_doanh_thu DESC
            """, nativeQuery = true)
    List<Map<String, Object>> thongKeTheoKhachHang();


    @Query(value = """
            SELECT TOP 5
                kh.ma_khach_hang,
                kh.ten_khach_hang,
                SUM(hd.tong_tien) AS tong_doanh_thu
            FROM hoa_don hd
            JOIN khach_hang kh ON hd.id_khach_hang = kh.id_khach_hang
            GROUP BY kh.ma_khach_hang, kh.ten_khach_hang
            ORDER BY tong_doanh_thu DESC
            """, nativeQuery = true)
    List<Map<String, Object>> topKhachHangMuaNhieu();

    @Query(value = """
            SELECT TOP 5
                kh.ma_khach_hang,
                kh.ten_khach_hang,
                COUNT(hd.ma_hoa_don) AS so_lan_mua,
                SUM(hd.tong_tien) AS tong_doanh_thu
            FROM hoa_don hd
            JOIN khach_hang kh ON hd.id_khach_hang = kh.id_khach_hang
            GROUP BY kh.ma_khach_hang, kh.ten_khach_hang
            ORDER BY so_lan_mua DESC
            """, nativeQuery = true)
    List<Map<String, Object>> khachHangTrungThanh();

    @Query(value = """
            SELECT 
                ma_khach_hang,
                ten_khach_hang,
                tong_diem
            FROM khach_hang
            WHERE tong_diem >= 1000
            """, nativeQuery = true)
    List<Map<String, Object>> khachHangHangCao();



    @Query(value = """
    SELECT COUNT(DISTINCT sp.id_san_pham)
    FROM chi_tiet_hoa_don cthd
    JOIN san_pham_chi_tiet spct ON cthd.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
    JOIN san_pham sp ON spct.id_san_pham = sp.id_san_pham
    JOIN hoa_don hd ON cthd.id_hoa_don = hd.id_hoa_don
    WHERE hd.ngay_tao_hoa_don >= ?1 AND hd.ngay_tao_hoa_don < ?2
""", nativeQuery = true)
    long countSanPhamBanChay(Date startDate, Date endDate);


    @Query(value = """
            SELECT TOP 5
                ma_san_pham,
                ten_san_pham,
                so_luong_ton_kho
            FROM san_pham
            WHERE so_luong_ton_kho >= 40
            ORDER BY so_luong_ton_kho DESC
            """, nativeQuery = true)
    List<Map<String, Object>> sanPhamTonKhoNhieu();

    @Query(value = """
            SELECT TOP 5
                                     sp.ma_san_pham,
                                     sp.ten_san_pham,
                                     SUM(spct.so_luong) AS tong_so_luong
                                 FROM san_pham sp
                                 JOIN san_pham_chi_tiet spct ON sp.id_san_pham = spct.id_san_pham
                                 GROUP BY sp.ma_san_pham, sp.ten_san_pham
                                 HAVING SUM(spct.so_luong) <= 10
                                 ORDER BY tong_so_luong ASC;
            """, nativeQuery = true)
    List<Map<String, Object>> sanPhamSapHetHang();


    @Query(value = "SELECT COUNT(*) FROM hoa_don", nativeQuery = true)
    Integer tongSoDonHang();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'PENDING'", nativeQuery = true)
    Integer donHangChuaXuLy();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'PAID'", nativeQuery = true)
    Integer donHangDaThanhToan();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'CANCELLED'", nativeQuery = true)
    Integer donHangBiHuy();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'REFUNDED'", nativeQuery = true)
    Integer donHangDaHoanTien();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'COMPLETED'", nativeQuery = true)
    Integer donHangDaHoanTat();
}

