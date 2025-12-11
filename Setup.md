# Viasat Demo Environment Setup

This document provides a clear overview of the demo environment used to simulate end-to-end satellite messaging using the Maps Messaging Server and the Viasat modem simulator. It is written for readers familiar with satellite communications but not yet familiar with Maps or its messaging architecture.

## Overview

The demo environment models a typical satellite communication workflow:

- A **Web-Side Maps Server**, connected to the Viasat Web Service.
- A **Modem-Side Maps Server**, connected to a simulated satellite modem.
- A **Common Event Request Agent**, publishing requests via MQTT to the Web-Side server.
- A **Common Event Response Agent**, subscribing via MQTT to the Modem-Side server.
- A **Weather Station Simulator**, generating weather telemetry once per second.
- A **Viasat Modem Simulator**, providing the satellite link between the two Maps Servers.

The goal is to demonstrate how telemetry and request/response flows behave when routed through a satellite network, including periodic summary data produced on the modem side.

## Components

### 1. Maps Server (Web Service Side)

The Maps Server on the web side communicates with the Viasat Web Service endpoint.  
It receives Common Event Requests via MQTT and forwards them into the simulated satellite network.

### 2. Maps Server (Modem Side)

The modem-side Maps Server communicates with the Viasat modem simulator.  
It performs two primary functions:

1. **Route inbound messages from the satellite link** to the response agent.
2. **Process incoming weather data**, compute statistical summaries, and publish the results once per minute.

### 3. Common Event Request Agent (MQTT)

This agent connects to the web-side Maps Server and sends request messages through the simulated satellite network.

### 4. Common Event Response Agent (MQTT)

This agent connects to the modem-side Maps Server and receives responses or modem-side generated events.

### 5. Weather Station Simulator

The weather simulator publishes one telemetry message per second.  
Each message includes typical atmospheric fields such as:

- Pressure (kPa)
- Temperature (°C)
- Humidity (%)
- Wind speed and direction
- Light intensity
- Rainfall

### 6. Viasat Modem Simulator

Acts as the transport layer between the Maps Servers, simulating Viasat modem behavior.

## Message Flow Summary

### Weather Telemetry Path

Weather Simulator → Modem-Side Maps Server → (1/minute) Weather Statistics

### Request/Response Path

Request Agent → Web-Side Maps Server → Modem Simulator → Modem-Side Maps Server → Response Agent

## Purpose of the Demo

This setup demonstrates:

- How Maps Messaging handles satellite-style links
- How request/response flows operate across a modem path
- How periodic telemetry aggregation works on the modem side
- How MQTT agents operate on both edges of the satellite network
- Full end-to-end visibility of message routing through the simulated environment
