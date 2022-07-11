// @title Private Messenger Server - App
// @version 0.0.9
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
    // Socket is a class for socket.
    private static final Socket SOCKET = getSocket();

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

    private static Socket getSocket() {
        try {
            return new Socket(Constants.HOST, Constants.PORT);
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
        // KafkaConsumer is a class for kafka consumer.
        KAFKA_CONSUMER.subscribe(TOPIC);
        // KafkaProducer is a class for kafka producer.
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC.iterator().next(), MESSAGE);
        KAFKA_PRODUCER.send(producerRecord);

        // ExecutorService is a class for thread pool.
        EXECUTOR_SERVICE.execute(() -> {
            // while loop
            while (true) {
                // try catch
                try {
                    // Socket is a class for socket.
                    Socket socket = SERVER_SOCKET.accept();
                    // Socket is a class for socket.
                    SOCKET_LIST.add(socket);
                    // Client is a class for client.
                    Client client = new Client(socket);
                    // ExecutorService is a class for thread pool.
                    EXECUTOR_SERVICE.execute((Runnable) client);
                } catch (IOException e) {
                    // Logger is a class for logging.
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
    }

    // start method is a method for starting.
    private static void start() {
        // while loop
        while (true) {
            // try catch
            try {
                // ConsumerRecords is a class for consumer records.
                ConsumerRecords<String, String> consumerRecords = KAFKA_CONSUMER.poll(Duration.ofMillis(100));
                // ConsumerRecords is a class for consumer records.
                consumerRecords.forEach(record -> {
                    // String is a class for string.
                    String message = record.value();
                    // String is a class for string.
                    String messageType = message.split(Constants.DELIMITER)[0];
                    // String is a class for string.
                    String messageBody = message.split(Constants.DELIMITER)[1];
                });
                // TimeUnit is a class for time unit.
                TimeUnit.SECONDS.sleep(1);

                // ProducerRecord is a class for producer record.
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC.iterator().next(), MESSAGE);
                // KafkaProducer is a class for kafka producer.
                KAFKA_PRODUCER.send(producerRecord);

                // TimeUnit is a class for time unit.
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                // Logger is a class for logging.
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
    // shutdown method is a method for shutting down.
    private static void shutdown() {
        // try catch block
        try {
            // KafkaConsumer is a class for kafka consumer.
            KAFKA_CONSUMER.close();
            // KafkaProducer is a class for kafka producer.
            KAFKA_PRODUCER.close();
            // ServerSocket is a class for server socket.
            SERVER_SOCKET.close();
            // ExecutorService is a class for thread pool.
            EXECUTOR_SERVICE.shutdown();
            // ExecutorService is a class for thread pool.
            EXECUTOR_SERVICE.awaitTermination(1, TimeUnit.MINUTES);
            // List is a class for list.
            for (Socket socket : SOCKET_LIST) {
                // Socket is a class for socket.
                socket.close();
            }
            // close method is a method for closing.
            SOCKET.close();
        } catch (IOException | InterruptedException e) {
            // Logger is a class for logging.
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // Logger is a class for logging.
        LOGGER.info("Shutdown complete.");

        // System is a class for system.
        System.exit(0);
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
    // String is a class for string.
    private String messageType;
    // String is a class for string.
    private String messageBody;
    // String is a class for string.
    private String messageResponse;
    // String is a class for string.
    private String messageError;
    // String is a class for string.
    private String messageNotification;
    // String is a class for string.
    private String messageRequest;
    // String is a class for string.

    // constructor
    public Client(Socket socket) {
        // Socket is a class for socket.
        this.socket = socket;
        // String is a class for string.
        this.message = null;
        // String is a class for string.
        this.messageType = null;
        // String is a class for string.
        this.messageBody = null;
        // String is a class for string.
        this.messageResponse = null;
        // String is a class for string.
        this.messageError = null;
        // String is a class for string.
        this.messageNotification = null;
        // String is a class for string.
        this.messageRequest = null;

        // try catch block
        try {
            // BufferedReader is a class for buffered reader.
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // BufferedWriter is a class for buffered writer.
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            // Logger is a class for logging.
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // Logger is a class for logging.
        LOGGER.info("Client connected.");

        // try catch block
        try {
            // String is a class for string.
            this.message = this.bufferedReader.readLine();
            // String is a class for string.
            this.messageType = this.message.substring(0, this.message.indexOf(":"));
            // String is a class for string.
            this.messageBody = this.message.substring(this.message.indexOf(":") + 1);
        } catch (IOException e) {
            // Logger is a class for logging.
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // Logger is a class for logging.
        LOGGER.info("Client message received.");

        // try catch block
        try {
            // String is a class for string.
            this.messageResponse = this.messageType + ":response";
            // String is a class for string.
            this.messageError = this.messageType + ":error";
            // String is a class for string.
            this.messageNotification = this.messageType + ":notification";
            // String is a class for string.
            this.messageRequest = this.messageType + ":request";
        } catch (Exception e) {
            // Logger is a class for logging.
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // Logger is a class for logging.
        LOGGER.info("Client message types received.");
    }
    
    // getMessage is a method for get message.
    public String getMessage() {
        // String is a class for string.
        return this.message;
    }

    // getMessageType is a method for get message type.
    public String getMessageType() {
        // String is a class for string.
        return this.messageType;
    }

    // getMessageBody is a method for get message body.
    public String getMessageBody() {
        // String is a class for string.
        return this.messageBody;
    }

    // getMessageResponse is a method for get message response.
    public String getMessageResponse() {
        // String is a class for string.
        return this.messageResponse;
    }

    // getMessageError is a method for get message error.
    public String getMessageError() {
        // String is a class for string.
        return this.messageError;
    }

    // getMessageNotification is a method for get message notification.
    public String getMessageNotification() {
        // String is a class for string.
        return this.messageNotification;
    }

    // getMessageRequest is a method for get message request.
    public String getMessageRequest() {
        // String is a class for string.
        return this.messageRequest;
    }

    // setMessage is a method for set message.
    public void setMessage(String message) {
        // String is a class for string.
        this.message = message;
    }

    // setMessageType is a method for set message type.
    public void setMessageType(String messageType) {
        // String is a class for string.
        this.messageType = messageType;
    }

    // setMessageBody is a method for set message body.
    public void setMessageBody(String messageBody) {
        // String is a class for string.
        this.messageBody = messageBody;
    }

    // setMessageResponse is a method for set message response.
    public void setMessageResponse(String messageResponse) {
        // String is a class for string.
        this.messageResponse = messageResponse;
    }

    // setMessageError is a method for set message error.
    public void setMessageError(String messageError) {
        // String is a class for string.
        this.messageError = messageError;
    }

    // setMessageNotification is a method for set message notification.
    public void setMessageNotification(String messageNotification) {
        // String is a class for string.
        this.messageNotification = messageNotification;
    }

    // setMessageRequest is a method for set message request.
    public void setMessageRequest(String messageRequest) {
        // String is a class for string.
        this.messageRequest = messageRequest;
    }

    // toString is a method for to string.
    @Override
    public String toString() {
        // String is a class for string.
        return "Message{" +
                "message='" + message + '\'' +
                ", messageType='" + messageType + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", messageResponse='" + messageResponse + '\'' +
                ", messageError='" + messageError + '\'' +
                ", messageNotification='" + messageNotification + '\'' +
                ", messageRequest='" + messageRequest + '\'' +
                '}';
    }

    @Override
    public void run() {
        // try catch block
        try {
            // BufferedWriter is a class for buffered writer.
            this.bufferedWriter.write(this.message);
            // BufferedWriter is a class for buffered writer.
            this.bufferedWriter.newLine();
            // BufferedWriter is a class for buffered writer.
            this.bufferedWriter.flush();
        } catch (IOException e) {
            // Logger is a class for logging.
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // Logger is a class for logging.
        LOGGER.info("Client message sent.");
    }

    // close is a method for close.
    public void close() {
        // try catch block
        try {
            // BufferedReader is a class for buffered reader.
            this.bufferedReader.close();
            // BufferedWriter is a class for buffered writer.
            this.bufferedWriter.close();
            // Socket is a class for socket.
            this.socket.close();
        } catch (IOException e) {
            // Logger is a class for logging.
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        // Logger is a class for logging.
        LOGGER.info("Client closed.");
    }
}