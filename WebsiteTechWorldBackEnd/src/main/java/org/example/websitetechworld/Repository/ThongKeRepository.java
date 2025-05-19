package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface ThongKeRepository extends JpaRepository<HoaDon, Integer> {

    @Query(value = """
        SELECT SUM(tong_tien) 
        FROM hoa_don 
        WHERE MONTH(ngay_tao) = MONTH(GETDATE())
          AND YEAR(ngay_tao) = YEAR(GETDATE())
        """, nativeQuery = true)
    BigDecimal doanhThuThang ();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE MONTH(ngay_tao) = MONTH(GETDATE()) AND YEAR(ngay_tao) = YEAR(GETDATE())", nativeQuery = true)
    Integer dashboardSoDonHang ();

    @Query(value = "SELECT COUNT(*) FROM san_pham WHERE MONTH(ngay_tao) = MONTH(GETDATE()) AND YEAR(ngay_tao) = YEAR(GETDATE())", nativeQuery = true)
    Integer dashboardSoSanPham ();

    @Query(value = "SELECT COUNT(*) FROM khach_hang WHERE MONTH(ngay_tao) = MONTH(GETDATE()) AND YEAR(ngay_tao) = YEAR(GETDATE())", nativeQuery = true)
    Integer dashboardSoKhachHang ();

    @Query(value = """
        SELECT TOP 5 
            sp.ma_san_pham, 
            sp.ten_san_pham, 
            SUM(cthd.so_luong) AS tong_so_luong_ban
        FROM chi_tiet_hoa_don cthd
        JOIN san_pham_chi_tiet spct ON cthd.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
        JOIN san_pham sp ON spct.id_san_pham = sp.id_san_pham
        GROUP BY sp.ma_san_pham, sp.ten_san_pham
        ORDER BY tong_so_luong_ban DESC
        """, nativeQuery = true)
    List<Map<String, Object>> sanPhamBanChay ();

    @Query(value = """
        SELECT TOP 5 
            hd.ma_hoa_don, 
            kh.ten_khach_hang, 
            hd.ngay_tao, 
            hd.tong_tien, 
            hd.trang_thai_thanh_toan
        FROM hoa_don hd
        JOIN khach_hang kh ON hd.id_khach_hang = kh.id_khach_hang
        ORDER BY hd.ngay_tao DESC
        """, nativeQuery = true)
    List<Map<String, Object>> donHangMoiNhat ();


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
        LEFT JOIN hoa_don hd ON CAST(hd.ngay_tao AS DATE) = start_date
        GROUP BY Ngay.start_date
        ORDER BY Ngay.start_date
        OPTION (MAXRECURSION 100)
    """, nativeQuery = true)
    List<Map<String, Object>> thongKeDoanhThuTheoNgay ();

    @Query(value = """
        WITH Thang AS (
            SELECT CAST(DATEFROMPARTS(YEAR(GETDATE()), 1, 1) AS DATE) AS dau_thang
            UNION ALL
            SELECT DATEADD(MONTH, 1, dau_thang)
            FROM Thang
            WHERE MONTH(dau_thang) < 12
        )
        SELECT 
            FORMAT(dau_thang, 'yyyy-MM') AS thang,
            ISNULL(SUM(hd.tong_tien), 0) AS tong_doanh_thu
        FROM Thang
        LEFT JOIN hoa_don hd 
            ON YEAR(hd.ngay_tao) = YEAR(GETDATE())
           AND MONTH(hd.ngay_tao) = MONTH(dau_thang)
        GROUP BY FORMAT(dau_thang, 'yyyy-MM')
        ORDER BY thang
        OPTION (MAXRECURSION 100)
    """, nativeQuery = true)
    List<Map<String, Object>> thongKeDoanhThuTheoThang ();

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
    List<Map<String, Object>> thongKeTheoKhachHang ();


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
    List<Map<String, Object>> topKhachHangMuaNhieu ();

    @Query(value = """
        SELECT TOP 5
            kh.ma_khach_hang,
            kh.ten_khach_hang,
            COUNT(hd.ma_hoa_don) AS so_lan_mua
        FROM hoa_don hd
        JOIN khach_hang kh ON hd.id_khach_hang = kh.id_khach_hang
        GROUP BY kh.ma_khach_hang, kh.ten_khach_hang
        ORDER BY so_lan_mua DESC
        """, nativeQuery = true)
    List<Map<String, Object>> khachHangTrungThanh ();

    @Query(value = """
        SELECT 
            ma_khach_hang,
            ten_khach_hang,
            tong_diem
        FROM khach_hang
        WHERE tong_diem >= 1000
        """, nativeQuery = true)
    List<Map<String, Object>> khachHangHangCao ();


    @Query(value = """
        SELECT TOP 5
            sp.ma_san_pham,
            sp.ten_san_pham,
            SUM(cthd.so_luong) AS tong_so_luong_ban
        FROM chi_tiet_hoa_don cthd
        JOIN san_pham_chi_tiet spct ON cthd.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
        JOIN san_pham sp ON spct.id_san_pham = sp.id_san_pham
        GROUP BY sp.ma_san_pham, sp.ten_san_pham
        ORDER BY tong_so_luong_ban DESC
        """, nativeQuery = true)
    List<Map<String, Object>> topSanPhamBanChay ();

    @Query(value = """
        SELECT TOP 5
            ma_san_pham,
            ten_san_pham,
            so_luong_ton_kho
        FROM san_pham
        WHERE so_luong_ton_kho >= 40
        ORDER BY so_luong_ton_kho DESC
        """, nativeQuery = true)
    List<Map<String, Object>> sanPhamTonKhoNhieu ();

    @Query(value = """
        SELECT TOP 5
            ma_san_pham,
            ten_san_pham,
            so_luong_ton_kho
        FROM san_pham
        WHERE so_luong_ton_kho <= 20
        ORDER BY so_luong_ton_kho ASC
        """, nativeQuery = true)
    List<Map<String, Object>> sanPhamSapHetHang ();


    @Query(value = "SELECT COUNT(*) FROM hoa_don", nativeQuery = true)
    Integer tongSoDonHang ();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'PENDING'", nativeQuery = true)
    Integer donHangChuaXuLy();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'PAID'", nativeQuery = true)
    Integer donHangDaThanhToan ();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'CANCELLED'", nativeQuery = true)
    Integer donHangBiHuy ();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'REFUNDED'", nativeQuery = true)
    Integer donHangDaHoanTien ();

    @Query(value = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = 'COMPLETED'", nativeQuery = true)
    Integer donHangDaHoanTat ();
}

