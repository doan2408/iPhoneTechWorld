package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.CommonResponse.FalseReasonResponse.FalseReasonResponse;
import org.example.websitetechworld.Entity.LyDoXuLy;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LyDoXuLyRepository extends JpaRepository<LyDoXuLy,Integer> {
    LyDoXuLy findByLoaiVuViec(CaseType caseType);

    @Query(value = """
        SELECT 
            new org.example.websitetechworld.Dto.Response.CommonResponse.FalseReasonResponse.FalseReasonResponse(
                ldxl.id,ldxl.tenLyDo,ldxl.loaiVuViec
            )  
        FROM 
            LyDoXuLy ldxl
        WHERE 
            ldxl.loaiVuViec = :loaiLyDo
    """)
    List<FalseReasonResponse> findRealsonByType(@Param("loaiLyDo") CaseType caseType);
}
