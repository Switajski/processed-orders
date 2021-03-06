package de.switajski.priebes.flexibleorders.service.api;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.repository.specification.OrderCreatedBetweenSpecification;
import de.switajski.priebes.flexibleorders.service.DocumentNumberInFormatComparator;

@Service
public class DocumentNumberGeneratorService {

    public static final String PENDING_ITEMS_SUFFIX = "-AA";

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ReportRepository reportRepository;

    /**
     * Generates the next orderNumber regarding the last reports only
     * 
     * @param date
     * @param orderNumber
     *            if given orderNumber is generated and valid returns this
     *            parameter back again
     * @return
     */
    public String yymmggg(LocalDate date, String orderNumber) {
        boolean isGenerated = orderNumber.startsWith("B") && orderNumber.length() == 8;
        if (isGenerated && reportRepository.findAll(new DocumentNumberContains(orderNumber)).isEmpty()) {
            return orderNumber.replace("B", "");
        }
        return generateNextYYMMGGGFor(date);
    }

    /**
     * Same as {@link #yymm(LocalDate)}, but with a "b" as prefix
     * 
     * @param date
     * @return
     */
    public String byymmggg(LocalDate date) {
        return "B" + generateNextYYMMGGGFor(date);
    }

    public String byOrderConfrimationNumber(String orderConfirmationNumber) {
        String firstDeliveryNotesNo = orderConfirmationNumber.replace("AB", "L");
        if (reportRepository.findByDocumentNumber(firstDeliveryNotesNo) == null) {
            return firstDeliveryNotesNo;
        }
        else {
            for (int i = 1; i < 10; i++) {
                String pendingDNNo = generatePendingDeliveryNotesNo(firstDeliveryNotesNo, i);
                if (reportRepository.findByDocumentNumber(pendingDNNo) == null) {
                    return pendingDNNo;
                }
            }
            return firstDeliveryNotesNo;
        }
    }

    private String generatePendingDeliveryNotesNo(String firstDeliveryNotesNo, int i) {
        return firstDeliveryNotesNo + PENDING_ITEMS_SUFFIX + i;
    };

    private String generateNextYYMMGGGFor(LocalDate date) {
        boolean regardingOrders = true;
        boolean regardingReports = true;

        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Specification<Order> spec = new OrderNumberStartsWith("B" + yymm(date));

        int lastDocumentNumber = 0;
        if (regardingOrders) {
            // Using Page, because it's the less effort for sort direction and
            // result size
            Page<Order> orders = orderRepository.findAll(spec, new PageRequest(0, 1, Sort.Direction.DESC, "orderNumber"));
            if (0 < orders.getContent().size()) {
                lastDocumentNumber = parseLastThreeNumbers(orders.iterator().next().getOrderNumber());
            }
        }
        if (regardingReports) {
            List<Report> reports = reportRepository.findAll(new DocumentNumberContains(yymm(date)));
            List<String> docNos = reports.stream().map(r -> r.getDocumentNumber()).collect(Collectors.toList());
            Collections.sort(docNos, new DocumentNumberInFormatComparator().reversed());
            if (0 < docNos.size()) {
                int lastThreeNumbersParsed = parseLastThreeNumbers(docNos.iterator().next());
                if (lastThreeNumbersParsed > lastDocumentNumber) {
                    lastDocumentNumber = lastThreeNumbersParsed;
                }
            }
        }

        int generated = lastDocumentNumber + 1;

        return new StringBuilder().append(year.toString().substring(2))
                .append(("00" + month).substring(month.toString().length()))
                .append(("000" + generated).substring(new Integer(generated).toString().length()))
                .toString();

    }

    private int parseLastThreeNumbers(String orderNumber) {
        String numbers = orderNumber.replaceAll("[\\D]", "");
        String lastThreeNumbers = numbers.substring(Math.max(0, numbers.length() - 3));
        int parsedLastThreeNumbers = Integer.parseInt(lastThreeNumbers);
        return parsedLastThreeNumbers;
    }

    @SuppressWarnings("unused")
    private Specification<Order> inMonthAndStartsWithLetterB(LocalDate date) {
        return where(OrderCreatedBetweenSpecification.inMonth(date))
                .and(new OrderNumberStartsWith("B"));
    }

    private String yymm(LocalDate localDate) {
        return DateTimeFormatter.ofPattern("yyMM").format(localDate);
    }

    private class OrderNumberStartsWith implements Specification<Order> {

        private String start;

        public OrderNumberStartsWith(String start) {
            this.start = start;
        }

        @Override
        public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(root.get("orderNumber"), start + "%");
        }

    }

    private class DocumentNumberContains implements Specification<Report> {

        private String pattern;

        public DocumentNumberContains(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.like(root.get("documentNumber"), "%" + pattern + "%");
        }

    }

}
