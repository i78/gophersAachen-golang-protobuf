package ac.gophers.sensorServer;

import ac.gophers.domain.measurements.MeasurementOuterClass;
import ac.gophers.domain.measurements.MeasurementOuterClass.Measurement;
import com.googlecode.protobuf.format.JsonFormat;
import java.time.Instant;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author manfreddreese
 */
public class SerializationSizeComparism {
  private Measurement measurement;

  @Before
  public void setUp() {
    measurement = MeasurementOuterClass.Measurement.newBuilder()
            .setTimestamp(Instant.now().getEpochSecond())
            .addReadings(
                    MeasurementOuterClass.SensorReading.newBuilder()
                            .setSensorName("Temperature-1")
                            .setValue(Math.sin(Instant.now().getEpochSecond() % 360) * 100)
                            .build()
            )
            .addReadings(
                    MeasurementOuterClass.SensorReading.newBuilder()
                            .setSensorName("Temperature-2")
                            .setValue(Math.cos(Instant.now().getEpochSecond() % 360) * 100)
                            .build()
            )
            .build();
  }

  @Test
  public void serializeToJson_shouldBeMoreThan0Bytes()  {
    String json = new JsonFormat().printToString(measurement);    
    int length = json.length();
    System.out.println("message.json=" + json);
    System.out.println("size(message.json)=" + length);
    assertTrue(length > 0);
  }
  
  @Test
  public void serializeToProtobuf_shouldBeMoreThan0Bytes()  {    
    byte[] wire = measurement.toByteArray();
    int length = wire.length;    
    System.out.println("message.protobuf=" + new Hex().encodeHexString(wire));
    System.out.println("size(message.protobuf)=" + length);
    assertTrue(length > 0);
  }
  
}
