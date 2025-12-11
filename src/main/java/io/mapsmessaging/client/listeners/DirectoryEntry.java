package io.mapsmessaging.client.listeners;

import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Data
public class DirectoryEntry {
  byte type;
  byte[] name;
  int size;
  int creationTime;

  public byte[] pack() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(type);
    outputStream.write(name.length);
    outputStream.write(name);
    outputStream.write(packUnsignedInt(size));
    outputStream.write(packSignedInt(creationTime));
    return outputStream.toByteArray();
  }

  private byte[] packUnsignedInt(int val){
    byte[] res = new byte[3];
    res[0] = (byte) (val >>> 16);
    res[1] = (byte) (val >>> 8);
    res[2] = (byte) (val);
    return res;
  }

  private byte[] packSignedInt(int val){
    byte[] res = new byte[4];
    res[0] = (byte) (val >>> 24);
    res[1] = (byte) (val >>> 16);
    res[2] = (byte) (val >>> 8);
    res[3] = (byte) (val);
    return res;
  }
}
