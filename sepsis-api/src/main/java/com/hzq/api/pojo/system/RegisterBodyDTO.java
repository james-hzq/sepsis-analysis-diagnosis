package com.hzq.api.pojo.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author hua
 * @className com.hzq.system.pojo.dto RegisterBodyDTO
 * @date 2024/9/1 15:29
 * @description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBodyDTO {
    private String username;

    private String password;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
