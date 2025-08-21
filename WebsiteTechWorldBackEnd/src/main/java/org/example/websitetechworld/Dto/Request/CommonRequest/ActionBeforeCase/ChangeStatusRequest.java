package org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.ActionAfterCase;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequest {
    private List<String> soImeis;
    private ActionAfterCase status;
}
