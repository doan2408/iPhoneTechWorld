package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.Ram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RamRepository extends JpaRepository<Ram, Integer> {
    boolean existsByDungLuong(String dungLuong);
    boolean existsByDungLuongAndIdNot(String dungLuong, Integer id);
    Page<Ram> findByDungLuongContainingIgnoreCaseOrNhaSanXuatContainingIgnoreCase(@Size(max = 50) String dungLuong, @Size(max = 50) String nhaSanXuat, Pageable pageable);

    Optional<Ram> findByDungLuong(String dungLuonng);

    Optional<Ram> findByDungLuongAndLoai(String dungLuong, String loaiRam);


    // Kiểm tra xem đã tồn tại RAM với dung lượng + loại + tốc độ chưa
    boolean existsByDungLuongAndLoai(String dungLuong, String loaiRam);

    // Kiểm tra khi update, loại bỏ bản ghi đang update
    boolean existsByDungLuongAndLoaiAndIdNot(String dungLuong, String loaiRam, Integer id);

    // Optional: tìm bản ghi theo dung lượng + loại + tốc độ
//    Optional<Ram> findByDungLuongAndLoaiRamAndTocDo(String dungLuong, String loaiRam, String tocDo);
}