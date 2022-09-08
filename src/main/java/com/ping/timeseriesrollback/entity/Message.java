package com.ping.timeseriesrollback.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer senorId;
    private String senorData;
    private Long senorTime;
}
