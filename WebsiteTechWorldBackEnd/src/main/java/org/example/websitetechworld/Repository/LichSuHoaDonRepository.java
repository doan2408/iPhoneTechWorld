package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.TabHoaDonAdminResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.LichSuHoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon,Integer> {
    Page<LichSuHoaDon> findByIdHoaDon_Id(Integer idHoaDon, Pageable pageable);

    @Query("""

            SELECT new org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.TabHoaDonAdminResponse(hd.id, hd.maHoaDon) FROM LichSuHoaDon lshd\s
			LEFT JOIN HoaDon hd\s
			ON lshd.idHoaDon.id = hd.id
			WHERE lshd.idNhanVien.id = :idNhanVien\s
			AND lshd.hanhDong = 'CREATE'
			AND hd.trangThaiThanhToan = 'PENDING'
""")
    List<TabHoaDonAdminResponse> findMaHoaDonPendingByNhanVien(@Param("idNhanVien") Integer idNhanVien);
}
