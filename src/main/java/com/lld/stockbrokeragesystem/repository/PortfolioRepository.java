package com.lld.stockbrokeragesystem.repository;

import com.lld.stockbrokeragesystem.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query("select p from Portfolio p where p.id = :id and p.stockId = :stockId")
    Optional<Portfolio> findByIdAndStockId(@Param("userId") Long userId, @Param("stockId") Long stockId);

}

