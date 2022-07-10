// @title Private Messenger Server Constants
// @version 0.0.7
// @author Takahashi Akari <akaritakahashioss@gmail.com>
// @date 2022-07-09
// @description This is a private messenger server.
// @license MIT License <https://opensource.org/licenses/MIT>
// @copyright (c) 2022 Takahashi Akari <akaritakahashioss@gmail.com>
// @url <https://takahashi-akari.github.io/PrivateMessenger/>
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
    // KAFKA_PROPERTIES
    static {
        KAFKA_PROPERTIES.put("bootstrap.servers", "localhost:9092");
        KAFKA_PROPERTIES.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PROPERTIES.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PROPERTIES.put("group.id", "group1");
        KAFKA_PROPERTIES.put("auto.offset.reset", "latest");
        KAFKA_PROPERTIES.put("enable.auto.commit", "false");
    }
    // TOPIC
    static {
        TOPIC.add("topic1");
        TOPIC.add("topic2");
        TOPIC.add("topic3");
    }
}
