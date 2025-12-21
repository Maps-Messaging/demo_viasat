package io.mapsmessaging.client.messages;

public class ModemPositionRequests implements Requests{

  private final byte[] request;

  public ModemPositionRequests() {
    request = new byte[2];
    request[0] = getSin(); // Modem
    request[1] = getMin(); // Get Position
  }

  @Override
  public byte[] getRequest() {
    return request;
  }

  @Override
  public byte getSin() {
    return 0;
  }

  @Override
  public byte getMin() {
    return 72;
  }
}
