package com.example.stonks.portfolio;

import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import com.example.stonks.portfolio.portfolioStock.PortfolioStockRepository;
import com.example.stonks.stock.Stock;
import com.example.stonks.stock.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final PortfolioStockRepository portfolioStockRepository;

    public void createPortfolio(Long memberId, String name){
        Portfolio portfolio = new Portfolio();
        portfolio.setName(name);
        portfolio.setMemberId(memberId);

        portfolioRepository.save(portfolio);
    }

    @Transactional
    public void addStockToPortfolio(Long id, String symbol, Double purchasePrice, Integer quantity) throws Exception {

        var pf = portfolioRepository.findById(id);
        var st = stockRepository.findBySymbol(symbol);
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

        portfolioStockRepository.save(portfolioStock);
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

        Portfolio pf = new Portfolio();
        pf.setMemberId(0l);
        pf.setName(portfolioName);
        portfolioRepository.save(pf);
    }

    public void deleteStockFromPortfolio(Long id) {

        portfolioStockRepository.deleteById(id);
    }
}