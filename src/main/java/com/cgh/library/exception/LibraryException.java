package com.cgh.library.exception;

import com.cgh.library.api.StatusCode;

/**
 * 自定义异常
 *
 * @author cenganhui
 */
public class LibraryException extends RuntimeException {

    private transient StatusCode code = StatusCode.UNKNOWN_EXCEPTION;

    public LibraryException() {
        super();
    }

    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(StatusCode code) {
        super(code.getMsg());
        this.code = code;
    }

    public LibraryException(String message, StatusCode code) {
        super(message);
        this.code = code.message(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibraryException(String message, Throwable cause, StatusCode code) {
        super(message, cause);
        this.code = code.message(message);
    }

    public LibraryException(Throwable cause) {
        super(cause);
    }

    public LibraryException(Throwable cause, StatusCode code) {
        super(cause);
        this.code = code;
    }

    public StatusCode getCode() {
        return code;
    }

}
