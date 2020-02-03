package com.SWETeam7.SWET7Demo.domain;

import lombok.Data;

@Data
public class User {
    private String userID;
    private ChocoMush chocoMush;
    private Record record;

    @Data
    public static class ChocoMush{
        private int balance;
    }
}
