package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ModelSanPham;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPhamModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelSanPhamRepository extends JpaRepository<ModelSanPham, Integer> {
    @Query(value = """

    SELECT
        msp.id_model_san_pham AS idModelSanPham,
        msp.ma_model_san_pham AS maModelSanPham,
        msp.ten_model AS tenModel,
        msp.id_loai AS idLoai,
        msp.id_ram AS idRam,
        msp.id_xuat_xu AS idXuatXu
    FROM model_san_pham msp
        LEFT JOIN ram AS r ON r.id_ram = msp.id_ram
        LEFT JOIN xuat_xu xx ON xx.id_xuat_xu = msp.id_xuat_xu
        LEFT JOIN loai l ON l.id_loai = msp.id_loai
     ORDER BY msp.id_model_san_pham DESC
""", nativeQuery = true)
    Page<Object[]> getAllPage(Pageable pageable);



    @Query(value = """
    SELECT * FROM model_san_pham m
    WHERE 1=1
      AND (:search IS NULL OR m.ten_model COLLATE Vietnamese_CI_AI LIKE CONCAT('%', :search, '%'))
      AND (:idLoai IS NULL OR m.id_loai = :idLoai)
      AND (:idRam IS NULL OR m.id_ram = :idRam)
      AND (:idXuatXu IS NULL OR m.id_xuat_xu = :idXuatXu)
    ORDER BY m.id_model_san_pham DESC
    """,
            countQuery = """
    SELECT COUNT(*) FROM model_san_pham m
    WHERE 1=1
      AND (:search IS NULL OR m.ten_model COLLATE Vietnamese_CI_AI LIKE CONCAT('%', :search, '%'))
      AND (:idLoai IS NULL OR m.id_loai = :idLoai)
      AND (:idRam IS NULL OR m.id_ram = :idRam)
      AND (:idXuatXu IS NULL OR m.id_xuat_xu = :idXuatXu)
    """,
            nativeQuery = true)
    Page<ModelSanPham> findByFiltersNative(
            @Param("search") String search,
            @Param("idLoai") Integer idLoai,
            @Param("idRam") Integer idRam,
            @Param("idXuatXu") Integer idXuatXu,
            Pageable pageable);


//    @Query("""
//        select count (m) > 0 from ModelSanPham m where
//        lower(m.tenModel) = lower(:tenModel)
//        and m.idRam.id = :idRam
//        and m.idManHinh.id = :idManHinh
//        and m.idHeDieuHanh.id = :idHeDieuHanh
//        and m.idPin.id = :idPin
//        and m.idCpu.id = :idCpu
//        and m.idCameraTruoc.id = :idCameraTruoc
//        and m.idXuatXu.id = :idXuatXu
//        and m.idLoai.id = :idLoai
//    """)
//    boolean existsModelWithSameConfig (
//            @Param("tenModel") String tenModel,
//            @Param("idRam") Integer idRam,
//            @Param("idManHinh") Integer idManHinh,
//            @Param("idHeDieuHanh") Integer idHeDieuHanh,
//            @Param("idPin") Integer idPin,
//            @Param("idCpu") Integer idCpu,
//            @Param("idCameraTruoc") Integer idCameraTruoc,
//            @Param("idXuatXu") Integer idXuatXu,
//            @Param("idLoai") Integer idLoai
//    );


//        and m.idRam.id = :idRam
    @Query("""
        select count (m) > 0 from ModelSanPham m where
        lower(m.tenModel) = lower(:tenModel)
        and m.idXuatXu.id = :idXuatXu
        and m.idLoai.id = :idLoai
        and m.trangThaiSanPhamModel = :trangThai
    """)
    boolean existsModelWithSameConfig (
            @Param("tenModel") String tenModel,
//            @Param("idRam") Integer idRam,
            @Param("idXuatXu") Integer idXuatXu,
            @Param("idLoai") Integer idLoai,
            @Param("trangThai") TrangThaiSanPhamModel trangThaiSanPhamModel
    );

    boolean existsByIdModelSanPham(Integer tenModel);

    @Query(value = "select md from ModelSanPham md " +
            "where md.trangThaiSanPhamModel =:trangThai")
    List<ModelSanPham> findAllByStatus(@Param("trangThai")TrangThaiSanPhamModel trangThaiSanPhamModel);

}
