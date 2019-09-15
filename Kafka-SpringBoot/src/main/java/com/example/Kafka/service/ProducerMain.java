package com.example.Kafka.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.clients.producer.Producer;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Random;

public class ProducerMain {

    public static void main(String[] args) {

        ProducerMain main = new ProducerMain();
        // Check arguments length value

        // create instance for properties to access producer configs
        Properties props1 = new Properties();
        props1.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props1.put("acks", "all");
        props1.put("retries", 0);
        props1.put("batch.size", 16384);
        props1.put("linger.ms", 1);
        props1.put("buffer.memory", 33554432);
        props1.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props1.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        Producer<String, String> producer = new KafkaProducer
                <String, String>(props1);

        for(int i = 0; i < 10; i++) {
            String str = main.givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect();
            producer.send(new ProducerRecord<String, String>("streams-plaintext-input",
                    Integer.toString(i), str));
        }

        for(int i = 0; i < 10; i++) {
            String str = main.givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect();
            producer.send(new ProducerRecord<String, String>("streams-text-input",
                    Integer.toString(i), str));
        }
        System.out.println("Message sent successfully");
        producer.close();
    }
    public String givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
        byte[] array = new byte[7]; // length is bounded by 7
        Random myRandom = new Random();
        new Random().nextBytes(array);
        String generatedString =
                (char) (myRandom.nextInt(26) + 'A') +
                (char) (myRandom.nextInt(26) + 'a') +
                (char) (myRandom.nextInt(26) + 'a')+"";

        return generatedString;
    }


}
