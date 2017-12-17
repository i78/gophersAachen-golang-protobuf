package ac.gophers.sensorServer.service;

import ac.gophers.domain.measurements.MeasurementOuterClass.Measurement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author manfreddreese
 */
@Service
public class MqttMeasurementPublisher implements MeasurementPublisher {

  MqttClient client;
  MqttConnectOptions connectOptions;

  @Value("${mqtt.topic}")
  String topic;

  public MqttMeasurementPublisher(
          @Value("${mqtt.broker.url}") String brokerUrl,
          @Value("${mqtt.client.id}") String clientId)
          throws MqttException {
    client = new MqttClient(brokerUrl, clientId);
    connectOptions = new MqttConnectOptions();
    client.connect();
  }

  @Override
  public void publish(Measurement m) {
    try {    
      client.publish(topic, new MqttMessage(m.toByteArray()));
    } catch (MqttException ex) {
      Logger.getLogger(MqttMeasurementPublisher.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
