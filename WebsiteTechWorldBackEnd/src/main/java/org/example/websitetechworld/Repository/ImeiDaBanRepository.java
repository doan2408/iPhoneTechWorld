package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImeiDaBanRepository extends JpaRepository<ImeiDaBan, Integer> {

    List<ImeiDaBan> findByIdHoaDonChiTiet_Id(Integer idHoaDonChiTiet_Id);

    List<ImeiDaBan> findByIdHoaDonChiTiet_IdAndSoImeiIn(Integer hdctId, List<String> soImeis);

}
