package com.example.stonks.portfolio.portfolioStock;

import com.example.stonks.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioStockService {

    private final PortfolioStockRepository portfolioStockRepository;

    public void savePortfolioStock(PortfolioStock portfolioStock) {
        portfolioStockRepository.save(portfolioStock);
    }

    public void deleteStockById(Long id) {
        portfolioStockRepository.deleteById(id);
    }
}
