// @title Private Messenger Server - App
// @version 0.0.13
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
import java.net.ServerSocket;
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

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import mn.akari.maven.privatemessengerserver.Constants;
import mn.akari.maven.privatemessengerserver.Client;

// App class is a main class of this project.
public class App {
    // Logger is a class for logging.
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    // ExecutorService is a class for thread pool.
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    // ServerSocket is a class for server socket.
    private static final ServerSocket SERVER_SOCKET = getServerSocket();
    // List is a class for list.
    private static final List<Socket> SOCKET_LIST = new ArrayList<>();
    // KafkaConsumer is a class for kafka consumer.
    private static final KafkaConsumer<String, String> KAFKA_CONSUMER = new KafkaConsumer<>(Constants.KAFKA_PROPERTIES);
    // KafkaProducer is a class for kafka producer.
    private static final KafkaProducer<String, String> KAFKA_PRODUCER = new KafkaProducer<>(Constants.KAFKA_PROPERTIES);
    // String is a class for string.
    private static final String TOPIC = Constants.TOPIC;
    // String is a class for string.
    private static final String MESSAGE = Constants.MESSAGE;
    // Socket is a class for socket.
    private static final Socket SOCKET = getSocket();
    // SocketAddress
    private static final SocketAddress SOCKET_ADDRESS = getSocketAddress();

    // Main method is a main method of this project.
    public static void main(String[] args) {
        // initialize
        LOGGER.info("Initializing...");
        initialize();
        // start
        LOGGER.info("Starting...");
        start();
        // shutdown
        LOGGER.info("Shutdown...");
        shutdown();
    }

    private static SocketAddress getSocketAddress() {
        return new SocketAddress() {
            @Override
            public String toString() {
                return "";
            }
        };
    }

    private static Socket getSocket() {
        try {
            return new Socket(Constants.HOST_NAME, Constants.PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to get socket.", e);
            return null;
        }
    }

    private static ServerSocket getServerSocket() {
        try {
            return new ServerSocket(Constants.PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to get socket.", e);
            return null;
        }
    }

    // initialize method is a method for initializing.
    private static void initialize() {
        // connect to kafka
        LOGGER.info("Connecting to kafka...");
        connectToKafka();
        // connect to socket
        LOGGER.info("Connecting to socket...");
        connectToSocket();
    }

    private static void connectToSocket() {
        try {
            SOCKET.connect(SOCKET_ADDRESS);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to socket.", e);
        }

        if (SOCKET.isConnected()) {
            LOGGER.info("Connected to socket.");
        } else {
            LOGGER.info("Failed to connect to socket.");
        }

        SOCKET_LIST.add(SOCKET);

        if (SOCKET.isClosed()) {
            LOGGER.info("Closed socket.");
        } else {
            LOGGER.info("Failed to close socket.");
        }

        if (SOCKET.isBound()) {
            LOGGER.info("Bound socket.");
        } else {
            LOGGER.info("Failed to bind socket.");
        }

        if (SOCKET.isConnected()) {
            LOGGER.info("Connected to socket.");
        } else {
            LOGGER.info("Failed to connect to socket.");
        }

        if (SOCKET.isClosed()) {
            LOGGER.info("Closed socket.");
        } else {
            LOGGER.info("Failed to close socket.");
        }
    }

    private static void connectToKafka() {
        try {
            KAFKA_CONSUMER.subscribe(Constants.TOPICS);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to kafka.", e);
        }
    }

    // start method is a method for starting.
    private static void start() {
        // start server socket
        LOGGER.info("Starting server socket...");
        startServerSocket();
        // start kafka consumer
        LOGGER.info("Starting kafka consumer...");
        startKafkaConsumer();
        // start kafka producer
        LOGGER.info("Starting kafka producer...");
        startKafkaProducer();
        // start socket
        LOGGER.info("Starting socket...");
        startSocket();
        // start executor service
        LOGGER.info("Starting executor service...");
        startExecutorService();
    }
    private static void startExecutorService() {
        EXECUTOR_SERVICE.execute(() -> {
            while (true) {
                try {
                    // get records
                    ConsumerRecords<String, String> records = KAFKA_CONSUMER.poll(Duration.ofMillis(1000));
                    // get record
                    for (ConsumerRecord<String, String> record : records) {
                        // get message
                        String message = record.value();
                        // send message
                        sendMessage(message);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to get records.", e);
                }
            }
        });
    }

    private static void sendMessage(String message2) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(Constants.TOPIC, message2);
            KAFKA_PRODUCER.send(record);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send message.", e);
        }
    }

    private static void startSocket() {
        try {
            SOCKET.setSoTimeout(Constants.TIMEOUT);
        } catch (SocketException e) {
            LOGGER.log(Level.SEVERE, "Failed to set socket timeout.", e);
        }
        while (true) {
            try {
                // get message
                String message = ((DataInputStream) SOCKET.getInputStream()).readUTF();
                // send message
                sendMessage(message);
            } catch (SocketTimeoutException e) {
                LOGGER.log(Level.SEVERE, "Failed to get message.", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to get message.", e);
            }
        }
    }

    private static void startKafkaProducer() {
        try {
            KAFKA_PRODUCER.initTransactions();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to init transactions.", e);
        }
        while (true) {
            try {
                // get message
                String message = MESSAGE;
                // send message
                sendMessage(message);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to send message.", e);
            }
        }
    }

    private static void startKafkaConsumer() {
        while (true) {
            try {
                // get records
                ConsumerRecords<String, String> records = KAFKA_CONSUMER.poll(Duration.ofMillis(1000));
                // get record
                for (ConsumerRecord<String, String> record : records) {
                    // get message
                    String message = record.value();
                    // send message
                    sendMessage(message);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to get records.", e);
            }
        }
    }

    private static void startServerSocket() {
        try {
            SERVER_SOCKET.setSoTimeout(Constants.TIMEOUT);
        } catch (SocketException e) {
            LOGGER.log(Level.SEVERE, "Failed to set socket timeout.", e);
        }
        while (true) {
            try {
                // get socket
                Socket socket = SERVER_SOCKET.accept();
                // add socket
                SOCKET_LIST.add(socket);
                // get message
                // socket to string
                String message = socket.toString();
                // send message
                sendMessage(message);
            } catch (SocketTimeoutException e) {
                LOGGER.log(Level.SEVERE, "Failed to get socket.", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to get socket.", e);
            }
        }
    }

    // shutdown method is a method for shutting down.
    private static void shutdown() {
        // shutdown server socket
        LOGGER.info("Shutting down server socket...");
        shutdownServerSocket();
        // shutdown kafka consumer
        LOGGER.info("Shutting down kafka consumer...");
        shutdownKafkaConsumer();
        // shutdown kafka producer
        LOGGER.info("Shutting down kafka producer...");
        shutdownKafkaProducer();
        // shutdown socket
        LOGGER.info("Shutting down socket...");
        shutdownSocket();
        // shutdown executor service
        LOGGER.info("Shutting down executor service...");
        shutdownExecutorService();
    }

    private static void shutdownExecutorService() {
        EXECUTOR_SERVICE.shutdown();
        try {
            EXECUTOR_SERVICE.awaitTermination(Constants.TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Failed to await termination.", e);
        }
    }

    private static void shutdownSocket() {
        try {
            SOCKET.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to close socket.", e);
        }
    }

    private static void shutdownKafkaProducer() {
        try {
            KAFKA_PRODUCER.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to close kafka producer.", e);
        }
    }

    private static void shutdownKafkaConsumer() {
        try {
            KAFKA_CONSUMER.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to close kafka consumer.", e);
        }
    }

    private static void shutdownServerSocket() {
        try {
            SERVER_SOCKET.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to close server socket.", e);
        }
    }
}
// Client is a class for client.
class Client implements Runnable {
    // Logger is a class for logging.
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    // Socket is a class for socket.
    private Socket socket;
    // BufferedReader is a class for buffered reader.
    private BufferedReader bufferedReader;
    // BufferedWriter is a class for buffered writer.
    private BufferedWriter bufferedWriter ;
    // String is a class for string.
    private String message;

    // constructor
    public Client(Socket socket) {
        // Socket is a class for socket.
        this.socket = socket;
        // String is a class for string.
        this.message = null;

        // BufferedReader is a class for buffered reader.
        try {
            // BufferedReader is a class for buffered reader.
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // BufferedWriter is a class for buffered writer.
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            // Logger is a class for logging.
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    // run method is a method for running.
    @Override
    public void run() {
        // while loop
        while (true) {
            // try catch
            try {
                // String is a class for string.
                String message = bufferedReader.readLine();
                // String is a class for string.
                this.message = message;
                // Logger is a class for logging.
                LOGGER.info(message);
            } catch (IOException e) {
                // Logger is a class for logging.
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
}
