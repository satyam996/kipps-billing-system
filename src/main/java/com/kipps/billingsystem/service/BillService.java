
package com.kipps.billingsystem.service;

import com.kipps.billingsystem.entity.Product;
import com.kipps.billingsystem.entity.bill.Bill;
import com.kipps.billingsystem.entity.bill.BillItem;
import com.kipps.billingsystem.exception.ProductNotFoundException;
import com.kipps.billingsystem.repository.BillRepository;
import com.kipps.billingsystem.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ProductRepository productRepository;

    public BillService(
            BillRepository billRepository,
            ProductRepository productRepository) {
        this.billRepository = billRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Bill createBill(Bill bill) {

        if (bill.getBillDate() == null) {
            bill.setBillDate(java.time.LocalDateTime.now());
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal gstAmount = BigDecimal.ZERO;

        if (bill.getItems() == null || bill.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "A bill must contain at least one item");
        }

        for (BillItem item : bill.getItems()) {
            if (item.getProduct() == null
                    || item.getProduct().getId() == null) {
                throw new IllegalArgumentException(
                        "Each bill item must have a product ID");
            }

            Product product = productRepository
                    .findById(item.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product not found with id: "
                                    + item.getProduct().getId()));



            BigDecimal quantity = item.getQuantity();
            BigDecimal availableStock = product.getStockQuantity();

            if (availableStock == null
                    || availableStock.compareTo(quantity) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: " + product.getName()
                                + ". Available: " + availableStock
                                + ", Requested: " + quantity);
            }

            product.setStockQuantity(availableStock.subtract(quantity));
            productRepository.save(product);

            BigDecimal price = product.getPrice();
            BigDecimal gstPercentage = product.getGstPercentage();

            item.setProduct(product);
            item.setBill(bill);
            item.setPrice(price);
            item.setGstPercentage(gstPercentage);

            BigDecimal itemSubtotal = price.multiply(item.getQuantity())
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal itemGst = itemSubtotal
                    .multiply(gstPercentage)
                    .divide(BigDecimal.valueOf(100), 2,
                            RoundingMode.HALF_UP);

            item.setTotal(itemSubtotal);

            subtotal = subtotal.add(itemSubtotal);
            gstAmount = gstAmount.add(itemGst);
        }

        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        gstAmount = gstAmount.setScale(2, RoundingMode.HALF_UP);

        bill.setSubtotal(subtotal);
        bill.setGstAmount(gstAmount);
        bill.setGrandTotal(subtotal.add(gstAmount)
                .setScale(2, RoundingMode.HALF_UP));

        return billRepository.save(bill);
    }

    public Bill getBill(long id) {
        return billRepository.findById(id).orElseThrow(()-> new BillNotFoundException("Bill not found with id: "+id));
    }
}
