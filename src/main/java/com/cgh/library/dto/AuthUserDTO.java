package com.cgh.library.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author cenganhui
 */
@Getter
@Setter
@ApiModel(value = "授权用户DTO")
public class AuthUserDTO {

    @NotBlank
    @ApiModelProperty(value = "用户名", example = "test")
    private String username;

    @NotBlank
    @ApiModelProperty(value = "密码", example = "123")
    private String password;

    @Override
    public String toString() {
        return "{username = " + username + ", password = ******}";
    }

}
