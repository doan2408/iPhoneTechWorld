package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Response.TinhThanhDTO;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/admin/tinh-thanh")
public class TinhThanhController {
    private final RestTemplate restTemplate = new RestTemplate();


    @GetMapping("/provinces")
    public ResponseEntity<?> getProvinces() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://provinces.open-api.vn/api/?depth=1";
        String data = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(data);
    }
    @GetMapping("/districts/{code}")
    public ResponseEntity<?> getDistricts(@PathVariable String code) {
        String url = "https://provinces.open-api.vn/api/p/" + code + "?depth=2";
        return ResponseEntity.ok(new RestTemplate().getForObject(url, String.class));
    }
    @GetMapping("/wards/{code}")
    public ResponseEntity<?> getWards(@PathVariable String code) {
        String url = "https://provinces.open-api.vn/api/d/" + code + "?depth=2";
        return ResponseEntity.ok(new RestTemplate().getForObject(url, String.class));
    }
    @GetMapping("/geo")
    public ResponseEntity<?> getGeo(@RequestParam String address) {
        try {
//            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
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
