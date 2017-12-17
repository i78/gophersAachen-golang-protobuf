package ac.gophers.sensorServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SensorServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SensorServerApplication.class, args);
  }
}
