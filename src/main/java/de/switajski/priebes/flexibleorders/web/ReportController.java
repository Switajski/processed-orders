package de.switajski.priebes.flexibleorders.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;

/**
 * Controller for handling http requests on reports. E.g. PDFs
 *
 * @author Marek Switajski
 *
 */
@CrossOrigin
@Controller
public class ReportController {

    private static Logger log = Logger.getLogger(ReportController.class);
    @Autowired
    private ReportRepository reportRepository;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public @ResponseBody Object listAll() {
        List<Report> reports = reportRepository.findAll();
        return reports;
    }

    @RequestMapping(value = "/report/{docNo}", method = RequestMethod.GET)
    public @ResponseBody Object listByDocumentNumber(
            @PathVariable("docNo") String docNo) {
        Report report = reportRepository.findByDocumentNumber(docNo);
        return report;
    }
}
