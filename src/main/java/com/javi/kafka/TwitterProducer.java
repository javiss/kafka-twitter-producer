package com.javi.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.config.KafkaProducerClient;
import com.javi.twitter.TwitterClient;
import com.javi.twitter.model.Tweet;
import com.twitter.hbc.core.Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;


@Slf4j
@Component
public class TwitterProducer {

    final TwitterClient twitterClient;

    final KafkaProducerClient kafkaProducerClient;


    public TwitterProducer(TwitterClient client, KafkaProducerClient kafkaProducerClient) {
        this.twitterClient = client;
        this.kafkaProducerClient = kafkaProducerClient;
    }

    public void start(String terms) {
        Client client = this.twitterClient.createClient(terms);

        Runtime.getRuntime().addShutdownHook(new Thread(client::stop));

        ObjectMapper mapper = getObjectMapper();
        KafkaProducer<Integer, String> kafkaProducer = kafkaProducerClient.createKafkaProducer();

        long started = currentTimeMillis();
        float nResults = 0f;
        while (!client.isDone()) {
            Tweet msg = null;
            String rawMsg = null;
            DecimalFormat df = new DecimalFormat("0.##");

            try {
                rawMsg = twitterClient.getMsgQueue().poll(2, TimeUnit.SECONDS);
                if (rawMsg != null) {
                    msg = mapper.readValue(rawMsg, Tweet.class);
                    nResults++;
                    log.info("{} tweets/s", df.format(nResults / ((currentTimeMillis() - started) / 1000f)));
                }
            } catch (InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
                client.stop();
            }
            if (msg != null)
                //  log.warn("Lang {} \tLocation {} \t{}", msg.getLang(), msg.getUser().getLocation(), msg.getText());
                kafkaProducer.send(new ProducerRecord<>("tweets", null, rawMsg), (recordMetadata, e) -> {

                    if (e != null) log.error("Cant send message", e);

                });
        }
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

}
