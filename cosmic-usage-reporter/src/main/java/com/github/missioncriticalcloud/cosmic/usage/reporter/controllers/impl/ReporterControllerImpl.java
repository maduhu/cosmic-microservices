package com.github.missioncriticalcloud.cosmic.usage.reporter.controllers.impl;

import com.github.missioncriticalcloud.cosmic.usage.reporter.controllers.ReporterController;
import com.github.missioncriticalcloud.cosmic.usage.reporter.services.PdfGeneratorService;
import com.github.missioncriticalcloud.cosmic.usage.reporter.services.UsageReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReporterControllerImpl implements ReporterController {

    private UsageReceiverService usageReceiverService;
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    public ReporterControllerImpl(
            UsageReceiverService usageReceiverService,
            PdfGeneratorService pdfGeneratorService
    ) {
        this.usageReceiverService = usageReceiverService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Override
    public void runReporting() {
        usageReceiverService.getUsage();

        pdfGeneratorService.generatePdf();
    }
}
