package com.hzq.diagnosis.server.dao;

import com.hzq.diagnosis.server.domain.entity.TbChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hua
 * @interfaceName com.hzq.diagnosis.server.dao TbChatSessionDao
 * @date 2024/12/21 19:52
 * @description 聊天会话 Dao 层
 */
public interface TbChatSessionDao extends JpaRepository<TbChatSession, String> {

    TbChatSession findFirstByUserOrderByCreateTimeDesc(@Param("user") String user);

    List<TbChatSession> findAllByUserOrderByCreateTimeDesc(@Param("user") String user);
}
