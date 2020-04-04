package com.javi.service;

import com.javi.kafka.TwitterProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableAsync
@AllArgsConstructor
public class ProducerService {

    final TwitterProducer twitterProducer;

    @Async
    public void startProducer(String searchTerms) {
        log.info("Starting producer with search terms - {}", searchTerms);
        twitterProducer.start(searchTerms);
    }
}
