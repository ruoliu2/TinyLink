package com.ruo.tinylink.admin.common.convention.exception;

import com.ruo.tinylink.admin.common.convention.errorcode.IErrorCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * abstract three types of exceptions
 *
 * @see ClientException
 * @see ServiceException
 * @see RemoteException
 */
@Getter
public abstract class AbstractException extends RuntimeException {

  public final String errorCode;

  public final String errorMessage;

  public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
    super(message, throwable);
    this.errorCode = errorCode.code();
    this.errorMessage =
        Optional.ofNullable(StringUtils.hasLength(message) ? message : null)
            .orElse(errorCode.message());
  }
}
