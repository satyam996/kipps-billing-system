package com.kipps.billingsystem.repository;

import com.kipps.billingsystem.entity.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}