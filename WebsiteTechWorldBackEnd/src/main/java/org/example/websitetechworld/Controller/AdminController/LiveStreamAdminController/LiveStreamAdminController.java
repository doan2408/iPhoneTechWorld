package org.example.websitetechworld.Controller.AdminController.LiveStreamAdminController;

import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class LiveStreamAdminController {

    private final SanPhamAdminService sanPhamAdminService;
    private final SimpMessagingTemplate messagingTemplate;

    public LiveStreamAdminController(SanPhamAdminService sanPhamAdminService,
                                     SimpMessagingTemplate messagingTemplate) {
        this.sanPhamAdminService = sanPhamAdminService;
        this.messagingTemplate = messagingTemplate;
    }

    private String currentStreamId = null;
    private final Map<String, List<Map<String, Object>>> liveProducts = new HashMap<>();
    private final Map<String, List<Map<String, Object>>> liveChats = new HashMap<>();

    @PostMapping("/live-stream/create")
    @ResponseBody
    public Map<String, String> createLiveStream() {
        currentStreamId = UUID.randomUUID().toString();

        messagingTemplate.convertAndSend("/topic/stream/global", Map.of(
                "type", "start",
                "streamId", currentStreamId
        ));

        return Map.of("streamId", currentStreamId);
    }

    @PostMapping("/live-stream/stop")
    @ResponseBody
    public void stopLiveStream() {
        if (currentStreamId != null) {
            // Broadcast sự kiện end cho tất cả viewer
            messagingTemplate.convertAndSend("/topic/stream/global", Map.of(
                    "type", "end",
                    "streamId", currentStreamId
            ));
            currentStreamId = null;
        }
    }

    @GetMapping("/live-stream/current")
    @ResponseBody
    public Map<String, String> getCurrentLiveStream() {
        return Map.of("streamId", currentStreamId != null ? currentStreamId : "");
    }

    @MessageMapping("/product/{streamId}")
    @SendTo("/topic/product/{streamId}")
    public Map<String, Object> handleProduct(@DestinationVariable String streamId,
                                             @Payload Map<String, Object> payload) {
        liveProducts.computeIfAbsent(streamId, k -> new ArrayList<>()).add(payload);
        Map<String, Object> message = new HashMap<>();
        message.put("type", "product");
        message.put("product", payload.get("product"));
        return message;
    }

    @GetMapping("/live-products")
    @ResponseBody
    public List<Map<String, Object>> getLiveProducts(@RequestParam String streamId) {
        return liveProducts.getOrDefault(streamId, Collections.emptyList());
    }

    @MessageMapping("/signal/{streamId}")
    @SendTo("/topic/signal/{streamId}")
    public Map<String, Object> handleSignal(@DestinationVariable String streamId,
                                            @Payload Map<String, Object> message) {
        return message;
    }

    @MessageMapping("/join/{streamId}")
    @SendToUser("/queue/stream-status")
    public Map<String, Object> handleJoin(@DestinationVariable String streamId, String viewerId) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "status");
        message.put("isLive", currentStreamId != null && currentStreamId.equals(streamId));
        return message;
    }

    @MessageMapping("/chat/{streamId}")
    @SendTo("/topic/chat/{streamId}")
    public Map<String, Object> handleChat(@DestinationVariable String streamId,
                                          @Payload Map<String, Object> message) {
        liveChats.computeIfAbsent(streamId, k -> new ArrayList<>()).add(message);
        return message;
    }

    @GetMapping("/live-chats")
    @ResponseBody
    public List<Map<String, Object>> getLiveChats(@RequestParam String streamId) {
        return liveChats.getOrDefault(streamId, Collections.emptyList());
    }

    @GetMapping("/live-san-pham")
    @ResponseBody
    public ResponseEntity<?> getSanPham(@RequestParam String search,
                                        @RequestParam int page,
                                        @RequestParam int size) {
        return ResponseEntity.ok(sanPhamAdminService.getAllSanPhamLive(search, page, size));
    }
}