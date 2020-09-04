package com.cgh.library.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author cenganhui
 */
@Data
@ApiModel(value = "二维码DTO")
public class QRCodeDTO {

    private String codeUrl;

    private String gid;

    private String sign;

    private String time;

}
