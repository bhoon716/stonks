package com.example.stonks.portfolio.portfolioStock;

import com.example.stonks.portfolio.Portfolio;
import com.example.stonks.stock.Stock;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomPortfolioStock {

    private Long id;
    private Portfolio portfolio;
    private Stock stock;
    private Double purchasePrice;
    private Integer quantity;
    private Double currentPrice;
    private Double ratio;
    private Double valuationGainLoss;
    private String gainLossPercentage;

    public CustomPortfolioStock(PortfolioStock portfolioStock, Double currentPrice) {
        this.id = portfolioStock.getId();
        this.portfolio = portfolioStock.getPortfolio();
        this.stock = portfolioStock.getStock();
        this.purchasePrice = portfolioStock.getPurchasePrice();
        this.quantity = portfolioStock.getQuantity();
        this.currentPrice = currentPrice;
        this.valuationGainLoss = currentPrice - purchasePrice;
        this.gainLossPercentage = (valuationGainLoss) / purchasePrice * 100 + "%";
    }

    public CustomPortfolioStock(PortfolioStock portfolioStock, Double currentPrice, Double ratio) {
        this.id = portfolioStock.getId();
        this.portfolio = portfolioStock.getPortfolio();
        this.stock = portfolioStock.getStock();
        this.purchasePrice = portfolioStock.getPurchasePrice();
        this.quantity = portfolioStock.getQuantity();
        this.currentPrice = currentPrice;
        this.ratio = ratio;
        this.valuationGainLoss = currentPrice - purchasePrice;
        this.gainLossPercentage = (valuationGainLoss) / purchasePrice * 100 + "%";
    }
}