package com.cgh.library.api;

import lombok.Getter;
import org.slf4j.event.Level;

/**
 * 状态码
 *
 * @author cenganhui
 */
@Getter
public class StatusCode {

    public static final StatusCode SUCCESS = new StatusCode("0000", "请求成功", Level.INFO);

    public static final StatusCode UNKNOWN_EXCEPTION = new StatusCode("9999", "系统未知错误", Level.ERROR);

    public static final StatusCode REQUEST_PARAM_ILLEGAL = new StatusCode("0001", "请求参数非法", Level.WARN);

    public static final StatusCode REQUEST_SIGNED_INVALID = new StatusCode("0002", "请求验签异常", Level.WARN);

    public static final StatusCode REQUEST_DECRYPT_INVALID = new StatusCode("0003", "请求数据解密异常", Level.WARN);

    public static final StatusCode OBJECT_NOT_EXIST = new StatusCode("0004", "对象不存在", Level.WARN);

    public static final StatusCode SERVICE_NOT_AVAILABLE = new StatusCode("0005", "服务不可用", Level.ERROR);

    public static final StatusCode LOGIN_FAILED = new StatusCode("0007", "用户名或密码错误", Level.INFO);

    public static final StatusCode NO_LOGIN = new StatusCode("0008", "未登录", Level.INFO);

    public static final StatusCode BOOK_UNAUTHORIZED_ACCESS = new StatusCode("0009", "你无此书", Level.INFO);

    public static final StatusCode NO_ADMIN_PERMISSION = new StatusCode("0010", "权限不足", Level.WARN);

    public static final StatusCode NOT_FOUND_BOOK = new StatusCode("0011", "找不到此书", Level.WARN);

    public static final StatusCode BOOK_FORMAT_ERROR = new StatusCode("0012", "上传格式错误", Level.WARN);

    public static final StatusCode USER_EXIST = new StatusCode("0013", "用户已存在", Level.WARN);

    private final String code;
    private final String msg;
    private final Level level;

    public StatusCode(String code, String msg, Level level) {
        this.code = code;
        this.msg = msg;
        this.level = level;
    }

    public StatusCode code(String code) {
        return new StatusCode(code, msg, level);
    }

    public StatusCode message(String message) {
        return new StatusCode(code, message, level);
    }

}