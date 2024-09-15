package com.example.stonks.stock;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Stock {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String symbol;

    public Stock(){}

    public Stock(String symbol) {
        this.symbol = symbol;
    }
}
