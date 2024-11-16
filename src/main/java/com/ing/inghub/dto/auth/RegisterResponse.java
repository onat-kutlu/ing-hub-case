package com.ing.inghub.dto.auth;

import com.ing.inghub.dto.CustomerDto;
import com.ing.inghub.dto.base.BaseResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterResponse  extends BaseResponse implements Serializable {
    private CustomerDto customerDto;
}
