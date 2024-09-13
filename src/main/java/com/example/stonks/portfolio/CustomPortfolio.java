package com.example.stonks.portfolio;

import com.example.stonks.portfolio.portfolioStock.CustomPortfolioStock;
import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
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
    private List<CustomPortfolioStock> customPortfolioStockList = new ArrayList<>();

    private Double totalAssets = 0.0;
    private Double totalPurchasePrice = 0.0;
    private Double totalValuationGainLoss;
    private String totalGainLossPercentage;

    private final StockService stockService;

    // 생성자에서 가격 데이터를 캐시
    private Map<String, Double> priceCache = new HashMap<>();

    public CustomPortfolio(Portfolio portfolio, StockService stockService) {
        this.id = portfolio.getId();
        this.name = portfolio.getName();
        this.memberId = portfolio.getMemberId();
        this.stockService = stockService;

        // 포트폴리오 내의 주식을 처리
        for (PortfolioStock portfolioStock : portfolio.getPortfolioStocks()) {
            Double currentPrice = getCurrentPriceWithCache(portfolioStock);
            CustomPortfolioStock customPortfolioStock = new CustomPortfolioStock(portfolioStock, currentPrice);
            this.totalAssets += customPortfolioStock.getCurrentPrice() * customPortfolioStock.getQuantity();
            this.totalPurchasePrice += customPortfolioStock.getPurchasePrice() * customPortfolioStock.getQuantity();
            customPortfolioStockList.add(customPortfolioStock);
        }

        // 평가손익 및 수익률 계산
        this.totalValuationGainLoss = this.totalAssets - this.totalPurchasePrice;
        this.totalGainLossPercentage = formatPercentage(this.totalValuationGainLoss / this.totalPurchasePrice * 100);

        // 비율 계산
        if (this.totalAssets != 0) {
            for (CustomPortfolioStock cuPortfolioStock : customPortfolioStockList) {
                cuPortfolioStock.setRatio((cuPortfolioStock.getCurrentPrice() * cuPortfolioStock.getQuantity()) / this.totalAssets * 100);
            }
        }
    }

    // 주식 가격을 캐싱하여 중복 호출 방지
    private Double getCurrentPriceWithCache(PortfolioStock portfolioStock) {
        String symbol = portfolioStock.getStock().getSymbol();
        return priceCache.computeIfAbsent(symbol, s -> stockService.getCurrentPrice(portfolioStock));
    }

    // 퍼센트 형식 지정 (소수점 2자리까지)
    private String formatPercentage(Double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value) + "%";
    }
}
