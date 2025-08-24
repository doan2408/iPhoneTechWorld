package org.example.websitetechworld.Dto.Request.CommonRequest;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnImeiRequest {
    private Integer idHoaDonChiTiet;
    private String soImei;
    private Integer idFailReason;

    private String urlHinh;
    private String urlVideo;
}
