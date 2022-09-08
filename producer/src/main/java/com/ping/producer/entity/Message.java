package com.ping.producer.entity;

import lombok.Data;

@Data
public class Message {
    private Integer senorId;
    private String senorData;
    private Long senorTime;
}
