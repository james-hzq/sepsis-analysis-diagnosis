package com.hzq.diagnosis.server.service;

import com.google.common.base.Strings;
import com.hzq.diagnosis.server.dao.TbChatMessageDao;
import com.hzq.diagnosis.server.dao.TbChatSessionDao;
import com.hzq.diagnosis.server.domain.dto.MessageSaveDTO;
import com.hzq.diagnosis.server.domain.entity.TbChatMessage;
import com.hzq.diagnosis.server.domain.entity.TbChatSession;
import com.hzq.diagnosis.server.domain.vo.TreeDataVO;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.service DiagnosisChatService
 * @date 2024/12/21 20:08
 * @description 智能问询业务处理类
 */
@RequiredArgsConstructor
@Service
public class DiagnosisChatService {

    private static final String DEFAULT_TITLE = "未命名会话";
    private final TbChatSessionDao tbChatSessionDao;
    private final TbChatMessageDao tbChatMessageDao;

    @Transactional(rollbackFor = Exception.class)
    public TbChatSession createNewSession(String user, String title) {
        TbChatSession session = new TbChatSession();
        session.setUser(user);
        session.setTitle(Strings.isNullOrEmpty(title) ? DEFAULT_TITLE : title);
        session.setCreateTime(LocalDateTime.now());
        return tbChatSessionDao.save(session);
    }

    public TbChatSession getLastSession(String user) {
        return tbChatSessionDao.findFirstByUserOrderByCreateTimeDesc(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSession(List<String> sessionIds) {
        if (CollectionUtils.isEmpty(sessionIds)) return;
        tbChatSessionDao.deleteAllById(sessionIds);
        List<TbChatMessage> messageList = tbChatMessageDao.findAllBySessionIds(sessionIds);
        tbChatMessageDao.deleteAll(messageList);
    }

    public TreeDataVO getSessionTree(String user) {
        List<TreeDataVO> sessionList = tbChatSessionDao
                .findAllByUserOrderByCreateTimeDesc(user)
                .stream()
                .map(session -> new TreeDataVO(session.getId(), session.getTitle(), new ArrayList<>()))
                .toList();
        return new TreeDataVO("root", "历史会话", sessionList);
    }

    public List<TbChatMessage> messageList(String sessionId) {
        return tbChatMessageDao.findAllBySessionIdOrderByIdAsc(sessionId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(MessageSaveDTO messageSaveDTO) {
        String sessionId = messageSaveDTO.getSessionId();
        TbChatMessage userMessage = new TbChatMessage()
                .setSessionId(sessionId)
                .setUserType('1')
                .setContent(messageSaveDTO.getUserContent())
                .setCreateTime(LocalDateTime.now());
        TbChatMessage aiMessage = new TbChatMessage()
                .setSessionId(sessionId)
                .setUserType('0')
                .setContent(messageSaveDTO.getAiContent())
                .setCreateTime(LocalDateTime.now());
        // 发送者类型为 0 说明是 AI 发送的信息
        List<TbChatMessage> messages = List.of(userMessage, aiMessage);
        tbChatMessageDao.saveAll(messages);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(String sessionId) {
        List<TbChatMessage> messageList = tbChatMessageDao.findAllBySessionIdOrderByIdAsc(sessionId);
        tbChatMessageDao.deleteAll(messageList);
    }
}
