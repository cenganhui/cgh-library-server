package com.cgh.library.dto;

import com.cgh.library.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cenganhui
 */
@Data
@NoArgsConstructor
@ApiModel(value = "在线用户")
public class OnlineUserDTO {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "Redis key", example = Constants.REDIS_USER_PREFIX + "username")
    private String key;

    @ApiModelProperty(value = "登录时间")
    private Date loginTime;

    @ApiModelProperty(value = "管理员权限")
    private Boolean admin;

}
