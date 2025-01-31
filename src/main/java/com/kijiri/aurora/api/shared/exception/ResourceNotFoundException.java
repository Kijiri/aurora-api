package com.kijiri.aurora.api.shared.exception;

import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(BusinessErrorCodes errorCode, String message) {
        super(errorCode, message);
    }
}
