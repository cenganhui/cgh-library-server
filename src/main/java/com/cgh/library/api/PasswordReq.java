package com.cgh.library.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author cenganhui
 */
@Data
@ApiModel("修改密码请求")
public class PasswordReq {

    @NotEmpty
    @ApiModelProperty(value = "原密码")
    private String oldPassword;

    @NotEmpty
    @ApiModelProperty(value = "新密码")
    private String newPassword;

}
