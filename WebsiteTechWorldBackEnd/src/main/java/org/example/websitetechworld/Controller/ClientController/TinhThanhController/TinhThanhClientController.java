package org.example.websitetechworld.Controller.ClientController.TinhThanhController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@RestController
@RequestMapping("/tinh-thanh")
public class TinhThanhClientController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/geo")
    public ResponseEntity<?> getGeo(@RequestParam String address) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&limit=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "WebsiteTechWorld (manhphi698@gmail.com)");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi lấy tọa độ: " + e.getMessage());
        }
    }

    @GetMapping("/distance")
    public ResponseEntity<?> getDistance(
            @RequestParam double fromLat,
            @RequestParam double fromLon,
            @RequestParam double toLat,
            @RequestParam double toLon
    ) {
        try {
            String url = String.format(
                    Locale.US,
                    "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false",
                    fromLon, fromLat, toLon, toLat
            );

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi tính khoảng cách: " + e.getMessage());
        }
    }
}
