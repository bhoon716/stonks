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

        CustomPortfolio customPortfolio = portfolioService.getCustomPortfolio(id);
        model.addAttribute("customPortfolio", customPortfolio);

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

        portfolioService.createPortfolio(0L, "name");

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

    @PostMapping("/portfolio/change-name")
    public String changePortfolioName(@RequestParam Long portfolioId, @RequestParam String nameToChange) {
        portfolioService.changePortfolioName(portfolioId, nameToChange);

        return "redirect:/portfolio/list";
    }

    // DeleteMapping -----------------------------------------

    @DeleteMapping("/portfolio")
    public String deletePortfolio(@RequestParam Long portfolioId){

        portfolioService.deletePortfolio(portfolioId);

        return "redirect:/portfolio/list";
    }

    @DeleteMapping("/portfolio/stock")
    public String deletePortfolioStock(@RequestParam Long portfolioStockId, @RequestParam Long portfolioId) {

        portfolioService.deleteStockFromPortfolio(portfolioStockId);

        return "redirect:/portfolio/" + portfolioId;
    }
}