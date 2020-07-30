package com.cgh.library.persistence.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 * @author cenganhui
 */
@Entity
@Getter
@Setter
@Table(name = "lib_book")
@ApiModel(value = "书本实体类")
public class Book extends BaseEntity {

    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id，创建不用传id，修改要传", hidden = true)
    private Long id;

    @ApiModelProperty(value = "书名")
    private String name;

    @ApiModelProperty(value = "url", example = "http://...")
    private String url;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "所属的用户")
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty(value = "当前页码")
    private Integer currentPage;

    @ApiModelProperty(value = "总页码")
    private Integer totalPage;

}
