# Satellite Demo 

## Requirements

### Maps Messaging Components
- Maps Server configured to communicate with the satellite modem
- Maps Server configured to communicate with the REST API

### Communications Hardware (choose one)
- **Satellite Modem**
- **Satellite Simulator**
- **Run the Satellite Comms Server included with the package**

## Configuration

### Modem Side

Install the Maps server and then configure it to communicate with the modem.

In the **NetworkConnectionManager.yaml** add this to the **data** section.

```yaml

      - name: "ST2100 Modem"
        url: serial://localhost:0/
        protocol: stogi
        initialSetup: ""                         # optional init string sent at startup
        incomingMessagePollInterval: 1           # seconds, minimum = 1
        outgoingMessagePollInterval: 20          # seconds, minimum = 15 recommended
        sharedSecret: "This is a shared secret to use"
        sendHighPriorityMessages: false
        modemResponseTimeout: 20000              # ms
        locationPollInterval: 60                 # seconds, GNSS/visibility state updates
        maxBufferSize: 4000
        compressionCutoffSize: 128
        modemStatsTopic: "/modem/stats"
        modemRawMessages: "/incoming/{sin}/{min}"  # Any raw events will be published to incoming and split on sin/min
        bridgeMode: false                        # if true, send raw byte[] with no modifications

        remote:
          sessionId: <optional>                  # not required for this link
          username: <optional>
          password: <optional>

        links:
          - direction: push
            local_namespace: "/report/weather"
            remote_namespace: "/remote/report/weather"
            include_schema: true

        serial:
          port: "com6"
          baudRate: 9600
          dataBits: 8
          stopBits: 1
          parity: n
          flowControl: 0
```

Please not the **port** should match your communications port to use. Maps should autodetect the version of the modem to use.


To then configure the weather statistics events to be processed and sent add

```yaml
      - name: "Weather stats upload"
        url: loop://localhost:0/
        protocol: loop
        remote:
          sessionId: "internal sessionid"                  # not required for this link
          username: ""
          password: ""
        links:
          -
            direction: push
            local_namespace: "/weather"
            remote_namespace: "/report/weather"
            analystics:
              eventCount: 60
              ignoreList: "timestamp"
              defaultAnalyser: "Advanced"
```

This informs the server to subscribe to the **/weather** topic, process the events using the **Advanced** analyser and forward an event after every 60 events.

> A complete NetworkConnectionManager.yaml can be found in main/resources/modem_side


We also need to adjust which port the MQTT server binds to, in **NetworkManager.yaml**

```yaml
      - name: "MQTT Interface"
        url: tcp://:::1884/
        protocol: mqtt
        auth: default
        selectorThreadCount: "{processors}/2"
```

The port change from 1883 ( default ) to 1884 matches the Weather Station simulation.

> A complete NetworkManager.yaml can be found in the main/resources/modem_side 


Once configured start the server


### Weather Station Simulator

This package has a simple Weather Station simulator that 

- Sets the topics schemas
- Publish updates once a second to /weather

If the server is stopped the simulator will exit.

#### Starting Weather Station Simulator

You will need maven installed and then simply

```shell
mvn exec:java -Dexec.mainClass=io.mapsmessaging.weather.WeatherGeneratorDemo
```

It will connect to tcp://localhost:1884/, if you need another host/port simply change the code and run again

### Server Side

#### Modem Simulator

If running the modem simulator then we will need to configure the server to use the **ogws** API. 

In the NetworkManager.yaml add

```yaml
      - name: "ogws interface"
        protocol: satellite
        auth: anon
        bridgeMode: false
        url: satellite://localhost:8500/
        httpRequestTimeoutSec: 30
        pollInterval: 15
        sharedSecret: "This is a shared secret to use"
        maxInflightEventsPerDevice: 2
        outboundNamespaceRoot: "/inmarsat/{deviceId}"
        outboundBroadcast: "/inmarsat/broadcast"
        baseUrl: "http://localhost:8085/api/v1.0"
        remoteAuthConfig:
          username: "ogws client id"
          password: "ogws client secret"
        namespaceRoot: "/{deviceId}/"
```

#### Satellite Comms Emulation
If running the agent from this repo then we use the **Inmarsat IoT interface**

In the NetworkManager.yaml add

```yaml

      - name: "inmarsat IoT interface"
        auth: anon
        protocol: satellite
        url: satellite://localhost:8500/


        # Rest Server Config
        baseUrl: "http://localhost:8085/iotMessaging/v1"
        mailboxId: "mailbox-001"
        mailboxPassword: "mailbox-secret"

        remoteAuthConfig:
          username: "ogws client id"
          password: "ogws client secret"
        # Timeouts
        incomingMessagePollInterval: 10
        outgoingMessagePollInterval: 30
        httpRequestTimeoutSec: 30



        # Maps message package config
        bridgeMode: false
        sharedSecret: "This is a shared secret to use"
        maxInflightEventsPerDevice: 2

        # Namespace mapping
        outboundNamespaceRoot: "/inmarsat/{deviceId}"
        outboundBroadcast: "/inmarsat/broadcast"
        namespaceRoot: "/{mailboxId}/{deviceId}/{sin}"

```


#### Other
If running against a live server then please refer to your documentation regarding the type and authentication required.

