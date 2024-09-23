package com.example.stonks;

import com.example.stonks.stock.Stock;
import com.example.stonks.stock.StockDto;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvService {

    private static final String CSV_PATH = "src/main/resources/stockData/";

    public static List<Stock> getStockListFromCsv() {
        List<Stock> stockList = new ArrayList<>();
        File folder = new File(CSV_PATH);
        File[] fileList = folder.listFiles((dir, name) -> name.endsWith(".csv")); //csv 파일 필터링

        if (fileList != null) {
            for (File file : fileList) {
                String symbol = file.getName().replace(".csv", "").toUpperCase();
                Stock stock = new Stock(symbol);
                stockList.add(stock);
            }
        }
        return stockList;
    }

    private static File getStockFile(String symbol) {
        File file = new File(CSV_PATH + symbol + ".csv");
        if (!file.exists()) {
            throw new RuntimeException("CSV 파일을 찾을 수 없습니다: " + file.getAbsolutePath());
        }
        return file;
    }

    // 가장 최신의 주식 정보 가져오는 함수
    public static StockDto getLatestStockDto(String symbol) {
        File file = getStockFile(symbol);
        StockDto stockDto;

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;

            // 첫 번째 라인(헤더)을 건너뜀
            reader.readNext();

            // 가장 최신의 데이터를 읽음
            nextLine = reader.readNext();

            if (nextLine != null && nextLine.length >= 7) {
                stockDto = new StockDto(
                        symbol,
                        nextLine[0],
                        Double.parseDouble(nextLine[1]),
                        Double.parseDouble(nextLine[2]),
                        Double.parseDouble(nextLine[3]),
                        Double.parseDouble(nextLine[4]),
                        nextLine[5],
                        Double.parseDouble(nextLine[6].replace("%", ""))
                );
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

    public static Double getLatestPrice(String symbol) {
        File file = getStockFile(symbol);

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;

            // 첫 번째 라인(헤더)을 건너뜀
            reader.readNext();

            // 가장 최신의 데이터를 읽음
            nextLine = reader.readNext();

            if (nextLine != null && nextLine.length >= 7) {
                return Double.parseDouble(nextLine[1]);
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
    }

    public static double calculateVolatility(String symbol) {
        File file = getStockFile(symbol);
        List<Double> dailyReturns = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;

            // 첫 번째 라인(헤더)을 건너뜀
            reader.readNext();

            // 각 줄에서 Change % 데이터를 가져옴
            while ((nextLine = reader.readNext()) != null) {
                String changePercentStr = nextLine[6].replace("%", "");  // Change % 열에서 데이터를 가져옴
                double changePercent = Double.parseDouble(changePercentStr) / 100.0;  // 퍼센트 값을 소수로 변환
                dailyReturns.add(changePercent);
            }
            // 일간 변화율의 평균 계산
            double mean = calculateMean(dailyReturns);

            // 표준 편차(변동성) 계산
            double varianceSum = 0.0;
            for (double dailyReturn : dailyReturns) {
                varianceSum += Math.pow(dailyReturn - mean, 2);
            }
            double variance = varianceSum / (dailyReturns.size() - 1);
            return Math.sqrt(variance);  // 표준 편차가 변동성

        } catch (Exception e) {
            throw new RuntimeException("주식 심볼 오류: " + symbol + "\n" + e.getMessage());
        }
    }

    private static double calculateMean(List<Double> returns) {
        double sum = 0.0;
        for (double r : returns) {
            sum += r;
        }
        return sum / returns.size();
    }

    public static List<Double> getStockChangeList(String symbol) {
        File file = getStockFile(symbol);
        List<Double> changeList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;

            // 첫 번째 라인(헤더)을 건너뜀
            reader.readNext();

            // 각 줄에서 Change % 데이터를 가져옴
            while ((nextLine = reader.readNext()) != null) {
                String changePercentStr = nextLine[6].replace("%", "");  // Change % 열에서 데이터를 가져옴
                double changePercent = Double.parseDouble(changePercentStr);
                changeList.add(changePercent);
            }
            return changeList;
        } catch (Exception e) {
            throw new RuntimeException("주식 심볼 오류: " + symbol + "\n" + e.getMessage());
        }
    }

    public static List<Double> getStockPriceList(String symbol) {
        File file = getStockFile(symbol);
        List<Double> stockPriceList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;
            reader.readNext();

            while((nextLine = reader.readNext()) != null){
                Double price = Double.parseDouble(nextLine[1]);
                stockPriceList.add(price);
            }

            return stockPriceList;
        } catch (Exception e) {
            throw new RuntimeException("주식 심볼 오류: " + symbol + "\n" + e.getMessage());
        }
    }
}
