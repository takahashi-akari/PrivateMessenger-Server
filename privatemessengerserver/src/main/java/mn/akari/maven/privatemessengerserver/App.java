// @title Private Messenger Server
// @version 0.0.7
// @author Takahashi Akari <akaritakahashioss@gmail.com>
// @date 2022-07-09
// @description This is a private messenger server.
// @license MIT License
// @copyright (c) 2022 Takahashi Akari <akaritakahashioss@gmail.com>
// @url <https://takahashi-akari.github.io/PrivateMessenger/>
// @see https://github.com/takahashi-akari/PrivateMessenger-Server/blob/main/privatemessengerserver/src/main/java/mn/akari/maven/privatemessengerserver/App.java
// @see https://github.com/takahashi-akari/PrivateMessenger-Server/blob/main/privatemessengerserver/src/main/java/mn/akari/maven/privatemessengerserver/Constsans.java
// @see https://github.com/takahashi-akari/PrivateMessenger-Server/blob/main/README.md


package mn.akari.maven.privatemessengerserver;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import mn.akari.maven.privatemessengerserver.Constants;

// App class is a main class of this project.
public class App {
    // Logger is a class for logging.
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    // ExecutorService is a class for thread pool.
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    // ServerSocket is a class for server socket.
    private static final ServerSocket SERVER_SOCKET = getSocket();
    // List is a class for list.
    private static final List<Socket> SOCKET_LIST = new ArrayList<>();
    // KafkaConsumer is a class for kafka consumer.
    private static final KafkaConsumer<String, String> KAFKA_CONSUMER = new KafkaConsumer<>(Constants.KAFKA_PROPERTIES);
    // KafkaProducer is a class for kafka producer.
    private static final KafkaProducer<String, String> KAFKA_PRODUCER = new KafkaProducer<>(Constants.KAFKA_PROPERTIES);
    // String is a class for string.
    private static final Collection<String> TOPIC = Constants.TOPIC;
    // String is a class for string.
    private static final String MESSAGE = Constants.MESSAGE;
    // String is a class for string.
    private static final String MESSAGE_TYPE = Constants.MESSAGE_TYPE;
    // String is a class for string.
    private static final String MESSAGE_TYPE_REQUEST = Constants.MESSAGE_TYPE_REQUEST;
    // String is a class for string.
    private static final String MESSAGE_TYPE_RESPONSE = Constants.MESSAGE_TYPE_RESPONSE;
    // String is a class for string.
    private static final String MESSAGE_TYPE_NOTIFICATION = Constants.MESSAGE_TYPE_NOTIFICATION;
    // String is a class for string.
    private static final String MESSAGE_TYPE_ERROR = Constants.MESSAGE_TYPE_ERROR;

    // Main method is a main method of this project.
    public static void main(String[] args) {
        // initialize
        LOGGER.info("Initializing...");
        // subscribe
        LOGGER.info("Subscribing...");
        KAFKA_CONSUMER.subscribe(TOPIC);
        // listen
        LOGGER.info("Listening...");
        while (true) {
            // accept
            try {
                Socket socket = SERVER_SOCKET.accept();
                // add
                SOCKET_LIST.add(socket);
                // run
                EXECUTOR_SERVICE.execute(() -> {
                    // receive
                    LOGGER.info("Receiving...");
                    String message = receive(socket);
                    // end
                    if (message == null) {
                        // remove
                        SOCKET_LIST.remove(socket);
                        // close
                        LOGGER.info("Closing...");
                        close(KAFKA_CONSUMER);
                        close(KAFKA_PRODUCER);
                        close(SERVER_SOCKET);
                        close(EXECUTOR_SERVICE);
                        close(SOCKET_LIST);
                        LOGGER.info("Closed.");
                        // return
                        return;
                    }
                    // parse
                    LOGGER.info("Parsing...");
                    String[] messageArray = parse(message);
                    // process
                    LOGGER.info("Processing...");
                    process(messageArray);
                });
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private static void close(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static ServerSocket getSocket() {
        try {
            return new ServerSocket(Constants.PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(1);
        }
        return null;
    }

    private static void process(String[] messageArray) {
        // request
        if (messageArray[0].equals(MESSAGE_TYPE_REQUEST)) {
            // send
            LOGGER.info("Sending...");
            send(messageArray[1]);
        }
        // response
        else if (messageArray[0].equals(MESSAGE_TYPE_RESPONSE)) {
            // send
            LOGGER.info("Sending...");
            send(messageArray[1]);
        }
        // notification
        else if (messageArray[0].equals(MESSAGE_TYPE_NOTIFICATION)) {
            // send
            LOGGER.info("Sending...");
            send(messageArray[1]);
        }
        // error
        else if (messageArray[0].equals(MESSAGE_TYPE_ERROR)) {
            // send
            LOGGER.info("Sending...");
            send(messageArray[1]);
        }
    }

    private static void send(String string) {
        // send
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("topic", string);
        KAFKA_PRODUCER.send(record);
    }

    private static void close(List<Socket> socketList) {
        for (Socket socket : socketList) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private static void close(ExecutorService executorService) {
        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void close(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void close(KafkaProducer<String, String> kafkaProducer) {
        if (kafkaProducer != null) {
            kafkaProducer.close();
        }
    }

    private static void close(KafkaConsumer<String, String> kafkaConsumer) {
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
        }
    }

    private static void send(String string, String string2) {
        // send
        KAFKA_PRODUCER.send(new ProducerRecord<String, String>(string, string2));
    }

    private static String receive(Socket socket) {
        // receive
        try {
            DataInputStream is = (DataInputStream) socket.getInputStream();
            return is.readUTF();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    private static String[] parse(String message2) {
        // parse
        return message2.split(Constants.DELIMITER);
    }
}
