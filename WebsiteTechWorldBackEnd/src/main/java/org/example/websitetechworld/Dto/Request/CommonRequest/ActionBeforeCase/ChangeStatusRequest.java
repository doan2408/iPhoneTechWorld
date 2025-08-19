package org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.ActionAfterCase;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequest {
    private String soImei;
    private ActionAfterCase status;
}
