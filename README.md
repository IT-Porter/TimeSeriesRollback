# Group Time Series Data Rollback Solution

## Introduction

IoT health monitoring: All sensors' data was transferred to the server via MQ, each sensor’s data is time series. The server analysis each sensor's data live time to tell is healthy or not. The key is must keep each sensor's data strictly in time series. For some reason, there are some sensors' data records that may be out of order, then we have to roll back to the point and recalculate. Also, we need to consider some complex scenes, eg: some new sensors were added randomly while some stopped randomly, some sensors' data have long time intervals while some are high-frequency data...
This solution is exactly for such scenes.

## Main dependencies and components

| dependency      | version |
| --------------- | ------- |
| Spring Boot     | 2.3.7   |
| Spring Kafka    | 2.5.10  |
| Redission       | 3.15.6  |
| Spring Data Jpa | 2.3.7   |

| components | version  |
| ---------- | -------- |
| mysql      | 5.7+     |
| redis      | 5.0.7+   |
| kafka      | 3.2      |
| zookeeper  | 3.8      |
| docker     | 19.03.12 |

## Structure

**producer**  senor data emulator

**timeseriesrollback** the main project