package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.ClientProductResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.ThongSoResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query(value = """
    SELECT
        sp.id_san_pham,
        sp.ma_san_pham,
        sp.ten_san_pham,
        sp.trang_thai,
        l.ten_loai,
        COALESCE(sl.tong_so_luong, 0) AS so_luong,
        ha_min.url AS url
    FROM san_pham AS sp
    JOIN model_san_pham AS msp ON msp.id_model_san_pham = sp.id_model_san_pham
    JOIN loai AS l ON l.id_loai = msp.id_loai
    LEFT JOIN (
        SELECT id_san_pham, SUM(so_luong) AS tong_so_luong
        FROM san_pham_chi_tiet
        GROUP BY id_san_pham
    ) AS sl ON sl.id_san_pham = sp.id_san_pham
    LEFT JOIN (
        SELECT spct.id_san_pham, MIN(ha.url) AS url
        FROM san_pham_chi_tiet AS spct
        JOIN hinh_anh AS ha ON ha.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
        GROUP BY spct.id_san_pham
    ) AS ha_min ON ha_min.id_san_pham = sp.id_san_pham
    WHERE 
        (:keyword IS NULL OR :keyword = '' OR 
         LOWER(sp.ma_san_pham) LIKE LOWER(CONCAT(:keyword, '%')) OR 
         LOWER(sp.ten_san_pham) LIKE LOWER(CONCAT(:keyword, '%')))
      AND (:idLoai IS NULL OR l.id_loai = :idLoai)
      AND (:trangThai IS NULL OR :trangThai = '' OR sp.trang_thai = :trangThai)
    ORDER BY sp.id_san_pham DESC
    """,
            countQuery = """
    SELECT COUNT(DISTINCT sp.id_san_pham)
    FROM san_pham AS sp
    JOIN model_san_pham AS msp ON msp.id_model_san_pham = sp.id_model_san_pham
    JOIN loai AS l ON l.id_loai = l.id_loai
    WHERE 
        (:keyword IS NULL OR :keyword = '' OR 
         LOWER(sp.ma_san_pham) LIKE LOWER(CONCAT(:keyword, '%')) OR 
         LOWER(sp.ten_san_pham) LIKE LOWER(CONCAT(:keyword, '%')))
      AND (:idLoai IS NULL OR l.id_loai = :idLoai)
      AND (:trangThai IS NULL OR :trangThai = '' OR sp.trang_thai = :trangThai)
    """,
            nativeQuery = true)
    Page<Object[]> getAllHienThi(
            @Param("keyword") String keyword,
            @Param("idLoai") Integer idLoai,
            @Param("trangThai") String trangThai,
            Pageable pageable);

    @Query(value = """
        SELECT DISTINCT
            LEFT(ten_san_pham, 
                CHARINDEX(' ', ten_san_pham + ' ', CHARINDEX(' ', ten_san_pham + ' ') + 1) - 1
            ) AS ten_dong_san_pham
        FROM san_pham
        """,
            countQuery = """
        SELECT COUNT(*) FROM (
            SELECT DISTINCT
                LEFT(ten_san_pham, 
                    CHARINDEX(' ', ten_san_pham + ' ', CHARINDEX(' ', ten_san_pham + ' ') + 1) - 1
                ) AS ten_dong_san_pham
            FROM san_pham
        ) AS temp
        """,
            nativeQuery = true)
    Page<String> findTenDongSanPham(Pageable pageable);

    Boolean existsByTenSanPham (String tenSanPham);

    //select all sản phẩm lên trang chủ client
    @Query("""
    SELECT new org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.ClientProductResponse(
        sp.id,
        (
            SELECT ha.url
            FROM HinhAnh ha
            JOIN ha.idSanPhamChiTiet spct_ha
            WHERE spct_ha.idSanPham.id = sp.id
            AND ha.id = (
                SELECT MIN(ha2.id)
                FROM HinhAnh ha2
                JOIN ha2.idSanPhamChiTiet spct_ha2
                WHERE spct_ha2.idSanPham.id = sp.id
            )
        ),
        sp.tenSanPham,
        (
            SELECT MIN(spct2.giaBan)
            FROM SanPhamChiTiet spct2
            WHERE spct2.idSanPham.id = sp.id
        ),
        l.tenLoai,
        xx.maXuatXu
    )
    FROM SanPham sp
    JOIN sp.idModelSanPham.idLoai l
    JOIN sp.idModelSanPham.idXuatXu xx
    WHERE sp.trangThaiSanPham = 'ACTIVE'
      AND (:idLoai IS NULL OR l.id = :idLoai)
      AND (:tenSanPham IS NULL OR LOWER(sp.tenSanPham) LIKE LOWER(CONCAT('%', :tenSanPham, '%')))
      AND (
                  :giaMin IS NULL OR
                  (SELECT MIN(spct2.giaBan) FROM SanPhamChiTiet spct2 WHERE spct2.idSanPham.id = sp.id) >= :giaMin
            )
      AND (
                  :giaMax IS NULL OR
                  (SELECT MIN(spct2.giaBan) FROM SanPhamChiTiet spct2 WHERE spct2.idSanPham.id = sp.id) <= :giaMax
            )
    ORDER BY
        CASE WHEN :sort = 'giaAsc' THEN
            (SELECT MIN(spct2.giaBan) FROM SanPhamChiTiet spct2 WHERE spct2.idSanPham.id = sp.id)
            END ASC,
        CASE WHEN :sort = 'giaDesc' THEN
            (SELECT MIN(spct2.giaBan) FROM SanPhamChiTiet spct2 WHERE spct2.idSanPham.id = sp.id)
            END DESC
    """)
    Page<ClientProductResponse> getSanPhamHome(
            @Param("tenSanPham") String tenSanPham,
            @Param("idLoai") Integer loai,
            @Param("giaMin") BigDecimal giaMin,
            @Param("giaMax") BigDecimal giaMax,
            @Param("sort") String sort,
            Pageable pageable
    );

    //get list anh cua ctsp
    @Query(value = """
    select top 1 hinh_anh.url from hinh_anh
    join san_pham_chi_tiet spct on spct.id_san_pham_chi_tiet = hinh_anh.id_san_pham_chi_tiet
    where spct.id_san_pham = :idSp
    order by spct.id_san_pham_chi_tiet asc
    """, nativeQuery = true)
    List<String> getFirstAnh(@Param("idSp") Integer idSp);

    //get list anh cua ctsp
    @Query(value = """
    select hinh_anh.url from hinh_anh
    join san_pham_chi_tiet spct on spct.id_san_pham_chi_tiet = hinh_anh.id_san_pham_chi_tiet
    where spct.id_san_pham_chi_tiet = :idSpct
    """, nativeQuery = true)
    List<String> getListHinhAnh(@Param("idSpct") Integer idSpct);

    //get gia ban thap nhat
    @Query(value = """
    select min(spct.gia_ban) from san_pham_chi_tiet spct
          where id_san_pham = :idSanPham 
    """, nativeQuery = true)
    BigDecimal giaThapNhat(@Param("idSanPham") Integer idSanPham);

    @Query(value = """
    select sum(spct.so_luong) from san_pham_chi_tiet spct
    where spct.id_san_pham = ?1
    """, nativeQuery = true)
    Integer tongSoLuong(int idSanPham);

    //get thong so theo rom
    @Query(value = """
        select top 1
                c.chip_xu_ly, r.dung_luong_ram, rom.dung_luong_rom,
            	mh.ten_man_hinh, mh.kich_thuoc, mh.loai_man_hinh, mh.do_phan_giai, mh.tan_so_quet, mh.do_sang, mh.chat_lieu_kinh,
            	cms.do_phan_giai, cmt.do_phan_giai,
            	p.phien_ban, p.cong_suat_sac, p.thoi_gian_su_dung, p.so_lan_sac_toi_da,
            	hdh.phien_ban, x.ten_quoc_gia
            	from model_san_pham mdsp
                join ram r on r.id_ram = mdsp.id_ram
            	join cpu c on c.id_cpu = mdsp.id_cpu
            	join man_hinh mh on mh.id_man_hinh = mdsp.id_man_hinh
            	join model_camera_sau mdcms on mdcms.id_model_san_pham = mdsp.id_model_san_pham  and mdcms.is_chinh = 1
        		join camera_sau cms on cms.id_camera_sau = mdcms.id_camera_sau
            	join camera_truoc cmt on cmt.id_camera_truoc = mdsp.id_camera_truoc
            	join pin p on p.id_pin = mdsp.id_pin
            	join he_dieu_hanh hdh on hdh.id_he_dieu_hanh = mdsp.id_he_dieu_hanh
            	join xuat_xu x on x.id_xuat_xu = mdsp.id_xuat_xu
            	join san_pham sp on sp.id_model_san_pham = mdsp.id_model_san_pham
            	join san_pham_chi_tiet spct on spct.id_san_pham = sp.id_san_pham
            	join rom on spct.id_rom = rom.id_rom
    	WHERE sp.id_san_pham = :idSanPham AND rom.id_rom = :idRom
    """, nativeQuery = true)
    ThongSoResponse getThongSoByIdSpct(@Param("idSanPham") Integer idSanPham, @Param("idRom") Integer idRom);

    //get object spct
    @Query("select spct from SanPhamChiTiet spct " +
            "where spct.idSanPham.id = :idSanPham and spct.idMau.id = :idMau and spct.idRom.id = :idRom")
    Optional<SanPhamChiTiet> getSpctByMauAndRom(@Param("idSanPham") Integer idSanPham, @Param("idMau") Integer idMau, @Param("idRom") Integer idRom);

    //get anh theo mau
    @Query(value = """
        select distinct ha.url from hinh_anh ha
    	join san_pham_chi_tiet spct on ha.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
    	where spct.id_san_pham = ?1 and spct.id_mau = ?2
    """, nativeQuery = true)
    List<String> getListAnhByMau(Integer idSanPham, Integer idMau);

    List<SanPham> findAllByIdModelSanPham_IdModelSanPham(Integer idModelSanPham);

    @Query(value = """
        select top 1
                c.chip_xu_ly, r.dung_luong_ram, rom.dung_luong_rom,
            	mh.ten_man_hinh, mh.kich_thuoc, mh.loai_man_hinh, mh.do_phan_giai, mh.tan_so_quet, mh.do_sang, mh.chat_lieu_kinh,
            	cms.do_phan_giai, cmt.do_phan_giai,
            	p.phien_ban, p.cong_suat_sac, p.thoi_gian_su_dung, p.so_lan_sac_toi_da,
            	hdh.phien_ban, x.ten_quoc_gia, loai.ten_loai,
            	c.so_nhan , c.xung_nhip, c.bo_nho_dem, c.tieu_thu_dien_nang, c.nam_ra_mat
            	from model_san_pham mdsp
                join ram r on r.id_ram = mdsp.id_ram
            	join cpu c on c.id_cpu = mdsp.id_cpu
            	join man_hinh mh on mh.id_man_hinh = mdsp.id_man_hinh
            	join model_camera_sau mdcms on mdcms.id_model_san_pham = mdsp.id_model_san_pham  and mdcms.is_chinh = 1
        		join camera_sau cms on cms.id_camera_sau = mdcms.id_camera_sau
            	join camera_truoc cmt on cmt.id_camera_truoc = mdsp.id_camera_truoc
            	join pin p on p.id_pin = mdsp.id_pin
            	join he_dieu_hanh hdh on hdh.id_he_dieu_hanh = mdsp.id_he_dieu_hanh
            	join xuat_xu x on x.id_xuat_xu = mdsp.id_xuat_xu
            	join san_pham sp on sp.id_model_san_pham = mdsp.id_model_san_pham
            	join san_pham_chi_tiet spct on spct.id_san_pham = sp.id_san_pham
            	join rom on spct.id_rom = rom.id_rom
            	join loai loai on mdsp.id_loai = loai.id_loai
    	WHERE sp.id_san_pham = :idSanPham
        ORDER BY rom.dung_luong_rom ASC
    """, nativeQuery = true)
    ThongSoResponse getThongSoByIdSp(@Param("idSanPham") Integer idSanPham);

}
