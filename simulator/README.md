# Dock and Vehicle Simulator

Kotlin/Java application with a UI to simulate sensors/actuators and interact with MQTT and the DB.

## Contents
- [Prerequisites](#prerequisites)
- [Build](#build)
- [Run](#run)

## Prerequisites
- JDK 17+
- Maven 3.9+
- Running MySQL with schema `mobishare` and the credentials used by the core (`core_backend`/`core_backend`)
- MQTT broker reachable at `tcp://localhost:1883` (default configuration in code)

## Build
```bash
cd simulator
mvn -q -DskipTests package
```
Generates a fat-jar in `target/simulator-0.1-fat.jar`.

## Run
```bash
java -jar target/simulator-0.1-fat.jar
```
On startup it connects to MySQL and MQTT, then opens the GUI window.
