package com.back;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Rq {
    private String cmd;

    public Rq(String cmd) {
        this.cmd = cmd;
    }

    public String getActionName() {
        return cmd.split("\\?")[0];
    }

    public String getParam(String inputKey, String defaultValue) {

        Map<String, String> paramMap = new HashMap<>();

        String[] cmdBits = cmd.split("\\?");

        if(cmdBits.length < 2) {
            return defaultValue;
        }

        String queryString = cmdBits[1];
        String[] queryBits = queryString.split("&");

        paramMap = Arrays.stream(queryBits)
                .map(param -> param.split("="))
                .filter(bits -> bits.length == 2 && bits[0] != null && bits[1] != null)
                .collect(
                        Collectors.toMap(
                                bits -> bits[0],
                                bits -> bits[1]
                        )
                );

        return paramMap.getOrDefault(inputKey, defaultValue);
    }

    public int getParamAsInt(String key, int defaultValue) {

        String value = getParam(key, "");

        if(value.isBlank()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }

    }
}