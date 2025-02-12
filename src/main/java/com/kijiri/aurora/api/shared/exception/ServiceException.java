package com.kijiri.aurora.api.shared.exception;


import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;

public class ServiceException extends BaseException {
    public ServiceException(BusinessErrorCodes errorCode, String message) {
        super(errorCode, message);
    }
}
