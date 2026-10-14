package com.kipps.billingsystem.service;

import com.kipps.billingsystem.entity.bill.Bill;
import org.springframework.stereotype.Service;

    @Service
    public class ReceiptService  {


        public String generateReceipt(Bill bill) {


            StringBuilder receipt = new StringBuilder();

            receipt.append("          H G SWEETS (KIPPS)\n");
            receipt.append("          philibhit byepass road)\n");
            receipt.append("          9454442143)\n");
            receipt.append("                     \n");
            receipt.append("----------------------------------------\n");
            receipt.append("Bill No: ").append(bill.getId()).append("\n");
            receipt.append("Date: ").append(bill.getBillDate()).append("\n");
            receipt.append("----------------------------------------\n");
            receipt.append(String.format(
                    "%-10s %5s %10s %11s%n",
                    "Item", "Qty", "Rate", "Amount"));
            receipt.append("----------------------------------------\n");

            bill.getItems().forEach(item -> {
                String name = item.getProduct().getName();
                if (name.length() > 10) {
                    name = name.substring(0, 10);
                }

                receipt.append(String.format(
                        "%-10s %5s %10s %11s%n",
                        name,
                        item.getQuantity().toPlainString(),
                        item.getPrice().toPlainString(),
                        item.getTotal().toPlainString()));
            });

            receipt.append("----------------------------------------\n");
            receipt.append(String.format("%-20s %18s%n",
                    "Subtotal:", bill.getSubtotal().toPlainString()));
            receipt.append(String.format("%-20s %18s%n",
                    "GST:", bill.getGstAmount().toPlainString()));
            receipt.append("----------------------------------------\n");
            receipt.append(String.format("%-20s %18s%n",
                    "GRAND TOTAL:", bill.getGrandTotal().toPlainString()));
            receipt.append("----------------------------------------\n");
            receipt.append("          Thanks! Visit Again!\n");

            return receipt.toString();
        }

    }
