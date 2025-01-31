package com.kijiri.aurora.api.shared.exception;

import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    protected BaseException(BusinessErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
