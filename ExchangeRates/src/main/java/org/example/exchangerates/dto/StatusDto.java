package org.example.exchangerates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;

public record StatusDto(@JsonProperty("account_id") String accountId, HashMap<String, HashMap<String, Integer>> quotas) {
}