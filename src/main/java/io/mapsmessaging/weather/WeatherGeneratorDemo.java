package io.mapsmessaging.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.MqttClientConnection;
import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.Instant;

import static common.Configuration.MODEM_MQTT_URL;

public class WeatherGeneratorDemo implements Runnable{

  private MqttClientConnection client;
  private final  WeatherSample initial;
  private final WeatherGenerator generator;

  public WeatherGeneratorDemo() throws MqttException {
    initial = new WeatherSample();
    initial.setAtmosphericPressureKpa(100.2);
    initial.setTimestamp(Instant.now());
    initial.setRainfallMillimeters(0.0);
    initial.setTemperatureCelsius(35.1);
    initial.setHumidityRelativePercent(26.2);
    initial.setWindSpeedMetersPerSecond(0.0);
    initial.setLightIntensityLux(2811);
    initial.setWindDirectionCode(0);
    initial.setWindDirectionAngleDegrees(0.0);
    connect();
    updateSchemas();
    generator = new WeatherGenerator(initial);
    Thread t = new Thread(this);
    t.start();
  }

  public void connect() throws MqttException {
    client = new MqttClientConnection(MODEM_MQTT_URL, "weatherStation");
  }


  // Sets the schema for both topics as json
  public void updateSchemas() throws MqttException {
    MqttMessage message = new MqttMessage(JSON_SCHEMA.getBytes());
    client.publish("$SCHEMA/weather", message);
    client.publish("$SCHEMA/report/weather", message);

  }

  @SneakyThrows
  public void run(){
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    while (true) {
      WeatherSample sample = generator.nextSample();
      WeatherSampleView view = new WeatherSampleView(sample);
      MqttMessage message = new MqttMessage(gson.toJson(view).getBytes());
      message.setRetained(true);
      message.setQos(0);
      try {
        client.publish("/weather", message);
      } catch (MqttException e) {
        e.printStackTrace();
        System.exit(0);
      }
      Thread.sleep(1000);
    }
  }

  public static void main(String[] args) throws MqttException {
    new WeatherGeneratorDemo();
  }

  private static final String JSON_SCHEMA =
      "{\n" +
      "      \"versionId\": \"1\",\n" +
      "      \"name\": \"Generic JSON\",\n" +
      "      \"labels\": {\n" +
      "        \"uniqueId\": \"10000000-0000-1000-a000-100000000007\",\n" +
      "        \"interface\": \"json\",\n" +
      "        \"resource\": \"monitor\"\n" +
      "      },\n" +
      "      \"format\": \"json\",\n" +
      "      \"schema\": {},\n" +
      "      \"createdAt\": \"2025-12-09T22:06:37.8522435Z\",\n" +
      "      \"modifiedAt\": \"2025-12-09T22:26:45.713051Z\"\n" +
      "}\n";
}
