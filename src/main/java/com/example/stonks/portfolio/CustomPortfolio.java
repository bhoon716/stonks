package com.example.stonks.portfolio;

import com.example.stonks.portfolio.portfolioStock.CustomPortfolioStock;
import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import com.example.stonks.CsvService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CustomPortfolio {

    private final Integer CAGR_YEAR = 10;

    private Long id;
    private String name;
    private Long memberId;
    private List<PortfolioStock> portfolioStockList;

    private Double totalAssets;
    private Double totalPurchasePrice = 0.0;
    private Double totalValuationGainLoss;
    private Double totalGainLossPercentage;
    private List<CustomPortfolioStock> customPortfolioStockList;
    private Double volatility;
    private Double compoundAnnualGrowthRate;

    public CustomPortfolio(Portfolio portfolio){
        this.id = portfolio.getId();
        this.name = portfolio.getName();
        this.memberId = portfolio.getMemberId();
        this.portfolioStockList = portfolio.getPortfolioStockList();

        this.totalAssets = calculateTotalAssets();
        this.totalPurchasePrice = calculateTotalPurchasePrice();
        this.totalValuationGainLoss = totalAssets - totalPurchasePrice;
        if(totalPurchasePrice != 0.0){
            this.totalGainLossPercentage =  totalValuationGainLoss / totalPurchasePrice * 100;
        } else{
            this.totalGainLossPercentage = 0.0;
        }
        this.customPortfolioStockList = getCustomPortfolioStockList(portfolioStockList);

        this.volatility = getPortfolioVolatility();
        this.compoundAnnualGrowthRate = getCompoundAnnualGrowthRate(CAGR_YEAR);
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

    public double getPortfolioVolatility() {
        // 주식별 수익률 및 비중 데이터 로드
        Map<String, List<Double>> stockReturns = new HashMap<>();
        int n = customPortfolioStockList.size();
        double[] weights = new double[n];

        // Change % 데이터를 읽어서 리스트로 반환
        for (int i = 0; i < n; i++) {
            CustomPortfolioStock customPortfolioStock = customPortfolioStockList.get(i);
            String symbol = customPortfolioStock.getStock().getSymbol();
            List<Double> returns = CsvService.getStockChangeList(symbol);
            stockReturns.put(symbol, returns);
            weights[i] = customPortfolioStock.getRatio() / 100.0; // 비율은 퍼센트로 저장된 값으로 가정
        }

        // 공분산 계산
        double[][] covarianceMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                List<Double> returns1 = stockReturns.get(customPortfolioStockList.get(i).getStock().getSymbol());
                List<Double> returns2 = stockReturns.get(customPortfolioStockList.get(j).getStock().getSymbol());

                covarianceMatrix[i][j] = calculateCovariance(returns1, returns2);
            }
        }
        // 포트폴리오 변동성 계산
        return calculatePortfolioVolatility(weights, covarianceMatrix);
    }

    // 공분산 계산
    private double calculateCovariance(List<Double> returns1, List<Double> returns2) {
        int size = Math.min(returns1.size(), returns2.size());
        double mean1 = returns1.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double mean2 = returns2.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        double covariance = 0.0;
        for (int k = 0; k < size; k++) {
            covariance += (returns1.get(k) - mean1) * (returns2.get(k) - mean2);
        }
        return covariance / (size - 1);
    }

    // 포트폴리오 변동성(표준편차) 계산
    private double calculatePortfolioVolatility(double[] weights, double[][] covarianceMatrix) {
        double portfolioVariance = 0.0;
        int n = weights.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                portfolioVariance += weights[i] * weights[j] * covarianceMatrix[i][j];
            }
        }

        return Math.sqrt(portfolioVariance);
    }

    // 포트폴리오 연 평균 수익률 계산
    public Double getCompoundAnnualGrowthRate(Integer year){
        double portfolioCAGR = 0.0;

        for(CustomPortfolioStock customPortfolioStock : customPortfolioStockList){
            String symbol = customPortfolioStock.getStock().getSymbol();
            List<Double> prices = CsvService.getStockPriceList(symbol);
            double startPrice = prices.get(prices.size() - 1);
            double endPrice = prices.get(0);

            double stockCAGR = calculateCompoundAnnualGrowthRate(startPrice, endPrice, year);
            portfolioCAGR += stockCAGR * customPortfolioStock.getRatio() / 100.0;
        }
        return portfolioCAGR * 100;
    }

    public double calculateCompoundAnnualGrowthRate(double startPrice, double endPrice, int years) {
        return Math.pow(endPrice / startPrice, 1.0 / years) - 1;
    }
}