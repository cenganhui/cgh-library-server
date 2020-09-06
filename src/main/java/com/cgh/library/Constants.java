package com.cgh.library;

/**
 * 常量类
 *
 * @author cenganhui
 */
public abstract class Constants {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_EXAMPLE = "2020-06-18 10:56:45";

    public static final String SAVE_FILE_PATH = "/home/www/pdf/";

//    public static final String SAVE_FILE_PATH = "E:\\library\\";

    public static final String HOST = "http://47.115.148.227:8204/pdf/";

    public static final String REDIS_USER_PREFIX = "library:lib_user:";

    public static final String REDIS_BOOK_PREFIX = "library:lib_book:";

    public static final String BOOK_FORMAT = "pdf";

    public static final String PDF = "255044462D312E";

    public static final String SEND_EMAIL_SUBJECT = "贴吧签到结果";

    private Constants() {
    }

}
