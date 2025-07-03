package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodResponse {
    private String code;
    private String displayName;
}
