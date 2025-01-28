package com.kijiri.aurora.api.shared.exception;

import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;

public class DuplicateException extends BaseException {
    
    public DuplicateException(BusinessErrorCodes errorCode, String message) {
        super(errorCode, message);
    }
}
