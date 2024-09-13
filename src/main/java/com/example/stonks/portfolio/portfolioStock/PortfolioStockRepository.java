package com.example.stonks.portfolio.portfolioStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, Long> {
}
