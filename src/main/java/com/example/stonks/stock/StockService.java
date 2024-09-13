package com.example.stonks.stock;

import com.example.stonks.portfolio.portfolioStock.PortfolioStock;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private static final String CSV_PATH = "src/main/resources/stockData/";

    // app 실행 시 자동으로 실행
    @PostConstruct
    public void saveStockFromCSV() {

        File folder = new File(CSV_PATH);
        File[] fileList = folder.listFiles((dir, name) -> name.endsWith(".csv")); //csv 파일 필터링

        if (fileList != null) {
            for (File file : fileList) {
                String symbol = file.getName().replace(".csv", "").toUpperCase();
                Stock stock = new Stock(symbol);
                stockRepository.save(stock);
            }
        }
    }

    // 가장 최신의 주식 정보 가져오는 함수
    public StockDto getLatestStockDto(String symbol) {
        StockDto stockDto = new StockDto();
        File file = new File(CSV_PATH + symbol + ".csv");

        if (!file.exists()) {
            throw new RuntimeException("CSV 파일을 찾을 수 없습니다: " + file.getAbsolutePath());
        }

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;

            // 첫 번째 라인(헤더)을 건너뜀
            reader.readNext();

            // 가장 최신의 데이터를 읽음
            nextLine = reader.readNext();

            if (nextLine != null && nextLine.length >= 7) {
                stockDto.setSymbol(symbol);
                stockDto.setDate(nextLine[0]);
                stockDto.setPrice(Double.parseDouble(nextLine[1]));
                stockDto.setOpen(Double.parseDouble(nextLine[2]));
                stockDto.setHigh(Double.parseDouble(nextLine[3]));
                stockDto.setLow(Double.parseDouble(nextLine[4]));
                stockDto.setVolume(nextLine[5]);
                stockDto.setChangePercentage(nextLine[6]);
            } else {
                throw new RuntimeException("CSV 데이터가 잘못되었습니다.");
            }

        } catch (NumberFormatException e) {
            throw new RuntimeException("숫자 형식 변환 오류가 발생했습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException("CSV 파일을 읽는 중 오류가 발생했습니다.", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException("CSV 파일 유효성 검사 오류", e);
        }

        return stockDto;
    }

    public Optional<Stock> findStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public Double getCurrentPrice(PortfolioStock portfolioStock){
         return getLatestStockDto(portfolioStock.getStock().getSymbol()).getPrice();
    }
}
