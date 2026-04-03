# mqtt-plus-examples

`mqtt-plus-examples` is a standalone Spring Boot demo project for [`mqtt-plus`](https://github.com/mqttplus/mqtt-plus), built around a drone IoT monitoring scenario.

`mqtt-plus-examples` 是一个独立的 Spring Boot 示例工程，围绕无人机物联网监控场景来演示 [`mqtt-plus`](https://github.com/mqttplus/mqtt-plus) 的核心能力。

## Feature Map / 功能映射

| # | Feature | 功能 | Demo Entry |
|---|---|---|---|
| 1 | QoS levels | QoS 等级 | [DroneHeartbeatListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneHeartbeatListener.java), [CommandPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/CommandPublisher.java) |
| 2 | Retained messages | 保留消息 | [AlertPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/AlertPublisher.java) |
| 3 | Async publish | 异步发布 | [StreamNotificationPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/StreamNotificationPublisher.java) |
| 4 | JSON POJO deserialization | JSON 反序列化为 POJO | [DroneStatusListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneStatusListener.java), [DroneStatus.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/model/DroneStatus.java) |
| 5 | MQTT wildcards | MQTT 通配符 | [DroneStatusListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneStatusListener.java), [AlertListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/AlertListener.java) |
| 6 | Interceptor chain | 拦截器链 | [MessageLoggingInterceptor.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/interceptor/MessageLoggingInterceptor.java) |
| 7 | Connection event listener | 连接事件监听 | [ConnectionEventLogger.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/config/ConnectionEventLogger.java) |
| 8 | Multi-broker listener | 多 broker 监听 | [CrossBrokerListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/CrossBrokerListener.java) |
| 9 | Dynamic subscription refresh | 动态订阅刷新 | [SubscriptionController.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/controller/SubscriptionController.java) |
| 10 | Authentication | 认证鉴权 | [application.yml](/D:/workspace/mygithub/mqtt-plus-examples/src/main/resources/application.yml), [docker-compose.yml](/D:/workspace/mygithub/mqtt-plus-examples/docker-compose.yml) |
| 11 | `MqttHeaders` usage | `MqttHeaders` 使用 | [DroneHeartbeatListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneHeartbeatListener.java) |

## Scenario / 场景说明

The demo models a drone platform with these topic conventions:

这个 demo 使用以下 topic 约定来模拟无人机平台：

| Topic | Meaning | 含义 |
|---|---|---|
| `drone/{sn}/status` | Drone status report | 无人机状态上报 |
| `drone/{sn}/heartbeat` | Drone heartbeat | 无人机心跳 |
| `drone/{sn}/command` | Platform sends a command | 平台下发指令 |
| `drone/{sn}/stream` | Platform announces stream info | 平台发布视频流信息 |
| `alert/{level}/{source}` | Platform alert broadcast | 平台告警广播 |

It uses two brokers:

项目使用两个 broker：

- `cloud` on `1883`, with username/password authentication
- `local` on `1884`, anonymous access
- `cloud` 使用 `1883` 端口，启用用户名密码认证
- `local` 使用 `1884` 端口，允许匿名访问

## Prerequisites / 前置条件

- Java 17+
- Maven 3.9+
- Docker Desktop or another local Docker engine
- `mqtt-plus` main project built and installed locally as `1.1.0-SNAPSHOT`
- 已在本地构建并安装 `mqtt-plus` 主工程的 `1.1.0-SNAPSHOT`

Install the local snapshots first from the main repo:

先在主仓库中安装本地快照依赖：

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus'
mvn install -DskipTests
```

## Quick Start / 快速开始

### 1. Generate the cloud broker password file / 生成 cloud broker 密码文件

```powershell
docker run --rm eclipse-mosquitto:2.0 sh -c "mosquitto_passwd -b -c /tmp/passwd admin mqtt123 && cat /tmp/passwd" > .\mosquitto\passwd
```

### 2. Start the brokers / 启动 broker

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus-examples'
docker compose up -d
docker compose ps
```

### 3. Start the demo application / 启动 demo 应用

```powershell
mvn spring-boot:run
```

Expected startup behavior:

预期启动效果：

- connect to `cloud`
- connect to `local`
- expose REST endpoints on `http://localhost:8080`
- 连接到 `cloud`
- 连接到 `local`
- 在 `http://localhost:8080` 暴露 REST 接口

## REST API / REST 接口

### Demo triggers / 演示触发接口

| Endpoint | Purpose | 说明 |
|---|---|---|
| `POST /api/demo/command?droneSn=DRONE001&action=land` | Publish a QoS 1 command | 发布 QoS 1 指令 |
| `POST /api/demo/alert?level=critical&source=battery&message=Battery%20below%2010%25` | Publish a retained alert | 发布 retained 告警消息 |
| `POST /api/demo/stream?droneSn=DRONE001&url=rtmp://example.com/live` | Publish an async stream notification | 发布异步视频流通知 |

### Subscription refresh / 订阅刷新接口

| Endpoint | Purpose | 说明 |
|---|---|---|
| `POST /api/subscriptions/subscribe?droneSn=DRONE999` | Add a runtime subscription refresh event | 添加运行时订阅刷新事件 |
| `POST /api/subscriptions/unsubscribe?droneSn=DRONE999` | Remove a runtime subscription refresh event | 移除运行时订阅刷新事件 |

PowerShell examples:

PowerShell 调用示例：

```powershell
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/command?droneSn=DRONE001&action=land' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/alert?level=critical&source=battery&message=Battery%20below%2010%25' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/demo/stream?droneSn=DRONE001&url=rtmp://example.com/live' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/subscriptions/subscribe?droneSn=DRONE999' | Select-Object -ExpandProperty Content
Invoke-WebRequest -Method Post 'http://localhost:8080/api/subscriptions/unsubscribe?droneSn=DRONE999' | Select-Object -ExpandProperty Content
```

## MQTT Smoke Commands / MQTT 冒烟命令

### Drone status JSON / 无人机状态 JSON

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/status" -m '{"sn":"DRONE001","latitude":30.5,"longitude":114.3,"altitude":120.0,"battery":85,"flightMode":"hover"}'
```

### Drone heartbeat / 无人机心跳

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/heartbeat" -q 0 -m "alive"
```

### Verify retained alert / 验证 retained 告警

Publish the alert first through REST, then start a fresh subscriber:

先通过 REST 发布告警，再启动一个新的订阅者：

```powershell
mosquitto_sub -h localhost -p 1883 -u admin -P mqtt123 -t "alert/critical/battery" -C 1 -v
```

### Multi-broker check / 多 broker 检查

```powershell
mosquitto_pub -h localhost -p 1884 -t "drone/LOCAL001/status" -m '{"sn":"LOCAL001","battery":99}'
```

## Important Notes / 重要说明

- The `cloud` broker uses `admin` / `mqtt123`.
- Feature #9 demonstrates runtime subscription refresh through `MqttSubscriptionRefreshEvent`.
- Feature #9 does not mean runtime creation of a new `@MqttListener` mapping.
- The README assumes `mosquitto_pub` and `mosquitto_sub` are available on your machine.
- `cloud` broker 使用 `admin` / `mqtt123`。
- 功能 #9 演示的是通过 `MqttSubscriptionRefreshEvent` 进行运行时订阅刷新。
- 功能 #9 不代表运行时动态创建新的 `@MqttListener` 映射。
- 本 README 默认你的机器上已经安装了 `mosquitto_pub` 和 `mosquitto_sub`。

## Project Structure / 项目结构

```text
mqtt-plus-examples/
|- docker-compose.yml
|- mosquitto/
|- pom.xml
|- src/main/java/io/github/mqttplus/examples/
|  |- config/
|  |- controller/
|  |- interceptor/
|  |- listener/
|  |- model/
|  |- publisher/
|  `- MqttPlusExamplesApplication.java
`- src/main/resources/application.yml
```

## Related Repos / 相关仓库

- Main project / 主项目: [mqtt-plus](https://github.com/mqttplus/mqtt-plus)
- Demo app entry / Demo 入口: [MqttPlusExamplesApplication.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/MqttPlusExamplesApplication.java)
