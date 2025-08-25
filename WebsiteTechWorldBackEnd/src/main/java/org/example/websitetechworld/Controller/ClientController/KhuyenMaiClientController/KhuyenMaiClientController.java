package org.example.websitetechworld.Controller.ClientController.KhuyenMaiClientController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices.SchedulerService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/khuyen-mai")
@RequiredArgsConstructor
public class KhuyenMaiClientController {
    private final SchedulerService schedulerService;

    @GetMapping("/next-delay")
    public Map<String, Long> getNextDelay() {
        LocalDateTime now = LocalDateTime.now();
        Duration nextDelay = schedulerService.calculateNextDelayKhuyenMai(now);
        return Map.of("delay", nextDelay.toMillis());
    }
}
