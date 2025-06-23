package org.example.websitetechworld.Dto.Request.TokenRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenRequestDTO {
    private String refreshToken;

}
