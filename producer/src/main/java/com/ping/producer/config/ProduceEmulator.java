package com.ping.producer.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ping.producer.entity.Message;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@AllArgsConstructor
public class ProduceEmulator {
    private final KafkaTemplate kafkaTemplate;


    @Scheduled(cron = "* * * * * ? ")
    public void emulatorDataSend()
    {
        for(int i=0;i<10;i++) {

            Message message = new Message();
            message.setSenorId(ThreadLocalRandom.current().nextInt(101, 106));
            message.setSenorTime(System.currentTimeMillis());
            message.setSenorData(UUID.randomUUID().toString());
            //message.setSenorData("rawData:id," + message.getSenorId() + ",time:" + message.getSenorTime());

            kafkaTemplate.send("message-queue", message).addCallback(new ListenableFutureCallback() {
                @Override
                public void onFailure(Throwable throwable) {


                }

                @Override
                public void onSuccess(Object o) {

                }
            });
        }
    }
}
