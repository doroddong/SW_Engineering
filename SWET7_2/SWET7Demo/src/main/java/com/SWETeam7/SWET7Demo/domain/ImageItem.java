package com.SWETeam7.SWET7Demo.domain;

import lombok.Data;

@Data
public class ImageItem {
    private String title;
    private String description;
    private int price;
    private String author;

    private long imageID;
    private String localUrl;
    private String serverUrl;
}
