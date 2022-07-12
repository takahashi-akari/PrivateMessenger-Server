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

import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.BufferedWriter;

// class App

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        logger.info("Started");
        // initialize
        initialize();

        // sleep loop
        while (true) {
            // sleep
            try {
                Thread.sleep(Duration.ofSeconds(1).toMillis());
            } catch (InterruptedException e) {
                // log
                logger.severe(e.getMessage());

                // shutdown
                shutdown();

                // exit
                // log
                logger.info("Exited");
                System.exit(1);
            }

            // check
            // log
            check();

        }
    }
    
    private static void shutdown() {
        // shutdown
        // log
        logger.info("Shutdown");
        executorService.shutdown();
        kafkaConsumer.close();
        kafkaProducer.close();
        client.close();
        // log
        logger.info("Shutdown complete");
    }

    private static void check() {
        // log
        logger.info("Check");
        // check kafka consumer
        if (kafkaConsumer == null) {
            // initialize kafka consumer
            initializeKafkaConsumer();
        }
        
        // check kafka producer
        if (kafkaProducer == null) {
            // initialize kafka producer
            initializeKafkaProducer();
        }
        
        // check client
        if (client == null) {
            // initialize client
            initializeClient();
        }
        // log
        logger.info("Check complete");
    }

    private static void runExecutorService() {
        // log
        logger.info("Run executor service");
        // run executor service
        executorService.execute(() -> {
            // run kafka consumer
            runKafkaConsumer();
        });

        executorService.execute(() -> {
            // run kafka producer
            runKafkaProducer();
        });

        executorService.execute(() -> {
            // run client
            client.run();
        });
        // log
        logger.info("Run executor service complete");
    }

    private static void runKafkaProducer() {
        // log
        logger.info("Run kafka producer");
        String key = Constants.KAFKA_PRODUCER_KEY;
        String value = Constants.KAFKA_PRODUCER_VALUE;
        // run kafka producer
        kafkaProducer.send(new ProducerRecord<String, String>(Constants.KAFKA_TOPIC_NAME, key, value));
        // log
        logger.info("Run kafka producer complete");
    }

    private static void runKafkaConsumer() {
        // log
        logger.info("Run kafka consumer");
        // run kafka consumer
        Duration duration = Duration.ofSeconds(1);
        ConsumerRecords<String, String> records = kafkaConsumer.poll(duration);
        for (ConsumerRecord<String, String> record : records) {
            logger.info(record.value());
        }
        // log
        logger.info("Run kafka consumer complete");
    }

    private static void initialize() {
        // log
        logger.info("Initialize");
        // initialize client
        initializeClient();
        // initialize kafka consumer
        initializeKafkaConsumer();
        // initialize kafka producer
        initializeKafkaProducer();
        // initialize executor service
        runExecutorService();
        // log
        logger.info("Initialize complete");
    }

    private static void initializeKafkaConsumer() {
        // log
        logger.info("Initialize kafka consumer");
        // create kafka consumer
        kafkaConsumer = new KafkaConsumer<>(Constants.KAFKA_CONSUMER_PROPERTIES);
        // subscribe
        kafkaConsumer.subscribe(Collections.singletonList(Constants.KAFKA_TOPIC_NAME));
        // log
        logger.info("Initialize kafka consumer complete");
    }
    private static void initializeKafkaProducer() {
        // log
        logger.info("Initialize kafka producer");
        // create kafka producer
        kafkaProducer = new KafkaProducer<>(Constants.KAFKA_PRODUCER_PROPERTIES);
        // log
        logger.info("Initialize kafka producer complete");
    }
    private static void initializeClient() {
        // log
        logger.info("Initialize client");
        // create client
        client = new Client();
        // log
        logger.info("Initialize client complete");
    }
}

// Client class is a class for client.
class Client {
    // Logger is a class for logging.
    private static Logger logger = Logger.getLogger(Client.class.getName());
    // ServerSocket is a class for server socket.
    private static ServerSocket serverSocket;
    // Socket is a class for socket.
    private static Socket socket;
    // BufferedReader is a class for buffered reader.
    private static BufferedReader reader;
    // BufferedWriter is a class for buffered writer.
    private static BufferedWriter writer;
    // message is a string for message.
    private static String message;
    // response is a string for response.
    private static String response;

    // constructor
    public Client() {
        try {
            // create server socket
            serverSocket = new ServerSocket(Constants.PORT);
            // create socket
            socket = serverSocket.accept();
            // create reader
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // create writer
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            // log
            logger.severe(e.getMessage());
        }
    }
    public void close() {
        try {
            // close socket
            socket.close();
            // close server socket
            serverSocket.close();
        } catch (Exception e) {
            // log
            logger.severe(e.getMessage());
        }
    }
    // run method is a method for running this class.
    public void run() {
        // loop
        while (true) {
            // sleep
            try {
                Thread.sleep(Duration.ofSeconds(1).toMillis());

                // receive message
                message = reader.readLine();

                // send response
                response = message;
                writer.write(response);
                writer.newLine();
                writer.flush();
            } catch (Exception e) {
                // log
                logger.severe(e.getMessage());
            }
        }
    }
    // getMessage method is a method for getting message.
    public String getMessage() {
        return message;
    }
    // getResponse method is a method for getting response.
    public String getResponse() {
        return response;
    }
}
