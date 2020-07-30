package com.cgh.library.persistence.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cenganhui
 */
@Entity
@Getter
@Setter
@Table(name = "lib_user")
@ApiModel(value = "用户实体类")
public class User extends BaseEntity {

    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id，创建不用传id，修改要传", hidden = true)
    private Long id;

    @NotBlank
    @Column(unique = true)
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "管理员权限")
    private Boolean admin = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ApiModelProperty(value = "书本列表")
    private List<Book> books;

}
