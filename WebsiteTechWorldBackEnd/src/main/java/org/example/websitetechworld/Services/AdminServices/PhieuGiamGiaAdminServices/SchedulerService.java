package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Events.KhuyenMaiUpdatedEvent;
import org.example.websitetechworld.Events.PhieuGiamGiaUpdatedEvent;
import org.example.websitetechworld.Repository.KhuyenMaiRepository;
import org.example.websitetechworld.Repository.PhieuGiamGiaRepository;
import org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService.KhuyenMaiAdminService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final PhieuGiamGiaAdminService phieuGiamGiaAdminService;
    private final KhuyenMaiAdminService khuyenMaiAdminService;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhuyenMaiRepository khuyenMaiRepository;
    private final ThreadPoolTaskScheduler scheduler;

    private ScheduledFuture<?> futureTaskPhieu;
    private ScheduledFuture<?> futureTaskKhuyenMai;


    @PostConstruct
    public void start() {
        runTaskPhieu();
        runTaskKhuyenMai();
    }

    @PreDestroy
    public void stop() {
        if (futureTaskPhieu != null && !futureTaskPhieu.isCancelled()) {
            futureTaskPhieu.cancel(false);
        }
        if (futureTaskKhuyenMai != null && !futureTaskKhuyenMai.isCancelled()) {
            futureTaskKhuyenMai.cancel(false);
        }
    }

    public void runTaskPhieu() {
        try {
            LocalDateTime now = LocalDateTime.now();
            phieuGiamGiaAdminService.capNhatTrangThaiPhieuGiamGia(now);
            scheduleNextRun(calculateNextDelayPhieu(now), true);
        } catch (Exception e) {
            e.printStackTrace();
            scheduleNextRun(Duration.ofMinutes(1), true);
        }
    }

    public void runTaskKhuyenMai() {
        try {
            LocalDateTime now = LocalDateTime.now();
            khuyenMaiAdminService.capNhatTrangThaiKhuyenMai(now);
            scheduleNextRun(calculateNextDelayKhuyenMai(now), false);
        } catch (Exception e) {
            e.printStackTrace();
            scheduleNextRun(Duration.ofMinutes(1), false);
        }
    }

    public Optional<Duration> durationToNextUpdate(LocalDateTime now, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        if (now.isBefore(ngayBatDau)) {
            return Optional.of(Duration.between(now, ngayBatDau));
        } else if (now.isBefore(ngayKetThuc)) {
            return Optional.of(Duration.between(now, ngayKetThuc));
        } else {
            return Optional.empty();
        }
    }

    private void scheduleNextRun(Duration delay, boolean isPhieu) {
        if (scheduler == null) return;
        if (isPhieu) {
            if (futureTaskPhieu != null && !futureTaskPhieu.isCancelled()) futureTaskPhieu.cancel(false);
            futureTaskPhieu = scheduler.schedule(this::runTaskPhieu, Instant.now().plus(delay));
        } else {
            if (futureTaskKhuyenMai != null && !futureTaskKhuyenMai.isCancelled()) futureTaskKhuyenMai.cancel(false);
            futureTaskKhuyenMai = scheduler.schedule(this::runTaskKhuyenMai, Instant.now().plus(delay));
        }
    }

    public Duration calculateNextDelayPhieu (LocalDateTime now) {
        Optional<Duration> nextDelay = phieuGiamGiaRepository.findAll()
                .stream()
                .map(p -> durationToNextUpdate(now, p.getNgayBatDau(), p.getNgayKetThuc()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Duration::compareTo);

        return nextDelay.orElse(Duration.ofDays(1));
    }

    public Duration calculateNextDelayKhuyenMai (LocalDateTime now) {
        Optional<Duration> nextDelay = khuyenMaiRepository.findAll()
                .stream()
                .map(p -> durationToNextUpdate(now, p.getNgayBatDau(), p.getNgayKetThuc()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Duration::compareTo);

        return nextDelay.orElse(Duration.ofDays(1));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePhieuGiamGiaUpdated(PhieuGiamGiaUpdatedEvent event) {
        runTaskPhieu();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleKhuyenMaiUpdated(KhuyenMaiUpdatedEvent event) {
        runTaskKhuyenMai();
    }
}