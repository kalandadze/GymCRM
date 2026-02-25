package com.example.gymcrmworkload.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.Month;
import java.time.Year;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class TrainerWorkload {
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private Map<Integer, YearlyWorkloadOverview> trainingSummery;

    @Data
    @Getter
    public static class YearlyWorkloadOverview {
        private Map<Month, Double> monthlyWorkload = new EnumMap<>(Month.class);

        public Double getMonthlyWorkload(Month month) {
            return monthlyWorkload.get(month);
        }

        public void addMonthlyWorkload(Month month, Double workload) {
            monthlyWorkload.compute(month, (k, v) -> (v == null) ? workload : Math.max(v + workload, 0));
        }
    }
}
