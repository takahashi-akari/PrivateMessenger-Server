// @title Private Messenger Server - Constants
// @version 0.0.12
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class Constants {
    // PORT
    public static final int PORT = 8080;
    // KAFKA_PROPERTIES
    public static final Properties KAFKA_PROPERTIES = new Properties();
    // TOPIC
    public static final Collection<String> TOPIC = new ArrayList<>();
    // MESSAGE
    public static final String MESSAGE = "message";
    // MESSAGE_TYPE
    public static final String MESSAGE_TYPE = "messageType";
    // MESSAGE_TYPE_REQUEST
    public static final String MESSAGE_TYPE_REQUEST = "request";
    // MESSAGE_TYPE_RESPONSE
    public static final String MESSAGE_TYPE_RESPONSE = "response";
    // MESSAGE_TYPE_NOTIFICATION
    public static final String MESSAGE_TYPE_NOTIFICATION = "notification";
    // MESSAGE_TYPE_ERROR
    public static final String MESSAGE_TYPE_ERROR = "error";
    // DELIMITER
    public static final String DELIMITER = ":";
    // SPLIT_MESSAGE
    public static final String SPLIT_MESSAGE = ",";
    // HOST
    public static final String HOST = "localhost";

    // KAFKA_PROPERTIES
    static {
        KAFKA_PROPERTIES.put("bootstrap.servers", "localhost:9092");
        KAFKA_PROPERTIES.put("acks", "all");
        KAFKA_PROPERTIES.put("retries", 0);
        KAFKA_PROPERTIES.put("batch.size", 16384);
        KAFKA_PROPERTIES.put("linger.ms", 1);
        KAFKA_PROPERTIES.put("buffer.memory", 33554432);
        KAFKA_PROPERTIES.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PROPERTIES.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PROPERTIES.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PROPERTIES.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PROPERTIES.put("group.id", "group1");
    }
    // TOPIC
    static {
        TOPIC.add("topic1");
        TOPIC.add("topic2");
        TOPIC.add("topic3");
    }
}
