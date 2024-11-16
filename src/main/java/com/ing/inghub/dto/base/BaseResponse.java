package com.ing.inghub.dto.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class BaseResponse implements Serializable {
    private ServiceResult serviceResult;

    public BaseResponse(final ServiceResult serviceResult) {
        this.serviceResult = serviceResult;
    }
}
