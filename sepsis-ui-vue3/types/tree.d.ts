/**
 * @author hzq
 * @date 2024/11/8 9:35
 * @apiNote 所有 树形控件数据都遵守该格式
 **/
interface TreeData {
  key: string
  label: string
  children?: TreeData[]
}
