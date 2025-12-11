package io.mapsmessaging.client.listeners;

import common.MqttClientConnection;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class DirectoryResponse implements IMqttMessageListener {

  private final MqttClientConnection connection;

  public DirectoryResponse(MqttClientConnection clientConnection) {
    this.connection = clientConnection;
  }

                           @Override
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    byte[] request = mqttMessage.getPayload();
    int len = request[0] & 0xff;
    if(len + 1 == request.length){
      String requestedPath = new String(request, 1, len);
      Path dir = Paths.get(requestedPath);
      DirectoryResult dr = new DirectoryResult(requestedPath);
      if(Files.isDirectory(dir)){
        try (Stream<Path> stream = Files.list(dir)) {
          stream.forEach(path -> {
            DirectoryEntry directoryEntry = new DirectoryEntry();

            try {
              boolean isDir = Files.isDirectory(path);
              directoryEntry.setType((byte) (isDir ? 0 : 1));
              directoryEntry.setName(path.getFileName().toString().getBytes(StandardCharsets.UTF_8));
              directoryEntry.setSize((int) Files.size(path));
              BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
              directoryEntry.setCreationTime( (int)(attrs.creationTime().toMillis()/1000L));

              dr.addDirectoryEntry(directoryEntry);
            }
            catch (IOException e) {
              // Let the universe burn or handle it properly
            }
          });
        }
      }
      if(!dr.getDirectoryEntries().isEmpty()){
        Runnable t = () -> {
          try {
            byte[] packed = dr.packed();
            connection.publish("/outbound", packed);
          } catch (MqttException| IOException e) {
            e.printStackTrace();
          }
        };
        Thread thread = new Thread(t);
        thread.start();
      }
    }
  }
}