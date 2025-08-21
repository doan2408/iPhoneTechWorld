package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoaiBaoHanhAdminResponse {

    private Integer id;
    private String tenLoaiBaoHanh;
    private Integer thoiGianThang;
    private String moTa;
}
