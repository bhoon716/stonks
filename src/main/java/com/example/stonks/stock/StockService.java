package com.example.stonks.stock;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private static final String CSV_PATH = "src/main/resources/stockData/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // app 실행 시 자동으로 실행
    @PostConstruct
    public void getDataCSV() {

        File folder = new File(CSV_PATH);
        File[] fileList = folder.listFiles((dir, name) -> name.endsWith(".csv")); //csv 파일 필터링

        if (fileList != null) {
            for (File file : fileList) {
                String symbol = file.getName().replace(".csv", "").toUpperCase();
                saveStockDataToDB(file, symbol);
            }
        }
    }

    // csv file 읽은 후 DB에 저장
    private void saveStockDataToDB(File file, String symbol) {

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                Stock stock = getStockData(symbol, nextLine);
                stockRepository.save(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Stock getStockData(String symbol, String[] nextLine) {
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setDate(LocalDate.parse(nextLine[0], DATE_FORMAT));
        stock.setPrice(Double.parseDouble(nextLine[1]));
        stock.setOpen(Double.parseDouble(nextLine[2]));
        stock.setHigh(Double.parseDouble(nextLine[3]));
        stock.setLow(Double.parseDouble(nextLine[4]));
        stock.setVolume(nextLine[5]);
        stock.setChangePercentage(nextLine[6]);

        return stock;
    }


    public Stock getLatestStockBySymbol(String symbol){
        List<Stock> result = stockRepository.getLatestBySymbol(symbol.toUpperCase());

        if(!result.isEmpty()){
            return result.get(0);
        } else{
            throw new RuntimeException("Not Found Data: "+ symbol);
        }
    }
}
