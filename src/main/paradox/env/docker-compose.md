```dtd
version: "2"
services:

  mongodb:
    image: mongo:3.6.6-jessie
    volumes:
     - ./.tmp/var/lib/db:/data
    tmpfs:
     - /tmp
    ports:
     - "27017:27017"

  zookeeper:
    image: zookeeper:latest
    ports:
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka:2.11-0.10.2.1
    ports:
      - 9092:9092
    volumes:
      - ./.tmp/data:/data
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_CREATE_TOPICS: "UPSTREAM_TOPIC:1:1,DOWNSTREAM_TOPIC:1:1"
      KAFKA_MESSAGE_MAX_BYTES: 2000000
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181

```