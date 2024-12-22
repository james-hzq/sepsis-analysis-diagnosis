export interface SessionData {
  // 会话唯一ID
  id: string
  // 会话创建人
  user: string
  // 会话标题
  title: string
  // 会话创建时间
  createTime: string
}

export interface SendMessageRequestData {
  // 当前信息所属会话
  sessionId: string
  // 当前信息内容
  content: string | ''
}

export interface MessageSaveRequestData {
  // 信息所属会话唯一ID
  sessionId: string
  // 用户发送信息内容
  userContent: string
  // AI发送信息内容
  aiContent: string
}

export interface SessionDeleteRequestData {
  // 会话ID集合
  sessionIds: string[]
}

export type SessionTreeResponseData = ApiResponseData<TreeData[]>
export type SessionResponseData = ApiResponseData<SessionData>
export type MessageListResponseData = ApiResponseData<MessageData[]>
export type MessageSendResponseData = ApiResponseData<string>
export type MessageSaveResponseData = ApiResponseData<void>
export type MessageDeleteResponseData = ApiResponseData<void>
export type SessionDeleteResponseData = ApiResponseData<void>
