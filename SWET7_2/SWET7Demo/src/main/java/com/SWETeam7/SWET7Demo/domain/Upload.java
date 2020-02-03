package com.SWETeam7.SWET7Demo.domain;

import lombok.Data;

import java.util.Calendar;

@Data
public class Upload {
    private int uploadID;
    private long imageID;
    private Calendar uploadTime;
}
