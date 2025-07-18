package org.example.websitetechworld.Dto.Request.ClientRequest.SanPhamClientRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoaiClientRequest {
    private Integer idLoai;
    private String tenLoai;
}
