package com.hzq.diagnosis.server.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.domain.entity TbChatSession
 * @date 2024/12/21 19:29
 * @description 聊天会话实体类，每新建一个聊天都是一次会话
 */
@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tb_chat_session")
public class TbChatSession  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "VARCHAR(36) comment '会话唯一UUID'")
    private String id;

    @Column(name = "title", columnDefinition = "VARCHAR(100) comment '会话主题'")
    private String title;

    @Column(name = "user", columnDefinition = "VARCHAR(30) comment '会话发起人'")
    private String user;

    @Column(name ="create_time", columnDefinition = "DATETIME comment '会话创建时间'")
    private LocalDateTime createTime;
}
