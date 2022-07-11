// @title Private Messenger Server - Constants
// @version 0.0.13
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

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class Constants {
    // SOCKET_PORT
    public static final int SOCKET_PORT = 8888;
    // SERVER_SOCKET_OPEN_PORT
    public static final int SERVER_SOCKET_OPEN_PORT = 8889;
    // KAFKA_PROPERTIES
    public static final Properties KAFKA_PROPERTIES = new Properties();
    // TOPIC
    public static final String TOPIC = "topic1";
    // MESSAGE
    public static final String MESSAGE = "message";
    // DELIMITER
    public static final String DELIMITER = ":";
    // SPLIT_MESSAGE
    public static final String SPLIT_MESSAGE = ",";
    // TIMEOUT
    public static int TIMEOUT = 5000;
    // TOPICS
    public static final Collection<String> TOPICS = new ArrayList<String>() {{
        add(TOPIC);
    }};
    // HOST_NAME
    public static final String HOST_NAME = "127.0.0.1";
    // HOST
    public static final SocketAddress HOST = new java.net.InetSocketAddress(HOST_NAME, PORT);

    // KAFKA_PROPERTIES
    static {
        KAFKA_PROPERTIES.put("bootstrap.servers", "127.0.0.1:9092");
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
}
