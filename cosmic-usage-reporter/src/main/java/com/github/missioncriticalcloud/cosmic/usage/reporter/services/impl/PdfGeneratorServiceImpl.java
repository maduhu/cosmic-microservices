package com.github.missioncriticalcloud.cosmic.usage.reporter.services.impl;

import java.io.IOException;
import java.util.logging.Logger;

import com.github.missioncriticalcloud.cosmic.usage.reporter.services.PdfGeneratorService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private static final Logger LOG = Logger.getLogger(PdfGeneratorServiceImpl.class.getName());

    @Override
    public void generatePdf() {
        LOG.info("Generating PDF!");

        String filename = "test.pdf";
        String message = "Hello World!";

        PDDocument doc = new PDDocument();
        try {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;

            PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 700);
            contents.showText(message);
            contents.endText();
            contents.close();

            doc.save(filename);
        } catch (IOException e) {
            LOG.severe(e.getCause().getLocalizedMessage());
        } finally {
            try {
                doc.close();
            } catch (IOException e) {
                LOG.severe(e.getCause().getLocalizedMessage());
            }
        }
    }
}