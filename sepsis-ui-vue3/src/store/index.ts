/**
 * @author hzq
 * @date 2024/11/7 16:13
 * @apiNote Pinia 是一个用于 Vue 3 的状态管理库，是 Vuex 的官方替代品。
 * 1. 设计理念和架构
 * a) Vuex 的设计理念来自 Vue 2.x，它使用了 Flux 架构（单向数据流）来管理状态。
 *    Vuex 使用 state、mutations、actions 和 getters 来组织和管理状态。
 *      state 存储应用的所有状态.
 *      mutations 负责同步修改 state。
 *      actions 负责处理异步逻辑并调用 mutations。
 *      getters 类似于计算属性，用于从 state 计算出派生的状态。
 *    Vuex 提供了将 store 分为多个模块的功能。每个模块拥有独立的 state、mutations、actions、getters
 * b) Pinia 是为 Vue 3 设计的，完全支持 Composition API，其设计更符合 Vue 3 的现代开发理念。
 *    Pinia 通过 state、getters 和 actions 来组织状态，并不使用传统的 mutations。
 *      state 存储状态，直接通过 ref 或 reactive 实现响应式。
 *      getters 用来获取派生状态，和 Vue 3 的计算属性类似。
 *      actions 用于执行逻辑和修改 state（不需要通过 mutations），允许同步和异步操作。
 *    Pinia 对模块化进行了优化，允许每个 store 是独立的，采用扁平化处理。每个 store 都是一个独立的模块。
 * 2. API 风格
 * a) Vuex 使用的是 Options API 风格。
 * b) Pinia 完全支持 Composition API 风格，响应式的 state 通过 ref 或 reactive 管理。
 * 3. TypeScript 支持
 * a) Vuex 的 TypeScript 支持较弱，虽然有类型声明，但使用时通常需要手动为 state、mutations、actions 等做类型推导或声明
 * b) Pinia 从一开始就设计了对 TypeScript 的全面支持，使用 defineStore 创建 store 时，所有的 state、actions 和 getters 都能自动推导类型
 * 4. 性能优化
 * a) Vuex 的设计模式中，getters 是用于计算派生状态的，当计算结果发生变化时，getters 会被重新计算
 * b) Pinia 的状态是直接响应式的，不再需要通过 getters 进行派生计算，所有的计算都可以在 getters 中简洁表达，性能上有优化。
 **/
import { createPinia } from "pinia"

const store = createPinia()

export default store
