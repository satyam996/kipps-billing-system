
package com.kipps.billingsystem.service;

import org.springframework.stereotype.Service;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import java.nio.charset.StandardCharsets;

@Service
public class ThermalPrintService {

    public String printReceipt(String receipt) {

        PrintService printer =
                PrintServiceLookup.lookupDefaultPrintService();

        if (printer == null) {
            throw new IllegalStateException(
                    "No default printer found. Install the printer and set it as default.");
        }

        try {
            byte[] data = receipt.getBytes(StandardCharsets.UTF_8);

            DocPrintJob job = printer.createPrintJob();

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

            if (!printer.isDocFlavorSupported(flavor)) {
                throw new IllegalStateException(
                        "The default printer does not support this print format.");
            }

            Doc document = new SimpleDoc(data, flavor, null);

            job.print(document, null);

            return "Print job sent to printer: " + printer.getName();

        } catch (PrintException e) {
            throw new IllegalStateException(
                    "Printing failed: " + e.getMessage(), e);
        }
    }
}
