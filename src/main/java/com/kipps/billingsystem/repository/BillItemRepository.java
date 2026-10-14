
package com.kipps.billingsystem.repository;

import com.kipps.billingsystem.entity.bill.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {
}

