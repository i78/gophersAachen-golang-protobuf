package ac.gophers.sensorServer.service;

import ac.gophers.domain.measurements.MeasurementOuterClass.Measurement;
import org.springframework.stereotype.Service;

/**
 *
 * @author manfreddreese
 */
public interface MeasurementPublisher {
    public void publish(Measurement m);
  
}
