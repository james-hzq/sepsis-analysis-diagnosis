import {info} from "sass";
import {ElMessage, ElMessageBox, ElNotification, ElLoading} from "element-plus";

export interface MessageOptions {
  duration?: number
  showClose?: boolean
  center?: boolean
}

export interface NotificationOptions extends MessageOptions {
  position?: 'top-right' | 'top-left' | 'bottom-right' | 'bottom-left'
}

class MessageUtils {
  private loadingInstance: any;

  msgInfo(content: string): void {
    ElMessage.info(content)
  }

  msgSuccess(content: string): void {
    ElMessage.success(content)
  }

  msgWarning(content: string): void {
    ElMessage.warning(content)
  }

  msgError(content: string): void {
    ElMessage.error(content)
  }

  alertInfo(content: string): void {
    ElMessageBox.alert(content, "系统提示", {type: info})
  }

  alertSuccess(content: string): void {
    ElMessageBox.alert(content, "系统提示", {type: 'success'})
  }

  alertWarning(content: string): void {
    ElMessageBox.alert(content, "系统提示", {type: 'warning'})
  }

  alertError(content: string): void {
    ElMessageBox.alert(content, "系统提示", {type: 'error'})
  }

  notifyInfo(content: string, position?: NotificationOptions): void {
    ElNotification.info({
      message: content,
      position
    })
  }

  notifySuccess(content: string, position?: NotificationOptions): void {
    ElNotification.success({
      message: content,
      position
    })
  }

  notifyWarning(content: string, position?: NotificationOptions): void {
    ElNotification.warning({
      message: content,
      position
    })
  }

  notifyError(content: string, position?: NotificationOptions): void {
    ElNotification.error({
      message: content,
      position
    })
  }

  confirm(content: string): Promise<any> {
    return ElMessageBox.confirm(content, "系统提示", {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: "warning",
    })
  }

  prompt(content: string): Promise<any> {
    return ElMessageBox.prompt(content, "系统提示", {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: "warning",
    })
  }

  loading(content: string = '加载中...'): void {
    this.loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    })
  }

  closeLoading(): void {
    if (this.loadingInstance) {
      this.loadingInstance.close();
    }
  }
}

export default new MessageUtils()
