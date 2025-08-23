package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhHistoryAdminResponse;
import org.example.websitetechworld.Entity.LichSuBaoHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LichSuBaoHanhRepository extends JpaRepository<LichSuBaoHanh,Integer> {

    @Query("""
        SELECT lsbh
            FROM LichSuBaoHanh lsbh
            ORDER BY lsbh.ngayTiepNhan DESC
    """)
    Page<LichSuBaoHanh> findDonBaoHanh(Pageable pageable);

    @Query("""
        SELECT new org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhHistoryAdminResponse(
            lsbh.id,
            lsbh.trangThai,
            lsbh.idBaoHanh.idLoaiBaoHanh.tenLoaiBaoHanh,
            lsbh.moTaLoi,
            lsbh.ngayTiepNhan,
            lsbh.ngayHoanThanh
        )
            FROM LichSuBaoHanh lsbh
    """)
    List<BaoHanhHistoryAdminResponse> findHistory(String soImei);
}
