package com.example.stonks.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StockController {

    private final StockRepository stockRepository;
    private final StockService stockService;

    // GetMapping ----------------------------------------

    @GetMapping("/stock/list")
    public String stockList(Model model) {

        List<Stock> result = stockRepository.findAll();
        model.addAttribute("stocks", result);

        return "list.html";
    }


    // PostMapping -----------------------------------------

    @PostMapping("/stock")
    public String stock(Model model, @RequestParam String symbol) {

        StockDto stock = CsvService.getLatestStockDto(symbol);
        model.addAttribute("stock", stock);

        return "stock.html";
    }
}
