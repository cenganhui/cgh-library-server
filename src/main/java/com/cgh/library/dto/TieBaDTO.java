package com.cgh.library.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author cenganhui
 */
@Data
@ApiModel(value = "贴吧信息DTO")
public class TieBaDTO {

    @ApiModelProperty(value = "贴吧名称")
    private String name;

    @ApiModelProperty(value = "贴吧id")
    private Integer id;

    @ApiModelProperty(value = "贴吧地址")
    private String url;

    @ApiModelProperty(value = "贴吧等级")
    private Integer levelId;

    @ApiModelProperty(value = "等级名称")
    private String levelName;

    @ApiModelProperty(value = "当前经验")
    private Integer curExp;

    @ApiModelProperty(value = "签到经验")
    private String signExp;

    @ApiModelProperty(value = "签到时间")
    private Date signTime;

    @ApiModelProperty(value = "签到次数")
    private Integer signCount;

    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

}
