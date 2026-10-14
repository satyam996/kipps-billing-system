package com.kipps.billingsystem.cotroller;

import com.kipps.billingsystem.entity.bill.Bill;
import com.kipps.billingsystem.service.BillService;
import com.kipps.billingsystem.service.ThermalPrintService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.kipps.billingsystem.service.ReceiptService;
import com.kipps.billingsystem.service.BillPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;

    private final ReceiptService receiptService;

    private final ThermalPrintService thermalPrintService;

    private final BillPdfService billPdfService;

    public BillController(
            BillService billService,
            ReceiptService receiptService,
            ThermalPrintService thermalPrintService, BillPdfService billPdfService) {

        this.billService = billService;
        this.receiptService = receiptService;
        this.thermalPrintService = thermalPrintService;
        this.billPdfService = billPdfService;
    }

    @PostMapping
    public Bill createBill(@Valid @RequestBody Bill bill){
        return billService.createBill(bill);
    }


    @GetMapping("/{id}")
    public Bill getBill(@PathVariable("id") Long id) {
        return billService.getBill(id);
    }


    @GetMapping("/{id}/receipt")
    public String getReceipt(@PathVariable("id") Long id) {
        Bill bill = billService.getBill(id);
        return receiptService.generateReceipt(bill);
    }

    @PostMapping("/{id}/print")
    public String printBill(@PathVariable("id") Long id) {

        Bill bill = billService.getBill(id);

        String receipt = receiptService.generateReceipt(bill);

        return thermalPrintService.printReceipt(receipt);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadBillPdf(
            @PathVariable("id") Long id) {

        Bill bill = billService.getBill(id);
        byte[] pdf = billPdfService.generatePdf(bill);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=bill-" + id + ".pdf")
                .body(pdf);
    }


}
