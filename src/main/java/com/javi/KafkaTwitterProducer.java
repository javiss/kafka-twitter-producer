package com.javi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaTwitterProducer {

    public static void main(String[] args) {
        System.out.println("testing1");
        SpringApplication.run(KafkaTwitterProducer.class, args);
    }

}
