package com.SWETeam7.SWET7Demo.domain;

import lombok.Data;

import java.util.List;

@Data
public class Record {
    private List<Sale> saleList;
    private List<Upload> uploadList;
}
