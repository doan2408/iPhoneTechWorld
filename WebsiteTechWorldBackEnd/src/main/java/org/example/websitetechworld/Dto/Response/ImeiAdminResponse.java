package org.example.websitetechworld.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImeiAdminResponse {
    private Integer id;
    private String soImei;
    private String trangThaiImei;
    private String nhaMang;
}
