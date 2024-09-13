package com.example.stonks.portfolio.portfolioStock;

import com.example.stonks.portfolio.Portfolio;
import com.example.stonks.stock.Stock;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PortfolioStock {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private Double purchasePrice;

    private Integer quantity;
}
