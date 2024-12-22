package com.hzq.diagnosis.server.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.domain.dto SessionDeleteDTO
 * @date 2024/12/22 12:26
 * @description 前端传递删除Session聊天会话的请求体对象
 */
@Data
@ToString
@EqualsAndHashCode
public class SessionDeleteDTO {
    private List<String> sessionIds;
}
