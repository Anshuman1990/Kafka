package com.example.Kafka.streams;

import com.example.Kafka.dto.Employee;
import com.example.Kafka.streams.Serdes.EmployeeSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class EmployeeDemo {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "employee-input";


    private static int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    private static Date createRandomDate() {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(5, 6);

        return new Date(2019, month, day);
    }


    public static void main(String[] args) {


        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-word");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.9.250:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", EmployeeSerializer.class);
        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        // Note: To re-run the demo, you need to use the offset reset tool:
        // https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Application+Reset+Tool
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        Random random = new Random();
        Date randomDate = createRandomDate();
        System.out.println(randomDate);

        Employee emp = new Employee();
        emp.setId(10);
        emp.setDoj(randomDate);
        emp.setScreened( random.nextBoolean());
        System.out.println();

        String str = "data";

        try (Producer<String, Employee> producer = new KafkaProducer<>(props)) {
            producer.send(new ProducerRecord(TOPIC,str, emp));
            System.out.println("Message " + emp.toString() + " sent !!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
