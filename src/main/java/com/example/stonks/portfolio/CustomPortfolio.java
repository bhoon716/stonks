package com.example.stonks.portfolio;

import com.example.stonks.portfolio.portfolioStock.CustomPortfolioStock;
import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import com.example.stonks.stock.CsvService;
import com.example.stonks.stock.StockService;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Getter
@Setter
public class CustomPortfolio {

    private Long id;
    private String name;
    private Long memberId;
    private List<PortfolioStock> portfolioStockList;

    private Double totalAssets;
    private Double totalPurchasePrice = 0.0;
    private Double totalValuationGainLoss;
    private String totalGainLossPercentage;
    private List<CustomPortfolioStock> customPortfolioStockList;

    public CustomPortfolio(Portfolio portfolio){
        this.id = portfolio.getId();
        this.name = portfolio.getName();
        this.memberId = portfolio.getMemberId();
        this.portfolioStockList = portfolio.getPortfolioStockList();

        this.totalAssets = calculateTotalAssets();
        this.totalPurchasePrice = calculateTotalPurchasePrice();
        this.totalValuationGainLoss = totalAssets - totalPurchasePrice;
        this.totalGainLossPercentage =  totalValuationGainLoss / totalPurchasePrice * 100 + "%";

        this.customPortfolioStockList = getCustomPortfolioStockList(portfolioStockList);
    }

    private Double calculateTotalAssets(){
        Double sum = 0.0;
        for(PortfolioStock portfolioStock : portfolioStockList){
            sum += (CsvService.getLatestPrice(portfolioStock.getStock().getSymbol()) * portfolioStock.getQuantity());
        }
        return sum;
    }

    private Double calculateTotalPurchasePrice() {
        Double sum = 0.0;
        for(PortfolioStock portfolioStock : portfolioStockList){
            sum += (portfolioStock.getPurchasePrice() * portfolioStock.getQuantity());
        }
        return sum;
    }

    public List<CustomPortfolioStock> getCustomPortfolioStockList(List<PortfolioStock> portfolioStockList) {
        List<CustomPortfolioStock> customPortfolioStocks= new ArrayList<>();
        for(PortfolioStock portfolioStock : portfolioStockList){
            CustomPortfolioStock customPortfolioStock = new CustomPortfolioStock(portfolioStock, totalAssets);
            customPortfolioStocks.add(customPortfolioStock);
        }
        return customPortfolioStocks;
    }
}
