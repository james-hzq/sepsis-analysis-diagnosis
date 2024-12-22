<script setup lang="ts">
import {computed, nextTick, onMounted, onUnmounted, ref, watch} from "vue";
import {SessionData} from "@/api/diagnosis/types";
import {useUserStore} from "@/store/modules/user";
import {
  messageDeleteApi,
  messageListApi,
  messageSaveApi,
  messageSendApi,
  sessionCreateApi,
  sessionDeleteApi,
  sessionLastApi,
  sessionTreeApi
} from "@/api/diagnosis";
import messageUtils from '@/utils/modal'

// 引入用户 Pinia Store
const userStore = useUserStore();
const user = computed(() => userStore.username);

// 新增会话Dialog
const createSessionDialogVisible = ref(false)
const createSessionTitle = ref('')

/**
 * 新增会话
 */
const createNewSession = () => {
  createSessionDialogVisible.value = true
}

/**
 * 新增会话dialog确认
 */
const submitCreateSession = () => {
  sessionCreateApi(user.value, createSessionTitle.value)
    .then(res => {
      currSession.value = res.data
      getCurrMessageList(currSession.value.id)
      getSessionTree()
    })
    .finally(() => {
      createSessionDialogVisible.value = false;
      createSessionTitle.value = ''
    })
}

/**
 * 新增会话dialog取消
 */
const cancelCreateSession = () => {
  createSessionDialogVisible.value = false
  createSessionTitle.value = ''
}

// 历史对话树定义和查询
const historyChatTreeData = ref<TreeData[]>([])
const historyChatTreeProps = {
  key: 'key',
  children: 'children',
  label: 'label',
}
// 用于追踪选中树的节点
const sessionTreeRef = ref(null)

/**
 * 获取历史对话树
 */
const getSessionTree = () => {
  sessionTreeApi(user.value).then(res => {
    historyChatTreeData.value = [res.data]
  })
}

/**
 * 点击子节点事件
 */
const handleNodeClick = (node) => {
  if (node.key === 'root') return
  getCurrMessageList(node.key)
}

// 用户当前会话定义
const currSession = ref<SessionData>(null)
const currMessageList = ref<MessageData[]>([])
// 这是DOM引用，用于操作消息列表容器的滚动
const messageListRef = ref<HTMLElement | null>(null)

// 监听当前消息列表变化，实现滚动
watch(() => currMessageList.value, (newVal) => {
  if (newVal && newVal.length > 0) {
    nextTick(() => {
      scrollToBottom(true)
    })
  }
}, {deep: true})

const scrollToBottom = (force: boolean = false) => {
  if (!messageListRef.value) return

  const scrollElement = messageListRef.value
  if (force || (scrollElement.scrollHeight - scrollElement.scrollTop - scrollElement.clientHeight < 100)) {
    scrollElement.scrollTop = scrollElement.scrollHeight
  }
}

// 调整消息列表高度方法
const adjustMessageListHeight = () => {
  if (messageListRef.value) {
    const windowHeight = window.innerHeight
    const headerHeight = 50 // 标题高度
    const inputHeight = 70 // 输入区域高度
    const padding = 40 // 内边距总和
    const maxHeight = windowHeight - headerHeight - inputHeight - padding
    messageListRef.value.style.maxHeight = `${maxHeight}px`
    messageListRef.value.style.height = `${maxHeight}px`
  }
}

/**
 * 获取用户当前信息内容
 */
const getCurrMessageList = (sessionId: string) => {
  messageListApi(sessionId).then(res => {
    if (!res || !res.data) currMessageList.value = []
    else currMessageList.value = res.data
  })
}

// 当前用户信息
const currUserMessage = ref<string>('')
// 当前AI信息
const currAiMessage = ref<string>('')
/**
 * 发送信息
 */
const sendMessage = () => {
  if (!currUserMessage.value.trim()) return
  messageSendApi(currUserMessage.value).then(res => {
    currAiMessage.value = res.data
    messageSaveApi({
      sessionId: currSession.value.id,
      userContent: currUserMessage.value,
      aiContent: currAiMessage.value
    }).then(() => {
      getCurrMessageList(currSession.value.id)
    }).finally(() => {
      currUserMessage.value = ''
      currAiMessage.value = ''
    })
  })
}

/**
 * 清空当前会话信息
 */
const clearSessionDialogVisible = ref(false)
const clearCurrSession = () => {
  clearSessionDialogVisible.value = true
}

/**
 * 清空当前会话dialog确认
 */
const submitClearSession = () => {
  messageDeleteApi(currSession.value.id).then(res => {
    getCurrMessageList(currSession.value.id)
  }).finally(() => {
    clearSessionDialogVisible.value = false;
  })
}

/**
 * 新增会话dialog取消
 */
const cancelClearSession = () => {
  clearSessionDialogVisible.value = false
}

/**
 * 删除选中会话
 */
const deleteSessionDialogVisible = ref(false)
const deleteSelectSession = () => {
  deleteSessionDialogVisible.value = true
}

/**
 * 删除选中会话dialog确认
 */
const submitDeleteSession = () => {
  if (!sessionTreeRef.value) return
  const nodes = sessionTreeRef.value.getCheckedNodes()
  const sessionIds = nodes.map(item => item.key).filter(key => key !== 'root')
  if (!sessionIds || sessionIds.length === 0) {
    messageUtils.msgWarning('请先选择要删除的节点');
    return;
  }
  sessionDeleteApi({sessionIds: sessionIds}).then(res => {
    sessionLastApi(user.value).then(res => {
      getCurrMessageList(currSession.value.id)
      getSessionTree()
    })
  }).finally(() => {
    deleteSessionDialogVisible.value = false;
  })
}

/**
 * 删除会话dialog取消
 */
const cancelDeleteSession = () => {
  deleteSessionDialogVisible.value = false
}

onMounted(async () => {
  // 获取该用户上一次最晚创建的 session
  const [lastSessionResData] = await Promise.all([
    sessionLastApi(user.value)
  ])
  currSession.value = lastSessionResData.data
  // 获取该用户上一次最晚创建的 session 的所有信息
  getCurrMessageList(currSession.value.id)
  // 获取历史对话
  getSessionTree()
  // 初始化消息列表高度
  adjustMessageListHeight()
  // 添加窗口大小变化监听
  window.addEventListener('resize', adjustMessageListHeight)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('resize', adjustMessageListHeight)
})
</script>

<template>
  <div class="app-container">
    <div class="chat-container">
      <!-- 聊天室侧边栏 -->
      <div class="chat-sidebar">
        <el-button type="primary" @click="createNewSession">创建新的对话</el-button>
        <el-button type="warning" @click="clearCurrSession">清空当前对话</el-button>
        <el-button type="danger" @click="deleteSelectSession">删除选中对话</el-button>
        <el-tree ref="sessionTreeRef"
                 :data="historyChatTreeData"
                 :props="historyChatTreeProps"
                 node-key="key"
                 @node-click="handleNodeClick"
                 show-checkbox
                 highlight-current
        />
      </div>

      <!-- 聊天室正文 -->
      <div class="chat-context">
        <!-- 聊天内容区域 -->
        <div class="chart-message-list" ref="messageListRef">
          <div class="chat-title">
            <span>{{ currSession?.title || '新对话' }}</span>
          </div>
          <div v-for="(msg, index) in currMessageList" :key="index" class="message-item">
            <div :class="['message', msg.userType == 0 ? 'message-ai' : 'message-user']">
              <img v-if="msg.userType == 0" src="@/icons/svg/chatgpt.svg" class="chat-icon" alt="AI Icon"/>
              <img v-else src="@/icons/svg/user.svg" class="chat-icon" alt="User Icon"/>

              <div class="message-content">
                <strong>{{ msg.username }}</strong>
                <span>{{ msg.content }}</span>
              </div>
            </div>
          </div>
        </div>
        <!-- 聊天发送区域 -->
        <div class="chat-input-container">
          <el-input class="chat-input" v-model="currUserMessage" placeholder="输入消息"/>
          <el-button class="send-button" type="primary" icon="Position" @click="sendMessage">发送</el-button>
        </div>
      </div>
    </div>

    <div class="create-session-dialog">
      <el-dialog title="新建会话" v-model="createSessionDialogVisible" width="25%" center>
        <el-input placeholder="请输入会话标题" v-model="createSessionTitle" type="text" clearable/>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="warning" plain size="default" @click="cancelCreateSession">取消</el-button>
            <el-button type="primary" plain size="default" @click="submitCreateSession">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>

    <div class="create-session-dialog">
      <el-dialog title="清空当前会话" v-model="clearSessionDialogVisible" width="25%" center>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="warning" plain size="default" @click="cancelClearSession">取消</el-button>
            <el-button type="primary" plain size="default" @click="submitClearSession">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>

    <div class="create-session-dialog">
      <el-dialog title="删除选中会话" v-model="deleteSessionDialogVisible" width="25%" center>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="warning" plain size="default" @click="cancelDeleteSession">取消</el-button>
            <el-button type="primary" plain size="default" @click="submitDeleteSession">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<style scoped lang="scss">
.chat-container {
  display: flex;
  height: 100%;

  .chat-sidebar {
    width: 200px;
    display: flex;
    flex-direction: column;
    background-color: white;
    padding: 15px;

    .el-button {
      width: 100%;
      margin: 0 0 20px 0;
      text-align: center;
    }

    .el-tree {
      width: 200px;
    }
  }

  .chat-context {
    display: flex;
    flex-grow: 1;
    flex-direction: column;
    padding: 20px;
    background-color: #fff;
    height: 100%;
    overflow: hidden;

    .chat-title {
      padding: 10px 0;
      font-weight: bold;
      font-size: 16px;
      border-bottom: 1px solid #eee;
      margin-bottom: 15px;
    }

    .chart-message-list {
      flex-grow: 1;
      height: 500px;
      overflow-y: auto;
      padding-right: 10px;
      scroll-behavior: smooth;
    }

    .message {
      display: flex;
      align-items: center;
      gap: 12px; /* 控制图标与内容之间的间距 */
      width: 100%; /* 确保消息项充满父容器 */
    }

    .message-content {
      display: flex;
      flex-direction: column;
      gap: 4px; /* 控制文字上下行间距 */
      line-height: 1.5; /* 增加文字的可读性 */
      flex-grow: 1; /* 让内容部分占据剩余空间 */
      word-wrap: break-word; /* 自动换行以适应宽度 */
    }

    .message-user {
      background-color: #5e72e4;
      color: white;
      border-radius: 12px;
      padding: 8px 12px;
    }

    .message-ai {
      background-color: #f4f4f5;
      color: #333;
      border-radius: 12px;
      padding: 8px 12px;
    }

    .message-item {
      display: flex;
      align-items: flex-start; /* 图标与内容顶部对齐 */
      margin-bottom: 12px; /* 消息之间的间距 */
    }

    .chat-input-container {
      display: flex;
      align-items: center;
      padding: 10px;
      background-color: #f9f9f9;
      border-top: 1px solid #ddd;
      gap: 10px;
    }

    .chat-input {
      flex-grow: 1;
    }

    .chat-send-button {
      flex-shrink: 0;
      min-width: 80px;
    }
  }
}

.chat-icon {
  width: 20px;
  height: 20px;
  margin-right: 8px;
  font-size: 18px;
  color: #666;

  &.ai {
    color: #5e72e4;
  }

  &.user {
    color: #f4a261;
  }
}
</style>
