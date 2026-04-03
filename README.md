<div align="center">

# mqtt-plus-examples

**Standalone Spring Boot demo for mqtt-plus**

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://adoptium.net)
[![MQTT](https://img.shields.io/badge/MQTT-3.1.1-green.svg)](https://mqtt.org)

[English](#english) | [中文](#中文)

</div>

---

<a name="english"></a>

## English

### Why mqtt-plus-examples?

`mqtt-plus-examples` is a standalone Spring Boot sample that demonstrates how to build a multi-broker MQTT application with `mqtt-plus`. It uses a drone IoT monitoring scenario so the listener, publisher, interceptor, authentication, and runtime subscription refresh features feel like one coherent workflow instead of isolated snippets.

```java
@MqttListener(broker = "cloud", topics = "drone/+/status", payloadType = DroneStatus.class)
public void onStatus(DroneStatus status, MqttHeaders headers, @MqttTopic String topic) {
    log.info("[STATUS] topic={}, drone={}", topic, status.getSn());
}
```

### Current Scope

This README reflects the current `1.1.0-SNAPSHOT` demo scope:

- Included: Spring Boot app scaffold, dual Mosquitto broker setup, listeners, publishers, REST triggers, interceptor, connection listener, bilingual README
- Not included: frontend UI, database, CI pipeline, formal integration test suite

### Features

- `@MqttListener` with MQTT wildcard support (`+`, `#`)
- Dual broker configuration in one application
- Runtime subscription refresh through `MqttSubscriptionRefreshEvent`
- `MqttTemplate` sync and async publish with `qos` and `retained`
- JSON POJO payload binding with Jackson
- Global interceptor for inbound message logging
- Broker connection lifecycle logging
- Cloud broker authentication plus local broker anonymous access

### Quick Start

**1. Install local mqtt-plus snapshots**

Build the main repository first so the example project can resolve `1.1.0-SNAPSHOT`:

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus'
mvn install -DskipTests
```

**2. Start the brokers**

Generate the cloud broker password file:

```powershell
docker run --rm eclipse-mosquitto:2.0 sh -c "mosquitto_passwd -b -c /tmp/passwd admin mqtt123 && cat /tmp/passwd" > .\mosquitto\passwd
```

Start the containers:

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus-examples'
docker compose up -d
docker compose ps
```

**3. Run the demo app**

```powershell
mvn spring-boot:run
```

Expected startup behavior:

- Connect to `cloud`
- Connect to `local`
- Expose REST endpoints on `http://localhost:8080`

### Configuration

The app uses two brokers:

- `cloud` on port `1883` with username/password authentication
- `local` on port `1884` with anonymous access

Core config lives in:

- [application.yml](/D:/workspace/mygithub/mqtt-plus-examples/src/main/resources/application.yml)
- [docker-compose.yml](/D:/workspace/mygithub/mqtt-plus-examples/docker-compose.yml)

### REST API

**Demo endpoints**

| Endpoint | Purpose |
|------|------|
| `POST /api/demo/command?droneSn=DRONE001&action=land` | Publish a QoS 1 command |
| `POST /api/demo/alert?level=critical&source=battery&message=Battery%20below%2010%25` | Publish a retained alert |
| `POST /api/demo/stream?droneSn=DRONE001&url=rtmp://example.com/live` | Publish an async stream notification |

**Subscription refresh endpoints**

| Endpoint | Purpose |
|------|------|
| `POST /api/subscriptions/subscribe?droneSn=DRONE999` | Add a runtime subscription refresh event |
| `POST /api/subscriptions/unsubscribe?droneSn=DRONE999` | Remove a runtime subscription refresh event |

PowerShell examples:

```powershell
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/command?droneSn=DRONE001&action=land' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/alert?level=critical&source=battery&message=Battery%20below%2010%25' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/stream?droneSn=DRONE001&url=rtmp://example.com/live' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/subscriptions/subscribe?droneSn=DRONE999' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/subscriptions/unsubscribe?droneSn=DRONE999' | Select-Object -ExpandProperty Content
```

### MQTT Smoke Commands

**Drone status**

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/status" -m '{"sn":"DRONE001","latitude":30.5,"longitude":114.3,"altitude":120.0,"battery":85,"flightMode":"hover"}'
```

**Drone heartbeat**

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/heartbeat" -q 0 -m "alive"
```

**Verify retained alert**

Publish the alert first, then use a fresh subscriber:

```powershell
mosquitto_sub -h localhost -p 1883 -u admin -P mqtt123 -t "alert/critical/battery" -C 1 -v
```

**Multi-broker check**

```powershell
mosquitto_pub -h localhost -p 1884 -t "drone/LOCAL001/status" -m '{"sn":"LOCAL001","battery":99}'
```

### Project Map

| File / Class | Purpose |
|------|------|
| [MqttPlusExamplesApplication.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/MqttPlusExamplesApplication.java) | Spring Boot entry point |
| [DroneStatusListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneStatusListener.java) | JSON POJO status listener |
| [DroneHeartbeatListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneHeartbeatListener.java) | Heartbeat listener with `MqttHeaders` |
| [AlertListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/AlertListener.java) | Alert wildcard listener |
| [CrossBrokerListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/CrossBrokerListener.java) | Multi-broker shared listener |
| [CommandPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/CommandPublisher.java) | QoS 1 command publishing |
| [AlertPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/AlertPublisher.java) | Retained alert publishing |
| [StreamNotificationPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/StreamNotificationPublisher.java) | Async publish example |
| [MessageLoggingInterceptor.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/interceptor/MessageLoggingInterceptor.java) | Inbound interceptor |
| [ConnectionEventLogger.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/config/ConnectionEventLogger.java) | Broker connection events |
| [DemoController.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/controller/DemoController.java) | REST demo triggers |
| [SubscriptionController.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/controller/SubscriptionController.java) | Runtime subscription refresh |

### Notes

- The `cloud` broker uses `admin` / `mqtt123`
- Runtime subscription refresh updates the active adapter subscription set; it does not create new `@MqttListener` mappings at runtime
- The README assumes `mosquitto_pub` and `mosquitto_sub` are available on your machine

### Requirements

- Java 17+
- Maven 3.9+
- Docker Desktop or another local Docker engine

### License

Apache 2.0

---

<a name="中文"></a>

## 中文

### 为什么使用 mqtt-plus-examples？

`mqtt-plus-examples` 是一个独立的 Spring Boot 示例工程，用一个完整的无人机物联网监控场景来演示 `mqtt-plus` 的实际用法。它不是把监听、发布、拦截器、认证、动态订阅刷新拆成零散片段，而是把这些能力串成一个可以直接联调的 demo。

```java
@MqttListener(broker = "cloud", topics = "drone/+/status", payloadType = DroneStatus.class)
public void onStatus(DroneStatus status, MqttHeaders headers, @MqttTopic String topic) {
    log.info("[STATUS] topic={}, drone={}", topic, status.getSn());
}
```

### 当前范围

本 README 对应当前 `1.1.0-SNAPSHOT` 的 demo 范围：

- 已包含：Spring Boot 示例工程骨架、双 Mosquitto broker 配置、监听器、发布器、REST 触发接口、拦截器、连接事件监听器、双语 README
- 未包含：前端界面、数据库、CI 流水线、正式集成测试套件

### 核心能力

- 支持 MQTT 通配符（`+`、`#`）的 `@MqttListener`
- 单应用双 broker 配置
- 通过 `MqttSubscriptionRefreshEvent` 实现运行时订阅刷新
- `MqttTemplate` 同步 / 异步发布，支持 `qos` 和 `retained`
- 基于 Jackson 的 JSON POJO 反序列化
- 全局入站消息拦截器
- broker 连接生命周期日志
- cloud broker 认证与 local broker 匿名访问

### 快速开始

**1. 安装本地 mqtt-plus 快照**

先构建主仓库，让示例工程可以解析 `1.1.0-SNAPSHOT`：

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus'
mvn install -DskipTests
```

**2. 启动 broker**

先生成 cloud broker 的密码文件：

```powershell
docker run --rm eclipse-mosquitto:2.0 sh -c "mosquitto_passwd -b -c /tmp/passwd admin mqtt123 && cat /tmp/passwd" > .\mosquitto\passwd
```

再启动容器：

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus-examples'
docker compose up -d
docker compose ps
```

**3. 启动 demo 应用**

```powershell
mvn spring-boot:run
```

预期启动效果：

- 连接到 `cloud`
- 连接到 `local`
- 在 `http://localhost:8080` 暴露 REST 接口

### 配置说明

这个 demo 使用两个 broker：

- `cloud`：端口 `1883`，启用用户名密码认证
- `local`：端口 `1884`，允许匿名访问

核心配置文件：

- [application.yml](/D:/workspace/mygithub/mqtt-plus-examples/src/main/resources/application.yml)
- [docker-compose.yml](/D:/workspace/mygithub/mqtt-plus-examples/docker-compose.yml)

### REST 接口

**演示触发接口**

| 接口 | 说明 |
|------|------|
| `POST /api/demo/command?droneSn=DRONE001&action=land` | 发布 QoS 1 指令 |
| `POST /api/demo/alert?level=critical&source=battery&message=Battery%20below%2010%25` | 发布 retained 告警消息 |
| `POST /api/demo/stream?droneSn=DRONE001&url=rtmp://example.com/live` | 发布异步视频流通知 |

**订阅刷新接口**

| 接口 | 说明 |
|------|------|
| `POST /api/subscriptions/subscribe?droneSn=DRONE999` | 添加运行时订阅刷新事件 |
| `POST /api/subscriptions/unsubscribe?droneSn=DRONE999` | 移除运行时订阅刷新事件 |

PowerShell 调用示例：

```powershell
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/command?droneSn=DRONE001&action=land' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/alert?level=critical&source=battery&message=Battery%20below%2010%25' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/stream?droneSn=DRONE001&url=rtmp://example.com/live' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/subscriptions/subscribe?droneSn=DRONE999' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/subscriptions/unsubscribe?droneSn=DRONE999' | Select-Object -ExpandProperty Content
```

### MQTT 冒烟命令

**无人机状态消息**

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/status" -m '{"sn":"DRONE001","latitude":30.5,"longitude":114.3,"altitude":120.0,"battery":85,"flightMode":"hover"}'
```

**无人机心跳消息**

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/heartbeat" -q 0 -m "alive"
```

**验证 retained 告警**

先通过 REST 发布告警，再启动一个新的订阅者：

```powershell
mosquitto_sub -h localhost -p 1883 -u admin -P mqtt123 -t "alert/critical/battery" -C 1 -v
```

**多 broker 检查**

```powershell
mosquitto_pub -h localhost -p 1884 -t "drone/LOCAL001/status" -m '{"sn":"LOCAL001","battery":99}'
```

### 代码地图

| 文件 / 类 | 作用 |
|------|------|
| [MqttPlusExamplesApplication.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/MqttPlusExamplesApplication.java) | Spring Boot 入口 |
| [DroneStatusListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneStatusListener.java) | JSON POJO 状态监听 |
| [DroneHeartbeatListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneHeartbeatListener.java) | 使用 `MqttHeaders` 的心跳监听 |
| [AlertListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/AlertListener.java) | 告警通配监听 |
| [CrossBrokerListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/CrossBrokerListener.java) | 多 broker 共用监听 |
| [CommandPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/CommandPublisher.java) | QoS 1 指令发布 |
| [AlertPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/AlertPublisher.java) | retained 告警发布 |
| [StreamNotificationPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/StreamNotificationPublisher.java) | 异步发布示例 |
| [MessageLoggingInterceptor.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/interceptor/MessageLoggingInterceptor.java) | 入站拦截器 |
| [ConnectionEventLogger.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/config/ConnectionEventLogger.java) | broker 连接事件 |
| [DemoController.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/controller/DemoController.java) | REST 演示触发接口 |
| [SubscriptionController.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/controller/SubscriptionController.java) | 运行时订阅刷新接口 |

### 说明

- `cloud` broker 使用 `admin` / `mqtt123`
- 运行时订阅刷新只会更新当前活跃 adapter 的订阅集合，不会在运行时创建新的 `@MqttListener` 映射
- 本 README 默认你的机器上已经安装了 `mosquitto_pub` 和 `mosquitto_sub`

### 环境要求

- Java 17+
- Maven 3.9+
- Docker Desktop 或其他本地 Docker 引擎

### License

Apache 2.0
