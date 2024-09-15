package com.example.stonks.stock;

import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    // app 실행 시 자동으로 실행
    @PostConstruct
    public void saveStockFromCSV() {
        stockRepository.saveAll(CsvService.getStockListFromCsv());
    }

    public Optional<Stock> findStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public Double getCurrentPrice(PortfolioStock portfolioStock){
         return CsvService.getLatestStockDto(portfolioStock.getStock().getSymbol()).getPrice();
    }
}
