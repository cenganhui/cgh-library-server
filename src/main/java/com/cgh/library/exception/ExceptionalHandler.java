package com.cgh.library.exception;

import com.cgh.library.api.BaseResponse;
import com.cgh.library.api.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 异常处理类
 *
 * @author cenganhui
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionalHandler implements Ordered {

    private final MultipartProperties multipartProperties;

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResponse<Object> handle(MaxUploadSizeExceededException e) {
        log.warn(e.getMessage(), e);
        return BaseResponse.buildResponse(StatusCode.REQUEST_PARAM_ILLEGAL.message(String.format("上传文件超过大小限制：%sMB",
                multipartProperties.getMaxFileSize().toMegabytes())));
    }

    @ExceptionHandler(LibraryException.class)
    public BaseResponse<Object> handle(LibraryException e) {
        log.warn(e.getMessage(), e);
        return BaseResponse.buildResponse(e.getCode());
    }

}
