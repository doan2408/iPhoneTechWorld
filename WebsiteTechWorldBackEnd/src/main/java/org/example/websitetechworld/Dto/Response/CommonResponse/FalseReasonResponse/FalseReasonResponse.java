package org.example.websitetechworld.Dto.Response.CommonResponse.FalseReasonResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.CaseReason.CaseType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FalseReasonResponse {
    private Integer idReason;
    private String tenLyDo;
    private CaseType loaiVuViec;

}
