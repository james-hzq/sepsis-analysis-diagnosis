import { request } from "@/utils/service"
import type * as Index from "./types/index"
import {MessageDeleteResponseData, SessionDeleteRequestData, SessionDeleteResponseData} from "./types/index";

/**
 * 获取历史对话树
 */
export function sessionTreeApi(user: string) {
  return request<Index.SessionTreeResponseData>({
    url: "/diagnosis/session/tree",
    method: "get",
    params: {
      user: user
    }
  })
}

/**
 * 查询当前用户最晚创建的会话（即页面打开显示的会话）
 */
export function sessionLastApi(user: string) {
  return request<Index.SessionResponseData>({
    url: "/diagnosis/session/last",
    method: "get",
    params: {
      user: user
    }
  })
}

/**
 * 请求创建新会话
 */
export function sessionCreateApi(user: string, title: string) {
  return request<Index.SessionResponseData>({
    url: "/diagnosis/session/create",
    method: "get",
    params: {
      user: user,
      title: title
    }
  })
}

/**
 * 查询当前会话的所有信息
 */
export function messageListApi(sessionId: string) {
  return request<Index.MessageListResponseData>({
    url: "/diagnosis/message/list",
    method: "get",
    params: {
      sessionId: sessionId
    }
  })
}

/**
 * 发送信息
 */
export function messageSendApi(content: string) {
  return request<Index.MessageSendResponseData>({
    url: "/diagnosis/message/send",
    method: "get",
    params: {
      content: content
    }
  })
}

/**
 * 保存信息
 */
export function messageSaveApi(data: Index.MessageSaveRequestData) {
  return request<Index.MessageSaveResponseData>({
    url: "/diagnosis/message/save",
    method: "post",
    data: data
  })
}

/**
 * 清空当前会话所有信息
 */
export function messageDeleteApi(sessionId: string) {
  return request<Index.MessageDeleteResponseData>({
    url: "/diagnosis/message/delete",
    method: "get",
    params: { sessionId: sessionId}
  })
}

/**
 * 清空当前会话所有信息
 */
export function sessionDeleteApi(data: SessionDeleteRequestData) {
  return request<Index.SessionDeleteResponseData>({
    url: "/diagnosis/session/delete",
    method: "post",
    data: data
  })
}
