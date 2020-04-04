package com.javi.rest;

import com.javi.service.ProducerService;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
public class SearchController {

    ProducerService producerService;

    @RequestMapping(value = "/consumer/startstream", method = RequestMethod.GET)
    public ResponseEntity<Void> searchHashtag(@NotNull @ApiParam(required = true) @Valid @RequestParam(value = "hashtag") String hashtag) {
        producerService.startProducer(hashtag);
        return ResponseEntity.ok().build();
    }
}
