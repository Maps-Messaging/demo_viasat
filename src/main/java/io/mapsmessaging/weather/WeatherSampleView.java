package io.mapsmessaging.weather;

import lombok.Data;

@Data
public class WeatherSampleView {

  private final double atmosphericPressureKpa;
  private final String timestamp;
  private final double rainfallMillimeters;
  private final double temperatureCelsius;
  private final double humidityRelativePercent;
  private final double windSpeedMetersPerSecond;
  private final double lightIntensityLux;
  private final int windDirectionCode;
  private final double windDirectionAngleDegrees;

  public WeatherSampleView(WeatherSample sample) {
    this.atmosphericPressureKpa = round1(sample.getAtmosphericPressureKpa());
    this.timestamp = sample.getTimestamp().toString();
    this.rainfallMillimeters = round1(sample.getRainfallMillimeters());
    this.temperatureCelsius = round1(sample.getTemperatureCelsius());
    this.humidityRelativePercent = round1(sample.getHumidityRelativePercent());
    this.windSpeedMetersPerSecond = round1(sample.getWindSpeedMetersPerSecond());
    this.lightIntensityLux = round1(sample.getLightIntensityLux());
    this.windDirectionCode = sample.getWindDirectionCode();
    this.windDirectionAngleDegrees = round1(sample.getWindDirectionAngleDegrees());
  }

  private double round1(double v) {
    return Math.round(v * 10.0) / 10.0;
  }
}