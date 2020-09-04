package com.cgh.library.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author cenganhui
 */
@Data
@ApiModel(value = "分页贴吧信息DTO")
public class PageTieBaDTO {

    @ApiModelProperty(value = "贴吧信息DTO列表")
    private List<TieBaDTO> tieBaDTOList;

    @ApiModelProperty(value = "返回总个数")
    private Integer total;

}
