package com.example.stonks.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockDataRepository extends JpaRepository<StockData, Long> {

    // 가장 최근 데이터를 가져오는 함수
    @Query("select s from StockData s where s.symbol = :symbol order by s.date desc")
    List<StockData> getLatestBySymbol(@Param("symbol") String symbol);
}
