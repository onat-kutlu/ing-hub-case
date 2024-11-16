package com.ing.inghub.dto.auth;

import com.ing.inghub.dto.base.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterRequest extends BaseRequest implements Serializable {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String surname;

}
