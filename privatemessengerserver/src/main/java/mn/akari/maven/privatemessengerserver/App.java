// @title Private Messenger Server - App
// @version 0.0.16
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
        // shutdown
        shutdown();
    }

    private static void shutdown() {
        // shutdown kafka consumer
        kafkaConsumer.close();
        // shutdown kafka producer
        kafkaProducer.close();
        // shutdown executor service
        executorService.shutdown();
    }

    private static void run() {
        // run
        executorService.execute(() -> {
            // run
            try {
                // run
                runKafkaConsumer();
            } catch (Exception e) {
                // error
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        });
        executorService.execute(() -> {
            // run
            try {
                // run
                runKafkaProducer();
            } catch (Exception e) {
                // error
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        });
        executorService.execute(() -> {
            // run
            try {
                // run
                runClient();
            } catch (Exception e) {
                // error
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        });
    }

    private static void runKafkaConsumer() {
        // run
        try {
            // run
            while (true) {
                // run
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                // run
                for (ConsumerRecord<String, String> record : records) {
                    // run
                    logger.log(Level.INFO, "Received message: " + record.value());
                }
            }
        } catch (Exception e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void runKafkaProducer() {
        // run
        try {
            // run
            while (true) {
                // run
                String message = readMessage();
                // run
                if (message != null) {
                    // run
                    kafkaProducer.send(new ProducerRecord<String, String>(Constants.KAFKA_TOPIC, message));
                }
            }
        } catch (Exception e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static String readMessage() {
        // run
        try {
            // run
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // run
            return reader.readLine();
        } catch (IOException e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
            // run
            return null;
        }
    }

    private static void runClient() {
        // run
        client.run();
    }

    private static void initialize() {
        // initialize kafka consumer
        kafkaConsumer = new KafkaConsumer<>(Constants.KAFKA_CONSUMER_PROPERTIES);
        // initialize kafka producer
        kafkaProducer = new KafkaProducer<>(Constants.KAFKA_PRODUCER_PROPERTIES);
        // initialize client
        client = new Client();
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
            socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
            // run
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // run
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // run
            while (true) {
                // run
                String message = readMessage();
                // run
                if (message != null) {
                    // run
                    writer.write(message);
                    // run
                    writer.newLine();
                    // run
                    writer.flush();
                }
            }
        } catch (SocketException e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            // run
            shutdown();
        }
    }

    private static String readMessage() {
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

    private static void shutdown() {
        // shutdown socket
        if (socket != null) {
            // run
            try {
                // run
                socket.close();
            } catch (IOException e) {
                // error
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
}

