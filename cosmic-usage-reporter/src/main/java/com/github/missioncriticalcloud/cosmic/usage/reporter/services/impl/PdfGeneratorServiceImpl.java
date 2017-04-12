package com.github.missioncriticalcloud.cosmic.usage.reporter.services.impl;

import java.io.IOException;
import java.util.logging.Logger;

import com.github.missioncriticalcloud.cosmic.usage.reporter.services.PdfGeneratorService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    @Value(value = "classpath:/cosmic_billing_template_billing_details.pdf")
    private Resource cosmicBillingTemplate;

    private static final Logger LOG = Logger.getLogger(PdfGeneratorServiceImpl.class.getName());

    @Override
    public void generatePdf() {

        LOG.info("Generating PDF!");

        String filename = "test.pdf";
        StringBuilder billing_details = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            billing_details.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec imperdiet vestibulum semper. Ut vitae tincidunt ligula. Mauris vel dolor sed " +
                    "nisi condimentum suscipit ut non tellus. Aliquam commodo ultrices commodo. Ut id pharetra urna. Etiam nunc metus, malesuada vel suscipit eget, euismod eget " +
                    "mauris. In non ultrices quam. Curabitur vestibulum euismod massa. Fusce eu elit a magna sagittis faucibus. Vestibulum eget interdum mauris. Class aptent " +
                    "taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nunc sit amet facilisis quam, vel tempus nisl.\n\n");
        }

        PDDocument doc = null;
        try {
            // load the document
            doc = PDDocument.load(cosmicBillingTemplate.getFile());

            // get the document catalog
            PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();

            // as there might not be an AcroForm entry a null check is necessary
            if (acroForm != null) {
                PDTextField field;
                field = (PDTextField) acroForm.getField("customer_details");
                field.setValue("Some customer name\nStreetname 10\nzipcode");

                field = (PDTextField) acroForm.getField("supplier_details");
                field.setValue("Schuberg Philis\nStreetname 271\nzipcode");

                field = (PDTextField) acroForm.getField("period_details");
                field.setValue("April 2017");

                field = (PDTextField) acroForm.getField("invoice_date_details");
                field.setValue("2017-04-01");

                field = (PDTextField) acroForm.getField("customer_number_details");
                field.setValue("666");

                acroForm.getField("invoice_number_details").setValue("123");

                acroForm.getField("billing_detail").setValue(billing_details.toString());
            }

            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;

            PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 200);
            contents.showText(billing_details.toString().replace("\n", ""));
            contents.endText();
            contents.close();

            doc.save(filename);
            LOG.info("PDF Generated!");
        } catch (IOException e) {
            LOG.severe(e.getCause().getLocalizedMessage());
        } finally {
            try {
                if (doc != null) {
                    doc.close();
                }
            } catch (IOException e) {
                LOG.severe(e.getCause().getLocalizedMessage());
            }
        }
    }
}