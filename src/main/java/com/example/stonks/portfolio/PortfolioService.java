package com.example.stonks.portfolio;

import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import com.example.stonks.portfolio.portfolioStock.PortfolioStockService;
import com.example.stonks.stock.Stock;
import com.example.stonks.stock.StockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final StockService stockService;
    private final PortfolioStockService portfolioStockService;

    public void createPortfolio(Long memberId, String name){
        Portfolio portfolio = new Portfolio();
        portfolio.setName(name);
        portfolio.setMemberId(memberId);

        portfolioRepository.save(portfolio);
    }

    public CustomPortfolio getCustomPortfolio(Long id) {
        var portfolioOpt = portfolioRepository.findById(id);
        if (portfolioOpt.isEmpty()) {
            throw new RuntimeException("포트폴리오를 찾을 수 없습니다.");
        }

        Portfolio portfolio = portfolioOpt.get();
        return new CustomPortfolio(portfolio, stockService);
    }

    @Transactional
    public void addStockToPortfolio(Long id, String symbol, Double purchasePrice, Integer quantity) throws Exception {

        var pf = portfolioRepository.findById(id);
        var st = stockService.findStockBySymbol(symbol);
        if(pf.isEmpty()){
            throw new Exception("포트폴리오 찾을 수 없음");
        }
        if(st.isEmpty()){
            throw new Exception("존재하지 않는 주식");
        }

        Portfolio portfolio = pf.get();
        Stock stock = st.get();
        PortfolioStock portfolioStock = new PortfolioStock();

        portfolioStock.setPortfolio(portfolio);
        portfolioStock.setStock(stock);
        portfolioStock.setPurchasePrice(purchasePrice);
        portfolioStock.setQuantity(quantity);

        portfolioStockService.savePortfolioStock(portfolioStock);
    }

    public List<Portfolio> findAllPortfolio(){

        return portfolioRepository.findAll();
    }

    public Portfolio findPortfolioById(Long id) {
        var result = portfolioRepository.findById(id);
        Portfolio portfolio = result.get();

        return portfolio;
    }

    public void addPortfolio(String portfolioName) {

        Portfolio portfolio = new Portfolio();
        portfolio.setMemberId(0l);
        portfolio.setName(portfolioName);
        portfolioRepository.save(portfolio);
    }

    public void deleteStockFromPortfolio(Long id) {

        portfolioStockService.deleteStockById(id);
    }


}