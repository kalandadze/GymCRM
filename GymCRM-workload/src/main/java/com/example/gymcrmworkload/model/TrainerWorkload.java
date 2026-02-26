package com.example.gymcrmworkload.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TrainerWorkload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<YearlyWorkloadOverview> trainingSummery;

    public YearlyWorkloadOverview getYearlyWorkloadOverview(int year) {
        YearlyWorkloadOverview yearlyWorkloadOverview = trainingSummery.stream().filter(yearlyWorkload -> yearlyWorkload.year == year).findFirst().orElse(null);
        if (yearlyWorkloadOverview != null) return yearlyWorkloadOverview;
        yearlyWorkloadOverview = new YearlyWorkloadOverview(year, this);
        trainingSummery.add(yearlyWorkloadOverview);
        return yearlyWorkloadOverview;
    }

    @Data
    @Getter
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YearlyWorkloadOverview {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne
        @JoinColumn(name = "trainer_id")
        @JsonBackReference
        private TrainerWorkload trainer;
        @OneToMany(mappedBy = "yearly", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JsonManagedReference
        private List<MonthlyWorkloadOverview> monthlyWorkload = new ArrayList<>();
        @Column(name = "workload_year")
        private int year;

        public YearlyWorkloadOverview(int year, TrainerWorkload trainerWorkload) {
            this.year = year;
            this.trainer = trainerWorkload;
        }

        public MonthlyWorkloadOverview getMonthlyWorkloadOverview(Month month) {
            return monthlyWorkload.stream().filter((monthlyWorkloadOverview -> monthlyWorkloadOverview.month.equals(month))).findFirst().orElse(null);
        }

        public void addMonthlyWorkload(Month month, Double workload) {
            MonthlyWorkloadOverview oldWorkload = getMonthlyWorkloadOverview(month);
            if (oldWorkload == null)
                monthlyWorkload.add(MonthlyWorkloadOverview.builder().yearly(this).month(month).workload(workload).build());
            else oldWorkload.addMonthlyWorkload(workload);
        }
    }

    @Data
    @Getter
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlyWorkloadOverview {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne
        @JoinColumn(name = "yearly_id")
        @JsonBackReference
        private YearlyWorkloadOverview yearly;
        private Double workload;
        @Column(name = "workload_month")
        private Month month;

        public void addMonthlyWorkload(Double workload) {
            this.workload += workload;
        }
    }

}
