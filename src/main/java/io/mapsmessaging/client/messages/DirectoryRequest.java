package io.mapsmessaging.client.messages;


public class DirectoryRequest implements Requests{

  private byte[] request;

  public DirectoryRequest(String path){
    int len = 3+path.length();
    request = new byte[len];
    request[0] = (byte) 24;
    request[1] = (byte) 3;
    request[2] = (byte) path.length();
    System.arraycopy(path.getBytes(),0,request,3,path.length());
  }

  @Override
  public byte[] getRequest() {
    return request;
  }
}
