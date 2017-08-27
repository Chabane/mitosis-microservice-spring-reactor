/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitosis.spring5app.api;

import org.json.JSONObject;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 *
 * @author root
 */
@RestController
public class SpringBootKafkaController {
    
    @Value("${topic}")
    private String topic;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    //@Autowired
    //private Listener listener;

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    public void hello(@NotNull @PathVariable String name, @NotNull @PathVariable CellType cellType) throws ExecutionException, InterruptedException {
        JSONObject cell = new JSONObject();
        cell.put("name", name);
        cell.put("type", cellType);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, cell.toString());
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("failed");
            }
        });
    }
}
