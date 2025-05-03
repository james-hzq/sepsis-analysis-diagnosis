# 欢迎来到脓毒症智能分析与问询平台
### 简介
sepsis-analysis-diagnosis 是本人的毕业设计，属于可以本地运行的学习项目。项目基于mimic-iv临床医学数据集提取出脓毒症患者的数据，进行统计分析，并且接入gpt 4o大模型进行问询。前端基于开源项目 v3-admin-vite 进行二次开发，后端采用 Spring Cloud Alibaba、Spring Security OAuth2、Spring Data Jpa、LangChain4j 等技术栈构建微服务平台。
### 使用
1. 前置环境：JDK 17 +、Redis 5.0+、MySQL 8.0+、Nacos Server 2.2+、Node.js 16.0.0+

2. 克隆项目到本地

    + ```bash
        git clone https://github.com/james-hzq/sepsis-analysis-diagnosis.git
        ```

3. 启动前端

    + ```bash
        npm install pnpm
        ```

    + ```bash
        pnpm i
        ```

4. 运行sql目录下的nacos.sql、sepsis_mimic.sql文件，导入到本地数据库

5. 运行naocs server、并且通过浏览器打开nacos client，修改全局配置，包括mysql、redis、各个独立微服务的配置

6. 修改 sepsis-diagnosis-server 中 LangChain4jConfig 中的本地知识库路径：KNOWLEDGE_PATH 为自己的路径

7. 启动各个微服务即可
