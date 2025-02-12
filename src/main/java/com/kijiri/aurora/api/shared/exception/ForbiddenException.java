package com.kijiri.aurora.api.shared.exception;

import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;

public class ForbiddenException extends BaseException {

    public ForbiddenException(BusinessErrorCodes errorCode, String message) {
        super(errorCode, message);
    }
}
