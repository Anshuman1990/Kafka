package com.example.Kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class EmployeeDeserializer implements Deserializer<Employee> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Employee deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        Employee user = null;
        try {
            user = mapper.readValue(data, Employee.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void close() {

    }
}
