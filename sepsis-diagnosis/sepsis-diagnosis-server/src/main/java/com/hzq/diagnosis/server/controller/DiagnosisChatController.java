package com.hzq.diagnosis.server.controller;

import com.google.common.base.Strings;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.diagnosis.server.domain.dto.MessageSaveDTO;
import com.hzq.diagnosis.server.domain.dto.SessionDeleteDTO;
import com.hzq.diagnosis.server.domain.entity.TbChatMessage;
import com.hzq.diagnosis.server.domain.entity.TbChatSession;
import com.hzq.diagnosis.server.domain.vo.TreeDataVO;
import com.hzq.diagnosis.server.service.AssistantService;
import com.hzq.diagnosis.server.service.DiagnosisChatService;
import com.hzq.security.annotation.RequiresPermissions;
import com.hzq.web.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.controller OpenAIChatController
 * @date 2024/12/21 19:53
 * @description 智能问询请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/diagnosis")
public class DiagnosisChatController {

    private final DiagnosisChatService diagnosisChatService;
    private final AssistantService assistantService;

    @GetMapping("/session/tree")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<TreeDataVO> getSessionTree(
            @RequestParam("user") String user
    ) {
        if (Strings.isNullOrEmpty(user)) throw new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED);
        return Result.success(diagnosisChatService.getSessionTree(user));
    }

    @GetMapping("/session/last")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<TbChatSession> getLastSession(
            @RequestParam("user") String user
    ) {
        if (Strings.isNullOrEmpty(user)) throw new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED);
        return Result.success(diagnosisChatService.getLastSession(user));
    }

    @PostMapping("/session/delete")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<Void> deleteSession(
            @RequestBody SessionDeleteDTO sessionDeleteDTO
    ) {
        diagnosisChatService.deleteSession(sessionDeleteDTO.getSessionIds());
        return Result.success();
    }

    @GetMapping("/session/create")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<TbChatSession> createSession(
            @RequestParam("user") String user, @RequestParam("title") String title
    ) {
        if (Strings.isNullOrEmpty(user)) throw new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED);
        return Result.success(diagnosisChatService.createNewSession(user, title));
    }

    @GetMapping("/message/list")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<TbChatMessage>> messageList(
            @RequestParam("sessionId") String sessionId
    ) {
        return Result.success(diagnosisChatService.messageList(sessionId));
    }

    @GetMapping("/message/send")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<String> messageSend(@RequestParam("content") String content) {
        if (Strings.isNullOrEmpty(content)) throw new SystemException(ResultEnum.SEND_MESSAGE_NOT_EMPTY);
        return Result.success(assistantService.chat(content.trim()));
    }

    @PostMapping("/message/save")
    public Result<Void> model(
           @Validated @RequestBody MessageSaveDTO messageSendDTO
    ) {
        diagnosisChatService.saveMessage(messageSendDTO);
        return Result.success();
    }

    @GetMapping("/message/delete")
    public Result<Void> model(
           @RequestParam("sessionId") String sessionId
    ) {
        diagnosisChatService.deleteMessage(sessionId);
        return Result.success();
    }
}
