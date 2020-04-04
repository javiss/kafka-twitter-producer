package com.javi.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Getter
public class TwitterClient {

    final TwitterClientCredentials credentials;

    // Blocking queue, size according to the expected twitts per second
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);
    private BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(1000);

    public TwitterClient(TwitterClientCredentials credentials) {
        this.credentials = credentials;
    }

    public Client createClient(String searchTerms) {

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);

        ClientBuilder builder = createFilterEndpoint(hosebirdHosts, searchTerms);

        Client hosebirdClient = builder.build();

        hosebirdClient.connect();

        // will shutdown the connection after certain time so it won't abuse the twitter API
        CompletableFuture.delayedExecutor(1, TimeUnit.MINUTES).execute(hosebirdClient::stop);

        return hosebirdClient;
    }

    private ClientBuilder createFilterEndpoint(Hosts hosebirdHosts, String searchTerms) {

        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Tracking terms...
        List<Long> followings = Lists.newArrayList(1000L, 5000000L);
        List<String> terms = Lists.newArrayList(searchTerms);
        hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(
            credentials.getConsumerKey(),
            credentials.getConsumerSecret(),
            credentials.getToken(),
            credentials.getSecret()
        );

        return new ClientBuilder()
            .name("Client0")
            .hosts(hosebirdHosts)
            .authentication(hosebirdAuth)
            .endpoint(hosebirdEndpoint)
            .processor(new StringDelimitedProcessor(msgQueue))
            .eventMessageQueue(eventQueue);
    }

    @PostConstruct
    private void initClient() {
        // test the client on startup
        createClient("not_used").stop();
    }

}
