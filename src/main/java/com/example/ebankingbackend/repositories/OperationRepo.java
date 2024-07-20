package com.example.ebankingbackend.repositories;

import com.example.ebankingbackend.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepo extends JpaRepository<Operation, Long> {
    Page<Operation> findByBankAccountIdOrderByOperationDateDesc(String accountId, Pageable pageable);

}
