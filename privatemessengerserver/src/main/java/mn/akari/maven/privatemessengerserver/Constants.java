// @title Private Messenger Server - Constants
// @version 0.0.20
// @author Takahashi Akari <akaritakahashioss@gmail.com>
// @date 2022-07-09
// @description This is a private messenger server. This class contains constants.
// @license MIT License
// @copyright (c) 2022 Takahashi Akari <akaritakahashioss@gmail.com>
// @url <https://takahashi-akari.github.io/PrivateMessenger/>
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Server/main/privatemessengerserver/src/main/java/mn/akari/maven/privatemessengerserver/App.java
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Server/main/privatemessengerserver/src/main/java/mn/akari/maven/privatemessengerserver/Constants.java
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Server/main/README.md
// @see https://takahashi-akari.github.io/PrivateMessenger/
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Client/main/privatemessengerclient/src/main/java/mn/akari/maven/privatemessengerclient/Client.java
// @see ./App.java
    
package mn.akari.maven.privatemessengerserver;

import java.util.Properties;

public class Constants {
    public static final Properties KAFKA_PROPERTIES = new Properties();
    public static final String KAFKA_BROKER_URL = "127.0.0.1:9092";
    // KAFKA_CONSUMER_PROPERTIES
    public static final Properties KAFKA_CONSUMER_PROPERTIES = new Properties();
    // KAFKA_PRODUCER_PROPERTIES
    public static final Properties KAFKA_PRODUCER_PROPERTIES = new Properties();
    // KAFKA_TOPIC
    public static final String KAFKA_TOPIC = "topic1";
    public static final String KAFKA_TOPIC_NAME = "topic1";
    public static final String KAFKA_PRODUCER_KEY = "key1";
    public static final String KAFKA_PRODUCER_VALUE = "value1";
    // PORT
    public static final int PORT = 8080;
    
    // KAFKA_PROPERTIES
    static {
        KAFKA_PROPERTIES.put("bootstrap.servers", Constants.KAFKA_BROKER_URL);
        KAFKA_PROPERTIES.put("acks", "all");
        KAFKA_PROPERTIES.put("retries", "0");
        KAFKA_PROPERTIES.put("batch.size", "16384");
        KAFKA_PROPERTIES.put("linger.ms", "1");
        KAFKA_PROPERTIES.put("buffer.memory", "33554432");
        KAFKA_PROPERTIES.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PROPERTIES.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PROPERTIES.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PROPERTIES.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }
    // KAFKA_CONSUMER_PROPERTIES
    static {
        KAFKA_CONSUMER_PROPERTIES.putAll(KAFKA_PROPERTIES);
    }
    // KAFKA_PRODUCER_PROPERTIES
    static {
        KAFKA_PRODUCER_PROPERTIES.putAll(KAFKA_PROPERTIES);
    }
}
