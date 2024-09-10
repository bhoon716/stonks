package com.example.stonks.stock;

import com.example.stonks.data.StockData;
import com.example.stonks.data.StockDataRepository;
import com.example.stonks.data.StockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class StockController {

    private final StockRepository stockRepository;
    private final StockDataRepository stockDataRepository;

    // GetMapping ----------------------------------------

    @GetMapping("/stock")
    public String stock(Model model, @RequestParam String symbol) {

        List<StockData> result = stockDataRepository.getLatestBySymbol(symbol.toUpperCase());
        if(result.isEmpty()){
            throw new RuntimeException("Not Found Data: "+ symbol);
        }

        StockData stock = result.get(0);
        model.addAttribute("stock", stock);

        return "stock.html";
    }

    @GetMapping("/list")
    public String stockList(Model model) {

        List<Stock> result = stockRepository.findAll();
        model.addAttribute("stocks", result);

        return "list.html";
    }


    // PostMapping -----------------------------------------

    @PostMapping("/stock")
    public String searchStock(Model model, String symbol) {

        var result = stockRepository.findBySymbol(symbol);
        Stock stock = result.get();
        model.addAttribute("stock", stock);

        return "redirect:/list";
    }

    @PostMapping("/stock/add")
    public String addStock(@RequestParam String symbol) {
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stockRepository.save(stock);

        return "redirect:/list";
    }
}
