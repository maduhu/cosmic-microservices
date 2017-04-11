package com.github.missioncriticalcloud.cosmic.usage.reporter;

import com.github.missioncriticalcloud.cosmic.usage.reporter.controllers.ReporterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private ReporterController reporterController;

    @Autowired
    public Application(ReporterController reporterController) {
        this.reporterController = reporterController;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        this.reporterController.runReporting();
    }
}