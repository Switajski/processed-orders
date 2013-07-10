// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.repository;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.repository.OrderItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect OrderItemRepository_Roo_Jpa_Repository {
    
    declare parents: OrderItemRepository extends JpaRepository<OrderItem, Long>;
    
    declare parents: OrderItemRepository extends JpaSpecificationExecutor<OrderItem>;
    
    declare @type: OrderItemRepository: @Repository;
    
}
