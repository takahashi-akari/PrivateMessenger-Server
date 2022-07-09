// @title Private Messenger Server
// @version 0.0.4
// @author Takahashi Akari <akaritakahashioss@gmail.com>
// @date 2022-07-09
// @description This is a private messenger server.
// @license MIT License
// @copyright (c) 2020 Takahashi Akari <akaritakahashioss@gmail.com>
// @url <https://akari.mn/privatemessenger>
package mn.akari.maven.privatemessengerserver;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.requests.DescribeConfigsResponse.Config;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import mn.akari.maven.privatemessengerserver.Constants;

// Class App is main Class of Private Messenger Server.
public class App {
    // Logger is used to log.
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    // KafkaConsumer is used to consume messages from Kafka.
    private static KafkaConsumer<String, String> kafkaConsumer;
    // KafkaProducer is used to produce messages to Kafka.
    private static KafkaProducer<String, String> kafkaProducer;
    // ExecutorService is used to execute tasks.
    private static ExecutorService executorService;
    // ServerSocket is used to listen to connections.
    private static ServerSocket serverSocket;
    // List<Socket> is used to store sockets.
    private static List<Socket> socketList = new ArrayList<>();
    // List<Thread> is used to store threads.
    private static List<Thread> threadList = new ArrayList<>();
    // List<String> is used to store messages.
    private static List<String> messageList = new ArrayList<>();

    // Main method is used to execute program.
    public static void main(String[] args) {
        // Kafka Consumer Properties.
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.KAFKA_SERVER_URL);
        kafkaConsumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, Constants.KAFKA_CONSUMER_GROUP_ID);
        kafkaConsumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Constants.KAFKA_KEY_DESERIALIZER_CLASS);
        kafkaConsumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Constants.KAFKA_VALUE_DESERIALIZER_CLASS);
        kafkaConsumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Constants.KAFKA_AUTO_OFFSET_RESET_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Constants.KAFKA_ENABLE_AUTO_COMMIT_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, Constants.KAFKA_AUTO_COMMIT_INTERVAL_MS_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, Constants.KAFKA_SESSION_TIMEOUT_MS_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, Constants.KAFKA_MAX_PARTITION_FETCH_BYTES_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, Constants.KAFKA_MAX_POLL_RECORDS_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Constants.KAFKA_MAX_POLL_INTERVAL_MS_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, Constants.KAFKA_FETCH_MIN_BYTES_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, Constants.KAFKA_FETCH_MAX_WAIT_MS_CONFIG);
        kafkaConsumerProperties.setProperty(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, Constants.KAFKA_FETCH_MAX_BYTES_CONFIG);
        
        // Kafka Producer Properties.
        Properties kafkaProducerProperties = new Properties();
        kafkaProducerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.KAFKA_SERVER_URL);
        kafkaProducerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Constants.KAFKA_KEY_SERIALIZER_CLASS);
        kafkaProducerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Constants.KAFKA_VALUE_SERIALIZER_CLASS);
        kafkaProducerProperties.setProperty(ProducerConfig.ACKS_CONFIG, Constants.KAFKA_ACKS_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.RETRIES_CONFIG, Constants.KAFKA_RETRIES_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Constants.KAFKA_BATCH_SIZE_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.LINGER_MS_CONFIG, Constants.KAFKA_LINGER_MS_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, Constants.KAFKA_BUFFER_MEMORY_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, Constants.KAFKA_CLIENT_ID_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, Constants.KAFKA_REQUEST_TIMEOUT_MS_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, Constants.KAFKA_RECONNECT_BACKOFF_MS_CONFIG);
        kafkaProducerProperties.setProperty(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, Constants.KAFKA_RECONNECT_BACKOFF_MAX_MS_CONFIG);   
        
        // Initialize KafkaConsumer.
        kafkaConsumer = new KafkaConsumer<>(kafkaConsumerProperties);
        // Initialize KafkaProducer.
        kafkaProducer = new KafkaProducer<>(kafkaProducerProperties);
        // Initialize ExecutorService.
        executorService = Executors.newFixedThreadPool(Constants.THREAD_NUMBER);
        // Initialize ServerSocket.
        try {
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize ServerSocket.", e);
            System.exit(1);
        }

        // Initialize Thread.
        Thread thread = new Thread(() -> {
            // Listen to connections.
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    // Add socket to socketList.
                    socketList.add(socket);
                    // Add thread to threadList.
                    threadList.add(new Thread(() -> {
                        // Receive messages.
                        while (true) {
                            try {
                                String message = socket.getInputStream().readUTF();
                                // Add message to messageList.
                                messageList.add(message);
                            } catch (IOException e) {
                                LOGGER.log(Level.SEVERE, "Failed to receive message.", e);
                                break;
                            }
                        }
                    }));
                    // Start thread.
                    threadList.get(threadList.size() - 1).start();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to accept connection.", e);
                    break;
                }
            }
        });
        // Start thread.
        thread.start();
        // Listen to messages.
        while (true) {
            try {
                // Get message.
                String message = messageList.take();
                // Send message to Kafka.
                kafkaProducer.send(new ProducerRecord<>(Constants.KAFKA_TOPIC, message));
            } catch (InterruptedException e) {
                
                // Consume messages.
                ConsumerRecords<String, String>
                records = kafkaConsumer.poll(Duration.ofMillis(Constants.KAFKA_POLL_INTERVAL_MS_CONFIG));
                // Iterate records.
                for (ConsumerRecord<String, String> record : records) {
                    // Send message to KafkaProducer.
                    kafkaProducer.send(new ProducerRecord<>(Constants.KAFKA_PRODUCER_TOPIC, record.value()));
                }
            } catch (KafkaException e) {
                LOGGER.log(Level.SEVERE, "Failed to consume messages.", e);
                break;
            }

            // Iterate messageList.
            for (String message : messageList) {
                // Iterate socketList.
                for (Socket socket : socketList) {
                    // Send message to socket.
                    try {
                        socket.getOutputStream().writeUTF(message);
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Failed to send message.", e);
                        break;
                    }
                }
            }
            // Clear messageList.
            messageList.clear();
            // Sleep.
            try {
                Thread.sleep(Constants.KAFKA_CONSUMER_POLL_TIMEOUT);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Failed to sleep.", e);
                break;
            }

            // Iterate threadList.
            for (Thread t : threadList) {
                // Check thread.
                if (!t.isAlive()) {
                    // Remove thread.
                    threadList.remove(t);
                }
            }
            // Iterate socketList.
            for (Socket socket : socketList) {
                // Check socket.
                if (!socket.isConnected()) {
                    // Remove socket.
                    socketList.remove(socket);
                }
            }

            // Check threadList.
            if (threadList.isEmpty()) {
                // Stop executorService.
                executorService.shutdown();
                // Wait for executorService.
                try {
                    executorService.awaitTermination(Constants.KAFKA_CONSUMER_POLL_TIMEOUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Failed to wait for executorService.", e);
                    break;
                }
                // Stop KafkaConsumer.
                kafkaConsumer.close();
                // Stop KafkaProducer.
                kafkaProducer.close();
                // Stop ServerSocket.
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to close ServerSocket.", e);
                    break;
                }
                // Stop Thread.
                thread.interrupt();
                // Stop Program.
                System.exit(0);
            }

            // Check socketList.
            if (socketList.isEmpty()) {
                // Stop executorService.
                executorService.shutdown();
                // Wait for executorService.
                try {
                    executorService.awaitTermination(Constants.KAFKA_CONSUMER_POLL_TIMEOUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Failed to wait for executorService.", e);
                    break;
                }
                // Stop KafkaConsumer.
                kafkaConsumer.close();
                // Stop KafkaProducer.
                kafkaProducer.close();
                // Stop ServerSocket.
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to close ServerSocket.", e);
                    break;
                }
                // Stop Thread.
                thread.interrupt();
                // Stop Program.
                System.exit(0);
            }

            // Check messageList.
            if (messageList.isEmpty()) {
                // Sleep.
                try {
                    Thread.sleep(Constants.KAFKA_CONSUMER_POLL_TIMEOUT);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Failed to sleep.", e);
                    break;
                }
            }
        }
    }
}
// End of App.java.
// End of Private Messenger Server.