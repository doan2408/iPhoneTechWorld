package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Events.PhieuGiamGiaUpdatedEvent;
import org.example.websitetechworld.Repository.PhieuGiamGiaRepository;
import org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService.KhuyenMaiAdminService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final PhieuGiamGiaAdminService phieuGiamGiaAdminService;
    private final KhuyenMaiAdminService khuyenMaiAdminService;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final ThreadPoolTaskScheduler scheduler;

    private ScheduledFuture<?> futureTask;

    @PostConstruct
    public void start() {
        runTask();
    }

    @PreDestroy
    public void stop() {
        if (futureTask != null) {
            futureTask.cancel(false);
        }
    }

    private void scheduleNextRun(Duration delay) {
        if (futureTask != null) {
            futureTask.cancel(false);
        }
        futureTask = scheduler.schedule(
                this::runTask,
                Instant.now().plus(delay)
        );
    }

    private void runTask() {
        try {
            LocalDateTime now = LocalDateTime.now();
            phieuGiamGiaAdminService.capNhatTrangThaiPhieuGiamGia(now);
            // khuyenMaiAdminService.capNhatTrangThaiKhuyenMai(now);

            Duration nextDelay;
            if (coPhieuCanUpdateTrongPhut(now)) {
                nextDelay = Duration.ofSeconds(1);
            } else if (coPhieuCanUpdateTrongGio(now)) {
                nextDelay = Duration.ofMinutes(1);
            } else if (coPhieuCanUpdateTrongNgay(now)) {
                nextDelay = Duration.ofHours(1);
            } else {
                nextDelay = Duration.ofDays(1);
            }

            scheduleNextRun(nextDelay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean coPhieuCanUpdateTrongNgay(LocalDateTime now) {
        return phieuGiamGiaRepository.existsPhieuCanCapNhatTrongNgay(now) > 0;
    }

    private boolean coPhieuCanUpdateTrongGio(LocalDateTime now) {
        return phieuGiamGiaRepository.existsPhieuCanCapNhatTrongGio(now, now.plusHours(1)) > 0;
    }

    private boolean coPhieuCanUpdateTrongPhut(LocalDateTime now) {
        return phieuGiamGiaRepository.existsPhieuCanCapNhatTrongPhut(now, now.plusMinutes(1)) > 0;
    }

    private boolean coKhuyenMaiCanUpdateTrongNgay(LocalDateTime now) {
        // TODO: query tương tự cho khuyến mãi
        return false;
    }

    private boolean coKhuyenMaiCanUpdateTrongGio(LocalDateTime now) {
        // TODO: query tương tự cho khuyến mãi
        return false;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePhieuGiamGiaUpdated(PhieuGiamGiaUpdatedEvent event) {
        // DB đã commit xong, an toàn để gọi runTask()
        runTask();
    }
}
