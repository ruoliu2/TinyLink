package com.ruo.tinylink.admin.common.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ruo.tinylink.admin.common.convention.errorcode.BaseErrorCode;
import com.ruo.tinylink.admin.common.convention.exception.AbstractException;
import com.ruo.tinylink.admin.common.convention.result.Result;
import com.ruo.tinylink.admin.common.convention.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component("globalExceptionHandlerByAdmin")
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /** Intercept parameter verification exceptions */
  @SneakyThrows
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public Result validExceptionHandler(
      HttpServletRequest request, MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
    String exceptionStr =
        Optional.ofNullable(firstFieldError)
            .map(FieldError::getDefaultMessage)
            .orElse(StrUtil.EMPTY);
    log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exceptionStr);
    return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr);
  }

  /** Intercept exceptions thrown within the application */
  @ExceptionHandler(value = {AbstractException.class})
  public Result abstractException(HttpServletRequest request, AbstractException ex) {
    if (ex.getCause() != null) {
      log.error(
          "[{}] {} [ex] {}",
          request.getMethod(),
          request.getRequestURL().toString(),
          ex.toString(),
          ex.getCause());
      return Results.failure(ex);
    }
    log.error(
        "[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
    return Results.failure(ex);
  }

  /** Intercept unhandled exceptions */
  @ExceptionHandler(value = Throwable.class)
  public Result defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
    log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
    // 注意，此处是为了聚合模式添加的代码，正常不需要该判断
    if (Objects.equals(
        throwable.getClass().getSuperclass().getSimpleName(),
        AbstractException.class.getSimpleName())) {
      String errorCode = ReflectUtil.getFieldValue(throwable, "errorCode").toString();
      String errorMessage = ReflectUtil.getFieldValue(throwable, "errorMessage").toString();
      return Results.failure(errorCode, errorMessage);
    }
    return Results.failure();
  }

  private String getUrl(HttpServletRequest request) {
    if (StringUtils.isEmpty(request.getQueryString())) {
      return request.getRequestURL().toString();
    }
    return request.getRequestURL().toString() + "?" + request.getQueryString();
  }
}
