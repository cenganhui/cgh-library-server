package com.cgh.library.persistence.entity;

import com.cgh.library.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author cenganhui
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedBy
    @Column(name = "create_by", updatable = false)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @LastModifiedBy
    @Column(name = "update_by")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updatedBy;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(value = "创建时间", example = Constants.DATE_FORMAT_EXAMPLE)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间", example = Constants.DATE_FORMAT_EXAMPLE)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private LocalDateTime updateTime;

    /* 分组校验 */
    public @interface Create {
    }

    /* 分组校验 */
    public @interface Update {
    }

}