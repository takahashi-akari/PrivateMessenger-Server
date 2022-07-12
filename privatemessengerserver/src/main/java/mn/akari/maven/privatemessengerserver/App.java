// @title Private Messenger Server - App
// @version 0.0.19
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
        // executor loop
        executorLoop();
        // shutdown
        shutdown();
        // system exit
        System.exit(0);
    }

    private static void executorLoop() {
        // executor loop
        while (true) {
            try {
                // sleep
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // log
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
            // executor loop
            executorService.execute(() -> {
                // executor loop
                while (true) {
                    // executor loop
                    try {
                        // executor loop
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // log
                        logger.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            });
        }
    }

    private static void shutdown() {
        // shutdown kafka consumer
        kafkaConsumer.close();
        // shutdown kafka producer
        kafkaProducer.close();
        // shutdown executor service
        executorService.shutdown();
        // shutdown client
        client.shutdown();
    }

    private static void run() {
        // run kafka consumer
        runKafkaConsumer();
        // run kafka producer
        runKafkaProducer();
        // run client
        runClient();
    }
    private static void runKafkaConsumer() {
        // run kafka consumer
        executorService.execute(() -> {
            // create kafka consumer
            kafkaConsumer = new KafkaConsumer<>(Constants.KAFKA_CONSUMER_PROPERTIES);
            // subscribe topics
            kafkaConsumer.subscribe(Constants.KAFKA_CONSUMER_TOPICS);
            // loop
            while (true) {
                // sleep
                try {
                    Thread.sleep(Constants.KAFKA_CONSUMER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    // log
                    logger.log(Level.SEVERE, "kafka consumer: {0}", e.getMessage());
                }
                // poll
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(Constants.KAFKA_CONSUMER_POLL_TIMEOUT));
                // loop
                for (ConsumerRecord<String, String> record : records) {
                    // log
                    logger.log(Level.INFO, "kafka consumer: {0}", record.value());
                    // send message to client
                    client.sendMessage(record.value());
                }

                // sleep
                try {
                    Thread.sleep(Constants.KAFKA_CONSUMER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    // log
                    logger.log(Level.SEVERE, "kafka consumer: {0}", e.getMessage());
                }

                // commit offsets
                kafkaConsumer.commitSync();
            }
        });

        // log
        logger.log(Level.INFO, "kafka consumer: started");
    }

    private static void runKafkaProducer() {
        // run kafka producer
        executorService.execute(() -> {
            // create kafka producer
            kafkaProducer = new KafkaProducer<>(Constants.KAFKA_PRODUCER_PROPERTIES);
            // loop
            while (true) {
                // sleep
                try {
                    Thread.sleep(Constants.KAFKA_PRODUCER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    // log
                    logger.log(Level.SEVERE, "kafka producer: " + e.getMessage());
                }
                // send to kafka consumer
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(Constants.KAFKA_CONSUMER_TOPIC, "hello");
                kafkaProducer.send(producerRecord);
            }
        });

        // log
        logger.log(Level.INFO, "kafka producer: started");
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
        // run client
        executorService.execute(() -> {
            // create client
            client = new Client();
            // loop
            while (true) {
                // sleep
                try {
                    Thread.sleep(Constants.CLIENT_SLEEP_TIME);
                } catch (InterruptedException e) {
                    // log
                    logger.log(Level.SEVERE, "client: " + e.getMessage());
                }

                // read message
                String message = readMessage();
                // message check
                if (message == null) {
                    // error
                    logger.log(Level.SEVERE, "client: message is null");
                    // continue
                    continue;
                }
                // send message
                client.send(message);
            }
        });

        // log
        logger.log(Level.INFO, "client: started");
    }

    private static void initialize() {
        // initialize
        try {
            // initialize
            // KAFKA_CONSUMER_CONFIG
            Properties KAFKA_CONSUMER_CONFIG = new Properties();
            KAFKA_CONSUMER_CONFIG.put("bootstrap.servers", Constants.KAFKA_SERVER);
            KAFKA_CONSUMER_CONFIG.put("group.id", Constants.KAFKA_GROUP_ID);
            KAFKA_CONSUMER_CONFIG.put("enable.auto.commit", "true");
            KAFKA_CONSUMER_CONFIG.put("auto.commit.interval.ms", "1000");
            KAFKA_CONSUMER_CONFIG.put("session.timeout.ms", "30000");
            KAFKA_CONSUMER_CONFIG.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            KAFKA_CONSUMER_CONFIG.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            // KAFKA_PRODUCER_CONFIG
            Properties KAFKA_PRODUCER_CONFIG = new Properties();
            KAFKA_PRODUCER_CONFIG.put("bootstrap.servers", Constants.KAFKA_SERVER);
            KAFKA_PRODUCER_CONFIG.put("acks", "all");
            KAFKA_PRODUCER_CONFIG.put("retries", 0);
            KAFKA_PRODUCER_CONFIG.put("batch.size", 16384);
            KAFKA_PRODUCER_CONFIG.put("linger.ms", 1);
            KAFKA_PRODUCER_CONFIG.put("buffer.memory", 33554432);
            KAFKA_PRODUCER_CONFIG.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            KAFKA_PRODUCER_CONFIG.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            // initialize kafka consumer
            kafkaConsumer = new KafkaConsumer<>(KAFKA_CONSUMER_CONFIG);
            // initialize kafka producer
            kafkaProducer = new KafkaProducer<>(KAFKA_PRODUCER_CONFIG);
            // initialize client
            client = new Client();
        } catch (Exception e) {
            // error
            logger.log(Level.SEVERE, e.getMessage(), e);
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

