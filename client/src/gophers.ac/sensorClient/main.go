package main

//go:generate protoc --go_out=../.. --proto_path $PROTO_PATH $PROTO_PATH/Measurement.proto

import (
	"flag"
	"log"

	MQTT "github.com/eclipse/paho.mqtt.golang"
	"github.com/golang/protobuf/proto"
	"gophers.ac/domain/measurements"
)

var averageTemperatures = map[string]float64{}

func measurementHandler(client MQTT.Client, message MQTT.Message) {
	measurement := &measurements.Measurement{}
	err := proto.Unmarshal(message.Payload(), measurement)

	if err != nil {
		log.Fatal("MHANDLER error=Could not decode message")
	} else {
		for _, reading := range measurement.GetReadings() {
			log.Println("sensor name=", reading.GetSensorName(), " value=", reading.GetValue())
			if value, exists := averageTemperatures[reading.GetSensorName()]; exists {
				averageTemperatures[reading.GetSensorName()] = (value + reading.GetValue()) / 2
			} else {
				averageTemperatures[reading.GetSensorName()] = reading.GetValue()
			}
		}
	}

	for sensor, value := range averageTemperatures {
		log.Println("averageTemp", " sensor="+sensor, " value=", value)
	}

}

func main() {
	done := make(chan bool)

	var mqttURI = flag.String("mqtt.uri", "tcp://127.0.0.1:1883", "URI of the MQTT broker to connect to.")
	flag.Parse()

	opts := MQTT.NewClientOptions().
		AddBroker(*mqttURI).
		SetClientID("go-client")

	client := MQTT.NewClient(opts)

	if token := client.Connect(); token.Wait() && token.Error() != nil {
		panic(token.Error())
	}

	log.Println("Waiting for telegrams")

	client.Subscribe("gophers/measurements", 0, measurementHandler)

	for {
		select {
		case <-done:
			break
		}
	}

}
