package com.example.stonks.portfolio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    // GetMapping ----------------------------------------

    @GetMapping("/portfolio/{id}")
    public String getPortfolio(@PathVariable Long id, Model model) {

        Portfolio portfolio = portfolioService.findPortfolioById(id);
        model.addAttribute("portfolio", portfolio);

        return "portfolio.html";
    }

    @GetMapping("/portfolio/list")
    public String portfolioList(Model model) {

        model.addAttribute("portfolios", portfolioService.findAllPortfolio());

        return "portfolio-list.html";
    }

    // PostMapping -----------------------------------------

    @PostMapping("/portfolio")
    public String createPortfolio() {

        portfolioService.createPortfolio(0l, "name");

        return "portfolio.html";
    }

    @PostMapping("/portfolio/add-stock")
    public String addStockToPortfolio(
            @RequestParam Long id,
            @RequestParam String symbol,
            @RequestParam Double purchasePrice,
            @RequestParam Integer quantity) throws Exception {

        portfolioService.addStockToPortfolio(id, symbol, purchasePrice, quantity);

        return "redirect:/portfolio/" + id;
    }

    @PostMapping("/portfolio/add")
    public String addPortfolio(@RequestParam String portfolioName) {

        portfolioService.addPortfolio(portfolioName);

        return "redirect:/portfolio/list";
    }

    // DeleteMapping -----------------------------------------

    @DeleteMapping("/portfolio/stock")
    public String deletePortfolioStock(@RequestParam Long id){

        portfolioService.deleteStockFromPortfolio(id);

        return "redirect:/portfolio/"+id;
    }
}