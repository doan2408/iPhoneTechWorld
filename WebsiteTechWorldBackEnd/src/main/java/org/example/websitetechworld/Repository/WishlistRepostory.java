package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishlistRepostory extends JpaRepository<Wishlist, Integer> {
    Page<Wishlist> findWishlistByKhacHang_Id(Integer idKhachHang, Pageable pageable);

    @Query(value = """
        SELECT CASE
                WHEN EXISTS(
                    SELECT 1
                    FROM wishlist
                    WHERE khac_hang_id = :idKhachHang AND chi_tiet_san_pham_id = :idSanPhamChiTiet
                )THEN CAST(1 AS BIT)
                ELSE CAST(0 AS BIT)
           END AS is_exists
    """,nativeQuery = true)
    boolean existsWishList(@Param("idKhachHang") Integer idKhachHang, @Param("idSanPhamChiTiet") Integer idSanPhamChiTiet);

    @Transactional
    void deleteByChiTietSanPham_Id(Integer idSanPhamChiTiet);
}
