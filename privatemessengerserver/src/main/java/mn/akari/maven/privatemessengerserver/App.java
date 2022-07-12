// @title Private Messenger Server - App
// @version 0.0.20
// @author Takahashi Akari <akaritakahashioss@gmail.com>
// @date 2022-07-09
// @description This is a private messenger server. App.java contains main method.
// @license MIT License
// @copyright (c) 2022 Takahashi Akari <akaritakahashioss@gmail.com>
// @url <https://takahashi-akari.github.io/PrivateMessenger/>
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Server/main/privatemessengerserver/src/main/java/mn/akari/maven/privatemessengerserver/App.java
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Server/main/privatemessengerserver/src/main/java/mn/akari/maven/privatemessengerserver/Constants.java
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Server/main/README.md
// @see https://takahashi-akari.github.io/PrivateMessenger/
// @see https://raw.githubusercontent.com/takahashi-akari/PrivateMessenger-Client/main/privatemessengerclient/src/main/java/mn/akari/maven/privatemessengerclient/Client.java
// @see ./Constants.java
package mn.akari.maven.privatemessengerserver;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.BufferedWriter;

// class App

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

// App class is a main class of this project.
public class App {
    // Logger is a class for logging.
    private static Logger logger = Logger.getLogger(App.class.getName());
    // ExecutorService is a class for thread pool.
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    // KafkaConsumer is a class for kafka consumer.
    private static KafkaConsumer<String, String> kafkaConsumer;
    // KafkaProducer is a class for kafka producer.
    private static KafkaProducer<String, String> kafkaProducer;
    // Client is a class for client.
    private static Client client;

    // Main method is a main method of this project.
    public static void main(String[] args) {
        // initialize
        initialize();

        // run
        run();

        // sleep
        sleep();
    }
    
    private static void sleep() {
        try {
            Thread.sleep(Duration.ofSeconds(10).toMillis());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "InterruptedException", e);
        }
    }

    private static void run() {
        // run
        executorService.execute(() -> {
            // run
            runKafkaConsumer();
        });
        executorService.execute(() -> {
            // run
            runKafkaProducer();
        });
        executorService.execute(() -> {
            // run
            runClient();
        });
    }

    private static void runClient() {
        // run
        client.run();
    }

    private static void runKafkaProducer() {
        // run
        try {
            // run
            kafkaProducer.send(new ProducerRecord<String, String>(Constants.KAFKA_TOPIC_NAME, "test"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }
    }

    private static void runKafkaConsumer() {
        // run
        try {
            // run
            kafkaConsumer.subscribe(List.of(Constants.KAFKA_TOPIC_NAME));
            while (true) {
                // run
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    // run
                    logger.info(record.value());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }
    }

    private static void initialize() {
        // initialize
        try {
            Properties KAFKA_PROPERTIES = new Properties();
            KAFKA_PROPERTIES.put("bootstrap.servers", "localhost:9092");
            KAFKA_PROPERTIES.put("group.id", "group1");
            KAFKA_PROPERTIES.put("enable.auto.commit", "true");
            KAFKA_PROPERTIES.put("auto.commit.interval.ms", "1000");
            KAFKA_PROPERTIES.put("session.timeout.ms", "30000");
            KAFKA_PROPERTIES.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");  
            KAFKA_PROPERTIES.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
            KAFKA_PROPERTIES.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");  
            KAFKA_PROPERTIES.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
            // initialize
            kafkaConsumer = new KafkaConsumer<>(KAFKA_PROPERTIES);
            kafkaProducer = new KafkaProducer<>(KAFKA_PROPERTIES);
            client = new Client();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }
    }
}

// Client class is a class for client.
class Client {
    // Logger is a class for logging.
    private static Logger logger = Logger.getLogger(Client.class.getName());
    // Socket is a class for socket.
    private static Socket socket;
    // BufferedReader is a class for buffered reader.
    private static BufferedReader reader;
    // BufferedWriter is a class for buffered writer.
    private static BufferedWriter writer;

    // run method is a method for running this class.
    public void run() {
        // run
        try {
            // run
            // create socket
            socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
            // create reader
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // create writer
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // loop
            while (true) {
                // read message
                String message = readMessage();
                // send message
                send(message);
            }
        } catch (Exception e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void message(String message) {
        // message
        try {
            // message
            writer.write(message);
            // message
            writer.newLine();
            // message
            writer.flush();
        } catch (IOException e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        // log
        logger.log(Level.INFO, "client: {0}", message);
    }

    public void sendMessage(String value) {
        // run
        try {
            // run
            // send message
            writer.write(value);
            // flush
            writer.flush();
        } catch (Exception e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        // log
        logger.log(Level.INFO, "client: send message: " + value);
    }

    public String getMessage() {
        // run
        try {
            // run
            return reader.readLine();
        } catch (IOException e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
            // run
            return null;
        }
    }

    public void send(String message) {
        // run
        try {
            // run
            // send message
            writer.write(message);
            // flush
            writer.flush();
        } catch (Exception e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        // log
        logger.log(Level.INFO, "client: sent message: " + message);
    }

    private static String readMessage() {
        // run
        try {
            // run
            // read message
            return reader.readLine();
        } catch (IOException e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
            // run
            return null;
        }
    }

    void shutdown() {
        // run
        try {
            // run
            // close socket
            socket.close();
        } catch (IOException e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}

