package ac.gophers.sensorServer.provider;

import ac.gophers.domain.measurements.MeasurementOuterClass.Measurement;
import ac.gophers.domain.measurements.MeasurementOuterClass.SensorReading;
import ac.gophers.sensorServer.service.MeasurementPublisher;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author manfreddreese
 */
@Component
public class PeriodicMeasurementProvider {
  private static final Logger log = LoggerFactory.getLogger(PeriodicMeasurementProvider.class);    
  
  @Autowired
  MeasurementPublisher publisher;
  
  @Scheduled(fixedDelay = 5000)
  public void issueNewMeasurement() {
    log.info("New measurement");
    
    Measurement m = Measurement.newBuilder()
            .setTimestamp(Instant.now().getEpochSecond())
            .addReadings(
                    SensorReading.newBuilder()
                    .setSensorName("Temperature-1")
                    .setValue(Math.sin(Instant.now().getEpochSecond() % 360)*100)
                    .build()
            )
            .addReadings(
                    SensorReading.newBuilder()
                    .setSensorName("Temperature-2")
                    .setValue(Math.cos(Instant.now().getEpochSecond() % 360)*100)
                    .build()
            )
            .build();
    
    log.info("built new measurement{}", m);
    
    publisher.publish(m);
                    
  }
  
}
