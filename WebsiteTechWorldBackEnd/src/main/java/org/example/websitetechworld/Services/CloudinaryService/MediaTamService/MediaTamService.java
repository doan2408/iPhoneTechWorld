package org.example.websitetechworld.Services.CloudinaryService.MediaTamService;

import org.example.websitetechworld.Entity.MediaTam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MediaTamService {
    private final Map<Integer, List<MediaTam>> mediaTamMap = new HashMap<>();

    public void addMedia(Integer idImei, String url, String type) {
        mediaTamMap.computeIfAbsent(idImei, k -> new ArrayList<>())
                .add(new MediaTam(url, type));
    }

    public List<MediaTam> getMedia(Integer idImei) {
        return mediaTamMap.getOrDefault(idImei, new ArrayList<>());
    }

    public void clearMedia(Integer idImei) {
        mediaTamMap.remove(idImei);
    }
}
