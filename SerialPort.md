# Serial Port configuration

To enable the Maps server to communicate with the modem or the modem simulator you will need either a cross over cable or use tools like socat to create a software verions.


## Serial Crossover Cable (TX/RX Only)

A simple serial crossover cable swaps the transmit and receive lines between two serial devices so they can communicate directly without modems or additional hardware flow control.

### Purpose

The goal is to connect two DTE-style devices (for example, two computers or a computer and a microcontroller) so that the outgoing data on one side is received by the other.

### Pinout (TX/RX Only)

This describes the minimal 3-wire crossover using **GND**, **TX**, and **RX**.

### DB9 (Female–Female) Crossover
| Device A Pin | Signal | Device B Pin |
|--------------|--------|--------------|
| 2            | RX     | 3            |
| 3            | TX     | 2            |
| 5            | GND    | 5            |

#### Notes
- No CTS, RTS, DSR, DTR, or other handshaking lines are required.
- Works for TTL serial only if voltage levels match (RS-232 voltage levels are not TTL-safe).
- Cable length: keep it under a few meters to avoid noise issues.

### Quick Diagram
Device A TX (3) ---> (2) RX Device B  
Device A RX (2) <--- (3) TX Device B  
Device A GND (5) --- (5) GND Device B

### Testing
- Loopback test: short TX to RX on one side to verify device output.
- Use a terminal at 9600-8-N-1 (or whatever your devices expect).


## Virtual Serial Crossover Cable Using socat (Linux)

This creates a pair of virtual serial ports on Linux that behave like a null-modem crossover cable.

### Command

socat -d -d \
PTY,link=/dev/tty.app,raw,echo=0 \
PTY,link=/dev/tty.modem,raw,echo=0,wait-slave

### Notes

- /dev/tty.app and /dev/tty.modem act as paired serial ports.
- Anything written to one appears on the other.
- Requires user to be in the dialout group for access.


## USB FTDI Crossover Setup (Using Two USB‑Serial Adapters)

This describes how to create a null‑modem style crossover using **two USB FTDI serial adapters**, such as the Adafruit FTDI Friend or any FT232‑based USB‑serial cable.

### Hardware Needed
- Two USB‑to‑TTL serial adapters (FTDI, CP2102, CH340, etc.)
- Jumper wires (female–female typically)

### Wiring (TTL Level)

Adapter A TX → Adapter B RX  
Adapter A RX ← Adapter B TX  
Adapter A GND ↔ Adapter B GND

### Notes
- **Do NOT** connect 5V or 3.3V between adapters. Power stays isolated.
- Both sides must share **ground**, or you will get garbage data.
- Voltage levels must match (both 3.3V or both 5V).
- This creates a perfect software‑only “virtual cable” for testing serial code.

### Example Pinout for FTDI Friend
| FTDI Pin | Connects To |
|----------|-------------|
| GND      | Other Adapter GND |
| TXO      | Other Adapter RXI |
| RXI      | Other Adapter TXO |

### Testing
Use two terminals, each bound to one FTDI device:
- Linux: `/dev/ttyUSB0`, `/dev/ttyUSB1`
- macOS: `/dev/cu.usbserial‑XXXX`

Send text on one; it appears on the other.
