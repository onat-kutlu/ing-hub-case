package com.ing.inghub.dto.auth;

import com.ing.inghub.dto.base.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class LoginRequest extends BaseRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
