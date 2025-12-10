package io.mapsmessaging.weather;

import java.time.Instant;
import java.util.Random;

public class WeatherGenerator {

  private final Random random;
  private final WeatherSample currentSample;

  public WeatherGenerator(WeatherSample initialSample) {
    this.random = new Random();
    this.currentSample = cloneSample(initialSample);
  }

  public WeatherSample nextSample() {
    // timestamp is always now
    currentSample.setTimestamp(Instant.now());

    // Pressure: ±0.02 kPa, clamp 95–105
    double newPressure = currentSample.getAtmosphericPressureKpa()
        + randomDelta(0.02);
    newPressure = clamp(newPressure, 95.0, 105.0);
    currentSample.setAtmosphericPressureKpa(newPressure);

    // Temperature: ±0.05 °C, clamp -10–45
    double newTemperature = currentSample.getTemperatureCelsius()
        + randomDelta(0.05);
    newTemperature = clamp(newTemperature, -10.0, 45.0);
    currentSample.setTemperatureCelsius(newTemperature);

    // Humidity: ±0.2 %, clamp 0–100
    double newHumidity = currentSample.getHumidityRelativePercent()
        + randomDelta(0.2);
    newHumidity = clamp(newHumidity, 0.0, 100.0);
    currentSample.setHumidityRelativePercent(newHumidity);

    // Wind speed: ±0.1 m/s, not below 0
    double newWindSpeed = currentSample.getWindSpeedMetersPerSecond()
        + randomDelta(0.1);
    if (newWindSpeed < 0.0) {
      newWindSpeed = 0.0;
    }
    currentSample.setWindSpeedMetersPerSecond(newWindSpeed);

    // Light: ±50 lux, not below 0
    double newLight = currentSample.getLightIntensityLux()
        + randomDelta(50.0);
    if (newLight < 0.0) {
      newLight = 0.0;
    }
    currentSample.setLightIntensityLux((long)newLight);

    // Wind direction: random walk ±3°, wrap 0–359
    double newAngle = currentSample.getWindDirectionAngleDegrees()
        + randomDelta(3.0);
    newAngle = normalizeAngle(newAngle);
    currentSample.setWindDirectionAngleDegrees(newAngle);

    // Simple 8-way direction code from angle
    int newDirectionCode = angleToDirectionCode(newAngle);
    currentSample.setWindDirectionCode(newDirectionCode);

    // Rainfall: keep as-is (or you can add rain logic later)
    // currentSample.setRainfallMillimeters(...);

    return cloneSample(currentSample);
  }

  private double randomDelta(double maxAbsoluteDelta) {
    double scale = random.nextDouble() * 2.0 - 1.0; // [-1, 1]
    return scale * maxAbsoluteDelta;
  }

  private double clamp(double value, double minValue, double maxValue) {
    if (value < minValue) {
      return minValue;
    }
    if (value > maxValue) {
      return maxValue;
    }
    return value;
  }

  private double normalizeAngle(double angleDegrees) {
    double normalized = angleDegrees % 360.0;
    if (normalized < 0.0) {
      normalized += 360.0;
    }
    return normalized;
  }

  /**
   * Map 0–360 degrees into 8 sectors:
   * 0: N, 1: NE, 2: E, 3: SE, 4: S, 5: SW, 6: W, 7: NW
   */
  private int angleToDirectionCode(double angleDegrees) {
    double normalized = normalizeAngle(angleDegrees);
    int sector = (int) Math.floor((normalized + 22.5) / 45.0);
    if (sector == 8) {
      sector = 0;
    }
    return sector;
  }

  private WeatherSample cloneSample(WeatherSample source) {
    WeatherSample copy = new WeatherSample();
    copy.setAtmosphericPressureKpa(source.getAtmosphericPressureKpa());
    copy.setTimestamp(source.getTimestamp());
    copy.setRainfallMillimeters(source.getRainfallMillimeters());
    copy.setTemperatureCelsius(source.getTemperatureCelsius());
    copy.setHumidityRelativePercent(source.getHumidityRelativePercent());
    copy.setWindSpeedMetersPerSecond(source.getWindSpeedMetersPerSecond());
    copy.setLightIntensityLux(source.getLightIntensityLux());
    copy.setWindDirectionCode(source.getWindDirectionCode());
    copy.setWindDirectionAngleDegrees(source.getWindDirectionAngleDegrees());
    return copy;
  }
}