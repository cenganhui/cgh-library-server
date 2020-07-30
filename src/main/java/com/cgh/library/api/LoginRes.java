package com.cgh.library.api;

import com.cgh.library.persistence.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cenganhui
 */
@Data
@ApiModel(value = "登录响应")
public class LoginRes {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "用户")
    private User user;

}
