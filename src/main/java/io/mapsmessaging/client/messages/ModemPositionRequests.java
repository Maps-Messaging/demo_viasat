package io.mapsmessaging.client.messages;

public class ModemPositionRequests implements Requests{

  private final byte[] request;

  public ModemPositionRequests() {
    request = new byte[2];
    request[0] = (byte) 0; // Modem
    request[1] = (byte) 72; // Get Position
  }

  @Override
  public byte[] getRequest() {
    return request;
  }
}
