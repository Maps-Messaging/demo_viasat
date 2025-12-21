package io.mapsmessaging.client.messages;


public class DirectoryRequest implements Requests{

  private byte[] request;

  public DirectoryRequest(String path){
    int len = 3+path.length();
    request = new byte[len];
    request[0] = getSin();
    request[1] = getMin();
    request[2] = (byte) path.length();
    System.arraycopy(path.getBytes(),0,request,3,path.length());
  }

  @Override
  public byte[] getRequest() {
    return request;
  }

  @Override
  public byte getSin() {
    return 24;
  }

  @Override
  public byte getMin() {
    return 3;
  }
}
