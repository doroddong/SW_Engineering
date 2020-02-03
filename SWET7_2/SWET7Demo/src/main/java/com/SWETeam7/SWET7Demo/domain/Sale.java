package com.SWETeam7.SWET7Demo.domain;

import lombok.Data;

import java.util.Calendar;

@Data
public class Sale {
    private int price;
    private int saleID;
    private long imageID;
    private Calendar downloadTime;
}
