package io.mapsmessaging.weather;


import lombok.Data;

import java.time.Instant;

@Data
public class WeatherSample {
  private double atmosphericPressureKpa;
  private Instant timestamp;
  private double rainfallMillimeters;
  private double temperatureCelsius;
  private double humidityRelativePercent;
  private double windSpeedMetersPerSecond;
  private long lightIntensityLux;
  private int windDirectionCode;
  private double windDirectionAngleDegrees;
}