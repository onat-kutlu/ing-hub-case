package com.ing.inghub.dto.auth;

import com.ing.inghub.dto.base.BaseResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class LoginResponse extends BaseResponse {
    private String token;
    private Instant tokenExpirationDate;
}
