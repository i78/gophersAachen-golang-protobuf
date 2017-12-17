# Golang / Java Protobuf Example

This repository contains the demonstration code for to talk at Golang Usergroup Aachen.

## Prerequisites
To be able to compile the Protobuf IDLs, please install the protobuf compiler and the protobuf-compiler-go using either the package management of your operating system or the binary distributions.

## Remarks
Both client and server use the language/framework tooling to compile the IDLs into golang/Java code. Thoretically, one could also use the CLI to trigger a manual compile, but I prefer to automate those things.

## Building the Example


### MQTT Server Docker Container
Client and server use an MQTT bus to talk to each other. The easiest way to spin up one is using the docker-compose recipe included in this repo.

```
cd mqtt
docker-compose up -d
```

Find out the IP address of the Container:

```
docker inspect mqtt_mosquitto_1 --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'
```


### Server (java)

Build the Server:
```
cd server
mvn install
```

You may want to modify the MQTT Server URI in the ''application.properties'' file before.

### Client (Golang)

Build the IDLs with ''go generate''. This executes all commands prefixed with ''go:generate'' in the go source files.
```
go generate src/gophers.ac/sensorClient/main.go
```

Build the go client:

```
cd client
go install gophers.ac/sensorClient
```

### Run the applications
After the MQTT broker is up, just run client and server in any order you prefer. As soon as the the client received a message, it outputs the values from the telegram to stdout and computes a dirty moving average.

Running the client:
```
bin/sensorClient --mqtt.uri="tcp://(your broker ip):1883"
```

Expected output:

```
 Waiting for telegrams
 sensor name= Temperature-1  value= 18.4752119221718
 sensor name= Temperature-2  value= -98.2785151720906
 averageTemp  sensor=Temperature-1  value= 18.4752119221718
 averageTemp  sensor=Temperature-2  value= -98.2785151720906
 sensor name= Temperature-1  value= 99.48237286710673
 sensor name= Temperature-2  value= -10.1615692060797
 averageTemp  sensor=Temperature-1  value= 58.978792394639264
 averageTemp  sensor=Temperature-2  value= -54.22004218908515
```
