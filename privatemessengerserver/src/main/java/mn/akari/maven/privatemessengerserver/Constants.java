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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class Constants {
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
    public static final int TIMEOUT = 1000;
    // MESSAGE_SIZE
    public static final Object MESSAGE_SIZE = 1024;
    public static final long KAFKA_POLL_TIMEOUT = 1000;
    // TOPICS
    public static final Collection<String> TOPICS = new ArrayList<String>() {{
        add(TOPIC);
    }};
    // KAFKA_SERVER
    public static final String KAFKA_SERVER = "127.0.0.1:9092";
    // KAFKA_CLIENT_ID
    public static final String KAFKA_CLIENT_ID = "client1";
    // KAFKA_GROUP_ID
    public static final String KAFKA_GROUP_ID = "group1";
    // KAFKA_AUTO_OFFSET_RESET
    public static final String KAFKA_AUTO_OFFSET_RESET = "latest";
    // KAFKA_ENABLE_AUTO_COMMIT
    public static final boolean KAFKA_ENABLE_AUTO_COMMIT = false;
    // KAFKA_MAX_POLL_RECORDS
    public static final int KAFKA_MAX_POLL_RECORDS = 1;
    // KAFKA_MAX_POLL_INTERVAL_MS
    public static final int KAFKA_MAX_POLL_INTERVAL_MS = 100;
    // HOST
    public static final String HOST = "127.0.0.1";
    // PORT
    public static final int PORT = 8080;
    public static Properties KAFKA_CONSUMER_PROPERTIES;
    public static Properties KAFKA_PRODUCER_PROPERTIES;
    public static final Collection<String> KAFKA_CONSUMER_TOPICS;
    public static final String KAFKA_PRODUCER_TOPIC = "topic1";
    public static String KAFKA_PRODUCER_MESSAGE = "";
    public static final String KAFKA_PRODUCER_DELIMITER = ":";
    public static final String KAFKA_PRODUCER_SPLIT_MESSAGE = ",";
    public static int KAFKA_PRODUCER_MESSAGE_SIZE = 1024;
    public static final String KAFKA_PRODUCER_TIMEOUT = "5000";
    public static final String KAFKA_PRODUCER_KAFKA_POLL_TIMEOUT = "5000";
    public static final String KAFKA_PRODUCER_KAFKA_SERVER = "127.0.0.1:9092";
    public static final String KAFKA_PRODUCER_KAFKA_CLIENT_ID = "client1";
    public static final String KAFKA_PRODUCER_KAFKA_GROUP_ID = "group1";
    public static final String KAFKA_PRODUCER_KAFKA_AUTO_OFFSET_RESET = "latest";
    public static final boolean KAFKA_PRODUCER_KAFKA_ENABLE_AUTO_COMMIT = false;
    public static final int KAFKA_PRODUCER_KAFKA_MAX_POLL_RECORDS = 1;
    public static final int KAFKA_PRODUCER_KAFKA_MAX_POLL_INTERVAL_MS = 100;
    public static final String KAFKA_PRODUCER_HOST = "127.0.0.1";
    public static final int KAFKA_PRODUCER_PORT = 8080;
    public static String KAFKA_TOPIC = "topic1";
    // SERVER_HOST
    public static final String SERVER_HOST = "127.0.0.1";
    // SERVER_PORT
    public static final int SERVER_PORT = 8080;

    // private static field 
    private static ArrayList<String> KAFKA_PRODUCER_TOPICS;
    private static int KAFKA_PRODUCER_MESSAGE_COUNT;
    private static int KAFKA_PRODUCER_MESSAGE_DELAY;
    private static int KAFKA_PRODUCER_MESSAGE_DELAY_RANGE;
    private static double KAFKA_PRODUCER_MESSAGE_DELAY_RANGE_RATIO;
    public static long KAFKA_CONSUMER_POLL_TIMEOUT = 1000;
    public static long KAFKA_PRODUCER_SLEEP_TIME = 1000;
    public static String KAFKA_CONSUMER_TOPIC = "topic1";
    public static long SLEEP_TIME = 1000;
    public static long CLIENT_SLEEP_TIME = 1000;
    public static long KAFKA_CONSUMER_SLEEP_TIME = 1000;
    public static Collection<String> KAFKA_TOPIC_NAME = new ArrayList<String>() {{
        add(TOPIC);
    }};
    
    // KAFKA_PROPERTIES
    static {
        KAFKA_PROPERTIES.put("bootstrap.servers", KAFKA_SERVER);
        KAFKA_PROPERTIES.put("client.id", KAFKA_CLIENT_ID);
        KAFKA_PROPERTIES.put("group.id", KAFKA_GROUP_ID);
        KAFKA_PROPERTIES.put("auto.offset.reset", KAFKA_AUTO_OFFSET_RESET);
        KAFKA_PROPERTIES.put("enable.auto.commit", KAFKA_ENABLE_AUTO_COMMIT);
        KAFKA_PROPERTIES.put("max.poll.records", KAFKA_MAX_POLL_RECORDS);
        KAFKA_PROPERTIES.put("max.poll.interval.ms", KAFKA_MAX_POLL_INTERVAL_MS);
        KAFKA_PROPERTIES.put("poll.timeout.ms", KAFKA_POLL_TIMEOUT);
        KAFKA_PROPERTIES.put("acks", "all");
        KAFKA_PROPERTIES.put("retries", 0);
        KAFKA_PROPERTIES.put("batch.size", 16384);
        KAFKA_PROPERTIES.put("linger.ms", 1);
        KAFKA_PROPERTIES.put("buffer.memory", 33554432);
        KAFKA_PROPERTIES.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PROPERTIES.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PROPERTIES.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PROPERTIES.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }
    // KAFKA_CONSUMER_TOPICS
    static {
        KAFKA_CONSUMER_TOPICS = new ArrayList<String>() {{
            add(TOPIC);
        }};
    }
    // KAFKA_CONSUMER_PROPERTIES
    static {
        KAFKA_CONSUMER_PROPERTIES = new Properties();
        KAFKA_CONSUMER_PROPERTIES.putAll(KAFKA_PROPERTIES);
        KAFKA_CONSUMER_PROPERTIES.put("auto.offset.reset", "earliest");

        KAFKA_CONSUMER_PROPERTIES.put("topics", KAFKA_CONSUMER_TOPICS);


        KAFKA_CONSUMER_PROPERTIES.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_CONSUMER_PROPERTIES.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


        KAFKA_CONSUMER_PROPERTIES.put("auto.offset.reset", "earliest");
        KAFKA_CONSUMER_PROPERTIES.put("enable.auto.commit", false);
        KAFKA_CONSUMER_PROPERTIES.put("max.poll.records", 1);
        KAFKA_CONSUMER_PROPERTIES.put("max.poll.interval.ms", 100);
        KAFKA_CONSUMER_PROPERTIES.put("poll.timeout.ms", 5000);
    }
    // KAFKA_PRODUCER_PROPERTIES
    static {
        KAFKA_PRODUCER_PROPERTIES = new Properties();
        KAFKA_PRODUCER_PROPERTIES.putAll(KAFKA_PROPERTIES);
        KAFKA_PRODUCER_PROPERTIES.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PRODUCER_PROPERTIES.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KAFKA_PRODUCER_PROPERTIES.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PRODUCER_PROPERTIES.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KAFKA_PRODUCER_PROPERTIES.put("acks", "all");
        KAFKA_PRODUCER_PROPERTIES.put("retries", 0);
        KAFKA_PRODUCER_PROPERTIES.put("batch.size", 16384);
        KAFKA_PRODUCER_PROPERTIES.put("linger.ms", 1);
        KAFKA_PRODUCER_PROPERTIES.put("buffer.memory", 33554432);
    }
    // KAFKA_PRODUCER_TOPICS
    static {
        KAFKA_PRODUCER_TOPICS = new ArrayList<String>() {{
            add(TOPIC);
        }};
    }
    // KAFKA_PRODUCER_MESSAGE
    static {
        KAFKA_PRODUCER_MESSAGE = "message";
    }
    // KAFKA_PRODUCER_MESSAGE_SIZE
    static {
        KAFKA_PRODUCER_MESSAGE_SIZE = 1024;
    }
    // KAFKA_PRODUCER_MESSAGE_COUNT
    static {
        KAFKA_PRODUCER_MESSAGE_COUNT = 1;
    }
    // KAFKA_PRODUCER_MESSAGE_DELAY
    static {
        KAFKA_PRODUCER_MESSAGE_DELAY = 1000;
    }
    // KAFKA_PRODUCER_MESSAGE_DELAY_RANGE
    static {
        KAFKA_PRODUCER_MESSAGE_DELAY_RANGE = 1000;
    }
    // KAFKA_PRODUCER_MESSAGE_DELAY_RANGE_RATIO
    static {
        KAFKA_PRODUCER_MESSAGE_DELAY_RANGE_RATIO = 0.5;
    }
}
