package io.mapsmessaging.client.listeners;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DirectoryResult {

  private List<DirectoryEntry> directoryEntries;
  private String path;

  public DirectoryResult(String path) {
    directoryEntries = new ArrayList<>();
    this.path = path;
  }

  public void addDirectoryEntry(DirectoryEntry directoryEntry) {
    directoryEntries.add(directoryEntry);
  }

  public byte[] packed() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
    outputStream.write(24);
    outputStream.write(3);
    outputStream.write(path.length());
    outputStream.write(path.getBytes());
    outputStream.write(directoryEntries.size());
    for(DirectoryEntry directoryEntry : directoryEntries){
      outputStream.write(directoryEntry.pack());
    }
    return outputStream.toByteArray();
  }


  public static void main(String[] args) throws IOException {
    DirectoryResult results = new DirectoryResult(".");
    DirectoryEntry entry = new DirectoryEntry();
    entry.setType((byte)1);
    entry.setName("fred".getBytes(StandardCharsets.UTF_8));
    entry.setSize(1000);
    entry.setCreationTime(1000000);
    results.addDirectoryEntry(entry);
    entry = new DirectoryEntry();
    entry.setType((byte)0);
    entry.setName("directory".getBytes(StandardCharsets.UTF_8));
    entry.setSize(200);
    entry.setCreationTime(200000);
    results.addDirectoryEntry(entry);

    byte[] packed = results.packed();
    StringBuffer sb = new StringBuffer();
    for(byte b : packed){
      int val = b & 0xff;
      if(val < 16){
        sb.append("0");
      }
      sb.append(Integer.toHexString(val)).append(" ");
    }
    System.err.println(sb.toString());

  }

}
