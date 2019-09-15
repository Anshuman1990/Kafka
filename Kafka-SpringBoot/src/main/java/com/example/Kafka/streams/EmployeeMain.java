package com.example.Kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class EmployeeMain {
    private static final String TOPIC = "employee-input";
    private static final Logger logger = LoggerFactory.getLogger(EmployeeMain.class);
    private static final java.util.Date date = new Date(2019, 6, 1);

    public static void main(String[] args) {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-word");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.9.250:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put("group.id", "employee-consumer-group");
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", EmployeeDeserializer.class);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        // Note: To re-run the demo, you need to use the offset reset tool:
        // https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Application+Reset+Tool
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        final StreamsBuilder builder = new StreamsBuilder();

        JsonDeserializer<Employee> employeeJsonDeserializer = new JsonDeserializer<>(Employee.class);
        JsonSerializer<Employee> employeeJsonSerializer = new JsonSerializer<>();

        Serde<Employee> employeeSerde = Serdes.serdeFrom(employeeJsonSerializer, employeeJsonDeserializer);

        Serde<String> stringSerde = Serdes.String();

        final KStream<String, Employee> source = builder.stream(TOPIC, Consumed.with(stringSerde, employeeSerde));


        source.foreach((key, value) -> {
            logger.info((Period.between(LocalDate.of(value.getDoj().getYear(), value.getDoj().getMonth(), value.getDoj().getDay()), LocalDate.now())).getDays() + "DATE DIFF");
        });

        Predicate<String, Employee> predicate_1 = (key, value) -> value.getScreened();
        Predicate<String, Employee> predicate_2 = ((key, value) -> (Period.between(LocalDate.of(value.getDoj().getYear(), value.getDoj().getMonth(), value.getDoj().getDay()), LocalDate.now())).getDays() > 10);

//        final KStream<String, Employee>[] sourceBranches = source.branch(predicate_1,predicate_2);


        KStream<String, Employee> branch_1 = source.filter(predicate_1);
        KStream<String, Employee> branch_2 = source.filter(predicate_2);
//        logger.info("===========================================branch_1==============================================");
//        branch_1.foreach((key, value) -> {
//            logger.info("branch_1_Key= " + key);
//            logger.info("branch_1_Value= " + value.toString());
//        });
//        logger.info("===========================================branch_2==============================================");
//
//        branch_2.foreach((key, value) -> {
//            logger.info("branch_2_Key= " + key);
//            logger.info("branch_2_Value= " + value.toString());
//        });
        KTable<String, Long> ktable_1 = branch_1.groupByKey().count();
        KTable<String, Long> ktable_2 = branch_2.groupByKey().count();

//        logger.info("===========================================ktable_1==============================================");
//        ktable_1.toStream().foreach((key, value) -> {
//            logger.info("ktable_1_Key= " + key);
//            logger.info("ktable_1_Value= " + value.toString());
//        });
//        logger.info("===========================================ktable_2==============================================");
//
//        ktable_2.toStream().foreach((key, value) -> {
//            logger.info("ktable_2_Key= " + key);
//            logger.info("ktable_2_Value= " + value.toString());
//        });

        ktable_1.join(ktable_2, (value1, value2) -> "isScreenedCount:- " + value1 + " Days Count:-" + value2).toStream().to("employee-output", Produced.with(Serdes.String(), Serdes.String()));

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-wordcount-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

}
