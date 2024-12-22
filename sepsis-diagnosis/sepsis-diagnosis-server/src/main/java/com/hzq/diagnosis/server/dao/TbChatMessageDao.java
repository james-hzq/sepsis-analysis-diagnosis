package com.hzq.diagnosis.server.dao;

import com.hzq.diagnosis.server.domain.entity.TbChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hua
 * @interfaceName com.hzq.diagnosis.server.dao TbChatMessageDao
 * @date 2024/12/21 19:51
 * @description 聊天消息 Dao 层
 */
public interface TbChatMessageDao extends JpaRepository<TbChatMessage, Long> {

    List<TbChatMessage> findAllBySessionIdOrderByIdAsc(@Param("sessionId") String sessionId);

    @Query("select t from TbChatMessage t where t.sessionId in :sessionIds")
    List<TbChatMessage> findAllBySessionIds(@Param("sessionIds") List<String> sessionIds);
}
