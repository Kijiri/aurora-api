package com.kijiri.aurora.api.shared.exception;

import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;

public class BadRequestException extends BaseException {
    public BadRequestException(BusinessErrorCodes errorCode, String message) {
        super(errorCode, message);
    }
}
