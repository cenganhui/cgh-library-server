package com.cgh.library.persistence.entity;

import com.cgh.library.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author cenganhui
 */
@Entity
@Getter
@Setter
@Table(name = "lib_tieba_user")
@ApiModel(value = "贴吧用户实体类")
public class TieBaUser {

    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id，创建不用传id，修改要传", hidden = true)
    private Long id;

    @Column(unique = true)
    @ApiModelProperty(value = "贴吧id")
    private Long openUid;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "登录状态")
    private Boolean login;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像路径")
    private String avatar;

    @ApiModelProperty(value = "bduss")
    private String bduss;

    @ApiModelProperty(value = "stoken")
    private String stoken;

    @ApiModelProperty(value = "总贴吧数")
    private Integer totalTb;

    @ApiModelProperty(value = "已签到贴吧数")
    private Integer signedTb;

    @ApiModelProperty(value = "失败签到贴吧数")
    private Integer errorTb;

    @ApiModelProperty(value = "签到状态")
    private Boolean signStatus;

    @ApiModelProperty(value = "耗时")
    private String cost;

    @ApiModelProperty(value = "签到时间", example = Constants.DATE_FORMAT_EXAMPLE)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private LocalDateTime signTime;

}
