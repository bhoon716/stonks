package com.example.stonks.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "stock_data", uniqueConstraints = {@UniqueConstraint(columnNames = {"symbol", "date"})})
public class StockData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private LocalDate date;

    private Double price;

    private Double open;

    private Double high;

    private Double low;

    private String volume;

    private String changePercentage;
}
