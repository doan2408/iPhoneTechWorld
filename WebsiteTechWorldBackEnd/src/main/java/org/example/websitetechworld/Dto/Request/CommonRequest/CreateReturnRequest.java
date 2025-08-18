package org.example.websitetechworld.Dto.Request.CommonRequest;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReturnRequest {
    private Integer idHoaDon;
    private List<ReturnImeiRequest> imeis;
}
