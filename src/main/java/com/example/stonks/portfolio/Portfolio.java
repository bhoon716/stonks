package com.example.stonks.portfolio;

import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Portfolio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long memberId;

    @OneToMany(
            mappedBy = "portfolio",
            fetch = FetchType.LAZY
    )
    private Set<PortfolioStock> portfolioStocks;
}
