package com.example.stonks.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findBySymbol(String symbol);

    // 가장 최근 데이터를 가져오는 함수
    @Query("select s from Stock s where s.symbol = :symbol order by s.date desc")
    List<Stock> getLatestBySymbol(@Param("symbol") String symbol);

    // 심볼별로 가장 최근 데이터를 가져오는 쿼리
    @Query("SELECT s FROM Stock s WHERE s.date = (SELECT MAX(subS.date) FROM Stock subS WHERE subS.symbol = s.symbol)")
    List<Stock> getStockList();
}