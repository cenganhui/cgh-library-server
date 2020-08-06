package com.cgh.library.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author cenganhui
 */
@Data
@ApiModel(value = "权限请求")
public class AdminReq {

    @NotNull
    @ApiModelProperty(value = "用户id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "管理员")
    private Boolean admin;

}
