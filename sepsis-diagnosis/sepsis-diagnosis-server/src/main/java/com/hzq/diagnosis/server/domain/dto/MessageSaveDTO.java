package com.hzq.diagnosis.server.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.domain.dto MessageSaveDTO
 * @date 2024/12/21 21:29
 * @description 消息保存到后端传输对象
 */
@Data
@ToString
@EqualsAndHashCode
public class MessageSaveDTO {

    @NotBlank(message = "当前会话ID不得为空")
    private String sessionId;

    @NotBlank(message = "发送内容不得为空")
    private String userContent;

    @NotBlank(message = "发送内容不得为空")
    private String aiContent;
}
