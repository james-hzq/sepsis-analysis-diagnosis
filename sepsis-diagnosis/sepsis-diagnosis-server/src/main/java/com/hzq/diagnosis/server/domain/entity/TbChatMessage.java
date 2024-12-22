package com.hzq.diagnosis.server.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.domain.entity TbChatMessage
 * @date 2024/12/21 19:45
 * @description 聊天消息实体类
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tb_chat_message")
public class TbChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '无意义,自增主键'")
    private Long id;

    @Column(name = "session_id", nullable = false, columnDefinition = "VARCHAR(36) comment '对应的会话ID'")
    private String sessionId;

    @Column(name = "user_type", columnDefinition = "char(1) comment '信息用户类型：0:AI,1:用户'")
    private Character userType;

    @Column(name = "content", columnDefinition = "text comment '信息内容'")
    private String content;

    @Column(name ="create_time", columnDefinition = "DATETIME comment '会话创建时间'")
    private LocalDateTime createTime;
}
