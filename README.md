# mqtt-plus-examples

`mqtt-plus-examples` is a standalone Spring Boot demo project for [`mqtt-plus`](https://github.com/mqttplus/mqtt-plus), built around a drone IoT monitoring scenario.

A standalone Spring Boot sample app for demonstrating `mqtt-plus` features through a drone IoT monitoring workflow.

## Feature Map

| # | Feature | Demo Entry |
|---|---|---|
| 1 | QoS levels | [DroneHeartbeatListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneHeartbeatListener.java), [CommandPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/CommandPublisher.java) |
| 2 | Retained messages | [AlertPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/AlertPublisher.java) |
| 3 | Async publish | [StreamNotificationPublisher.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/publisher/StreamNotificationPublisher.java) |
| 4 | JSON POJO deserialization | [DroneStatusListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneStatusListener.java), [DroneStatus.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/model/DroneStatus.java) |
| 5 | MQTT wildcards | [DroneStatusListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneStatusListener.java), [AlertListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/AlertListener.java) |
| 6 | Interceptor chain | [MessageLoggingInterceptor.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/interceptor/MessageLoggingInterceptor.java) |
| 7 | Connection event listener | [ConnectionEventLogger.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/config/ConnectionEventLogger.java) |
| 8 | Multi-broker listener | [CrossBrokerListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/CrossBrokerListener.java) |
| 9 | Dynamic subscription refresh | [SubscriptionController.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/controller/SubscriptionController.java) |
| 10 | Authentication | [application.yml](/D:/workspace/mygithub/mqtt-plus-examples/src/main/resources/application.yml), [docker-compose.yml](/D:/workspace/mygithub/mqtt-plus-examples/docker-compose.yml) |
| 11 | `MqttHeaders` usage | [DroneHeartbeatListener.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/listener/DroneHeartbeatListener.java) |

## Scenario

The demo models a drone platform with these topic conventions:

| Topic | Meaning |
|---|---|
| `drone/{sn}/status` | Drone status report |
| `drone/{sn}/heartbeat` | Drone heartbeat |
| `drone/{sn}/command` | Platform sends a command |
| `drone/{sn}/stream` | Platform announces stream info |
| `alert/{level}/{source}` | Platform alert broadcast |

It uses two brokers:

- `cloud` on `1883`, with username/password authentication
- `local` on `1884`, anonymous access

## Prerequisites

- Java 17+
- Maven 3.9+
- Docker Desktop or another local Docker engine
- `mqtt-plus` main project built and installed locally as `1.1.0-SNAPSHOT`

Install the local snapshots first from the main repo:

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus'
mvn install -DskipTests
```

## Quick Start

### 1. Generate the cloud broker password file

```powershell
docker run --rm eclipse-mosquitto:2.0 sh -c "mosquitto_passwd -b -c /tmp/passwd admin mqtt123 && cat /tmp/passwd" > .\mosquitto\passwd
```

### 2. Start the brokers

```powershell
Set-Location 'D:/workspace/mygithub/mqtt-plus-examples'
docker compose up -d
docker compose ps
```

### 3. Start the demo application

```powershell
mvn spring-boot:run
```

Expected startup behavior:

- connect to `cloud`
- connect to `local`
- expose REST endpoints on `http://localhost:8080`

## REST API

### Demo triggers

| Endpoint | Purpose |
|---|---|
| `POST /api/demo/command?droneSn=DRONE001&action=land` | Publish a QoS 1 command |
| `POST /api/demo/alert?level=critical&source=battery&message=Battery%20below%2010%25` | Publish a retained alert |
| `POST /api/demo/stream?droneSn=DRONE001&url=rtmp://example.com/live` | Publish an async stream notification |

### Subscription refresh

| Endpoint | Purpose |
|---|---|
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

## MQTT Smoke Commands

### Drone status JSON

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/status" -m '{"sn":"DRONE001","latitude":30.5,"longitude":114.3,"altitude":120.0,"battery":85,"flightMode":"hover"}'
```

### Drone heartbeat

```powershell
mosquitto_pub -h localhost -p 1883 -u admin -P mqtt123 -t "drone/DRONE001/heartbeat" -q 0 -m "alive"
```

### Verify retained alert

Publish the alert first through REST, then start a fresh subscriber:

```powershell
mosquitto_sub -h localhost -p 1883 -u admin -P mqtt123 -t "alert/critical/battery" -C 1 -v
```

### Multi-broker check

```powershell
mosquitto_pub -h localhost -p 1884 -t "drone/LOCAL001/status" -m '{"sn":"LOCAL001","battery":99}'
```

## Important Notes

- The `cloud` broker uses `admin` / `mqtt123`.
- Feature #9 demonstrates runtime subscription refresh through `MqttSubscriptionRefreshEvent`.
- Feature #9 does not mean runtime creation of a new `@MqttListener` mapping.
- The README assumes `mosquitto_pub` and `mosquitto_sub` are available on your machine.

## Project Structure

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

## Related Repos

- Main project: [mqtt-plus](https://github.com/mqttplus/mqtt-plus)
- Demo app entry: [MqttPlusExamplesApplication.java](/D:/workspace/mygithub/mqtt-plus-examples/src/main/java/io/github/mqttplus/examples/MqttPlusExamplesApplication.java)
