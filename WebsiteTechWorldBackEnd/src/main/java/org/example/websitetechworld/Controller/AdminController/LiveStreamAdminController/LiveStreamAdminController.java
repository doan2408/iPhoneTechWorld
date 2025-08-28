package org.example.websitetechworld.Controller.AdminController.LiveStreamAdminController;

import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LiveStreamAdminController {

    private final SanPhamAdminService sanPhamAdminService;

    public LiveStreamAdminController(SanPhamAdminService sanPhamAdminService) {
        this.sanPhamAdminService = sanPhamAdminService;
    }

    @MessageMapping("/signal/{streamId}")
    @SendTo("/topic/signal/{streamId}")
    public String handleSignal(@DestinationVariable String streamId, String message) {
        return message; // Chuyển tiếp tin nhắn signaling
    }

    @MessageMapping("/join/{streamId}")
    @SendTo("/topic/join/{streamId}")
    public String handleJoin(@DestinationVariable String streamId, String message) {
        return message; // Thông báo streamer khi có người xem mới
    }

    @MessageMapping("/chat/{streamId}")
    @SendTo("/topic/chat/{streamId}")
    public String handleChat(@DestinationVariable String streamId, String message) {
        return message; // Phát tin nhắn chat cho tất cả
    }

    @GetMapping("/live-san-pham")
    @ResponseBody
    public ResponseEntity<?> getSanPham (@RequestParam String search, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(sanPhamAdminService.getAllSanPhamLive(search, page, size));
    }
}
