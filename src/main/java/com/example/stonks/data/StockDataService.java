package com.example.stonks.data;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class StockDataService {

    private final StockDataRepository stockDataRepository;

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
                StockData stockData = getStockData(symbol, nextLine);
                stockDataRepository.save(stockData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static StockData getStockData(String symbol, String[] nextLine) {
        StockData stockData = new StockData();
        stockData.setSymbol(symbol);
        stockData.setDate(LocalDate.parse(nextLine[0], DATE_FORMAT));
        stockData.setPrice(Double.parseDouble(nextLine[1]));
        stockData.setOpen(Double.parseDouble(nextLine[2]));
        stockData.setHigh(Double.parseDouble(nextLine[3]));
        stockData.setLow(Double.parseDouble(nextLine[4]));
        stockData.setVolume(nextLine[5]);
        stockData.setChangePercentage(nextLine[6]);

        return stockData;
    }
}
