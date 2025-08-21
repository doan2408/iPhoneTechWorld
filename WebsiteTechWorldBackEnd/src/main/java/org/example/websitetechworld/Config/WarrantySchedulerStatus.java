package org.example.websitetechworld.Config;


import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.BaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Repository.BaoHanhRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WarrantySchedulerStatus {

    private final BaoHanhRepository baoHanhRepository;

    /**
     * Task này chạy mỗi ngày lúc 00:00 (theo cron)
     * Kiểm tra tất cả bảo hành, nếu ngày kết thúc < hiện tại
     * và trạng thái chưa hết hạn, sẽ tự động set thành "WARRANTY_EXPIRED"
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredWarranty() {
        Date today = new Date();
        List<BaoHanh> list = baoHanhRepository.baoHanhByStatus(TrangThaiBaoHanh.UNDER_WARRANTY);

        for (BaoHanh b : list) {
            if (b.getNgayKetThuc() != null
                    && today.after(b.getNgayKetThuc())
                    && !"WARRANTY_EXPIRED".equals(b.getTrangThaiBaoHanh())) {

                b.setTrangThaiBaoHanh(TrangThaiBaoHanh.WARRANTY_EXPIRED);
                baoHanhRepository.save(b);
                System.out.println("Updated warranty ID " + b.getId() + " to EXPIRED on " + today);
            }
        }
    }
}
