package com.javi.config;

import lombok.Getter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Getter
@Component
public class KafkaProducerClient {

    private KafkaCredentials credentials;

    public KafkaProducerClient(KafkaCredentials credentials) {
        this.credentials = credentials;
    }

    public KafkaProducer<Integer, String> createKafkaProducer() {

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, credentials.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, credentials.getClientId());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 5);

        // compression adn batching, more CPU usage and latency but allows higher throughput
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 300);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 64 * 1024);


        return new KafkaProducer<>(props);
    }

}

