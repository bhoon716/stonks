package com.example.stonks.stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDto {

    private String symbol;

    private String date;

    private Double price;

    private Double open;

    private Double high;

    private Double low;

    private String volume;

    private String changePercentage;

    public StockDto() {    }

    public StockDto(String symbol, String date, Double price, String volume, String changePercentage){
        this.symbol = symbol;
        this.date = date;
        this.price = price;
        this.volume = volume;
        this.changePercentage = changePercentage;
    }

    public StockDto(String symbol, String date, Double price, Double open,
                    Double high, Double low, String volume, String changePercentage) {
        this.symbol = symbol;
        this.date = date;
        this.price = price;
        this.open = open;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.changePercentage = changePercentage;
    }
}
