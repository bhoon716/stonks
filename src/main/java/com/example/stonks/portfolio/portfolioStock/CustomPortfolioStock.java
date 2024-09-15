package com.example.stonks.portfolio.portfolioStock;

import com.example.stonks.portfolio.Portfolio;
import com.example.stonks.stock.CsvService;
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
    private Double valuationGainLoss;
    private String gainLossPercentage;
    private String ratio;

    public CustomPortfolioStock(PortfolioStock portfolioStock, Double portfolioTotalAssets) {
        this.id = portfolioStock.getId();
        this.portfolio = portfolioStock.getPortfolio();
        this.stock = portfolioStock.getStock();
        this.purchasePrice = portfolioStock.getPurchasePrice();
        this.quantity = portfolioStock.getQuantity();

        this.currentPrice = CsvService.getLatestPrice(stock.getSymbol());
        this.valuationGainLoss = (currentPrice - purchasePrice) * quantity;
        this.gainLossPercentage = (currentPrice - purchasePrice) / purchasePrice * 100 + "%";
        this.ratio = (currentPrice * quantity) / portfolioTotalAssets * 100 + "%";
    }


}