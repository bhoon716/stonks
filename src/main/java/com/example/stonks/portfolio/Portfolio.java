package com.example.stonks.portfolio;

import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "memberId"})
)
public class Portfolio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "memberId")
    private Long memberId;

    @OneToMany(
            mappedBy = "portfolio",
            fetch = FetchType.LAZY
    )
    private List<PortfolioStock> portfolioStockList;
}
