package de.switajski.priebes.flexibleorders.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;

@Controller
public class OrderController {

    @Autowired
    OrderRepository orderRepo;

    @RequestMapping(value = "/ordered", method = RequestMethod.GET)
    public @ResponseBody List<Order> listAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orders;
    }

    @RequestMapping(value = "/ordered/{docNo}", method = RequestMethod.GET)
    public @ResponseBody Order listOrder(
            @PathVariable("docNo") String docNo) {
        Order order = orderRepo.findByOrderNumber(docNo);
        return order;
    }

}
