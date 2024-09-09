package com.example.stonks.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StockController {

    @GetMapping("/stock")
    public String stock(){

        return "stock.html";
    }
}
