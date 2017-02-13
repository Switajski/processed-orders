package de.switajski.priebes.flexibleorders.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.web.debug.LoggingRequestInterceptor;

@Controller
public class PouchDBExportController {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ReportRepository reportRepo;

    @RequestMapping(value = "/export-orders", method = RequestMethod.GET)
    public @ResponseBody Object export() {
        StringBuilder strb = new StringBuilder().append("exported orders: ");
        try {
            List<Order> allOrders = orderRepo.findAll();
            return strb.append(exportViaPostToPouchDb(allOrders));
        }
        catch (HttpClientErrorException httpExc) {
            strb.append(httpExc.getMessage());
            return httpExc.toString() + " <br/>" + httpExc.getResponseBodyAsString() + "<br/>log:<br/>" + strb.toString();
        }
    }

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public @ResponseBody Object openReports() {
        StringBuilder strb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList allDocs = new ArrayList(orderRepo.findAll());
            allDocs.addAll(reportRepo.findAll());
            strb.append(mapper.writeValueAsString(allDocs));
            return strb.toString();
        }
        catch (HttpClientErrorException | JsonProcessingException httpExc) {
            strb.append(httpExc.getMessage());
            return httpExc.toString() + " <br/>" + httpExc.getMessage() + "<br/>log:<br/>" + strb.toString();
        }
    }

    private Object exportViaPostToPouchDb(List<Order> allOrders) {
        StringBuilder strb = new StringBuilder();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Arrays.asList(new LoggingRequestInterceptor()));
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        String destination = "http://localhost:5984/fo6";
        for (Order order : allOrders) {
            ResponseEntity<Order> response = restTemplate.postForEntity(
                    destination,
                    order,
                    Order.class);
            strb.append(order.getOrderNumber())
                    .append(":")
                    .append(response.getStatusCode())
                    .append(", ");
        }

        for (Report r : reportRepo.findAll()) {
            ResponseEntity<? extends Report> response = restTemplate.postForEntity(
                    destination,
                    r,
                    r.getClass());
            strb.append(r.getDocumentNumber())
                    .append(":")
                    .append(response.getStatusCode())
                    .append(", ");
        }
        return strb.toString();
    }

}
