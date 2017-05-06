/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mitosis.spring5app.bean;

import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 *
 * @author root
 */
public class Listener {

    public final CountDownLatch countDownLatch1 = new CountDownLatch(1);

    @KafkaListener(id = "mitosis-group", topics = "topic-mitosis", group="mitosis")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println(record);
        countDownLatch1.countDown();
    }

}
