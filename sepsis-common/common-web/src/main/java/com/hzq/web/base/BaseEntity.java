package com.hzq.web.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author hua
 * @className com.hzq.common.dao BaseEntity
 * @date 2024/8/28 21:59
 * @description 实体类的公共字段
 */
@MappedSuperclass
@Data
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(createBy, that.createBy) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateBy, that.updateBy) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createBy, createTime, updateBy, updateTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("createBy", createBy)
                .append("createTime", sdf.format(createTime))
                .append("updateBy", updateBy)
                .append("updateTime", sdf.format(updateTime))
                .toString();
    }
}
