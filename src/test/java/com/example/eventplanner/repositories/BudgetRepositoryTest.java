package com.example.eventplanner.repositories;

import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.Purchase;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.user.ServiceProductProvider;
import com.example.eventplanner.repositories.event.BudgetRepository;
import com.example.eventplanner.repositories.order.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
@Transactional
public class BudgetRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TestEntityManager entityManager;

    private ServiceProductProvider provider;
    private Product product;
    private Purchase purchase;
    private Budget budget;
    private Event event;

    @BeforeEach
    void setUp() {
        provider = new ServiceProductProvider();
        provider.setEmail("provider@test.com");
        provider = em.persist(provider);

        product = new Product();
        product.setName("Test product");
        product.setServiceProductProvider(provider);
        product = em.persist(product);

        purchase = new Purchase();
        purchase.setProduct(product);
        purchase.setActive(true);
        purchase = em.persist(purchase);

        budget = new Budget();
        budget.setPurchases(List.of(purchase));
        budget = em.persist(budget);

        event = new Event();
        event.setName("Test Event");
        event.setBudgets(List.of(budget));
        event = em.persist(event);

        em.flush();
    }

    @Test
    void deleteById_ShouldSetActiveFalse() {
        purchaseRepository.deleteById(purchase.getId());
        em.flush();
        em.clear();
        assertNull(em.find(Purchase.class, purchase.getId()));
    }

    @Test
    void findByProviderId_ShouldReturnPurchases() {
        List<Purchase> result = purchaseRepository.findByProviderId(provider.getId());
        assertEquals(1, result.size());
        assertEquals(purchase.getId(), result.get(0).getId());
    }

    @Test
    void findByProductId_ShouldReturnPurchases() {
        List<Purchase> result = purchaseRepository.findByProductId(product.getId());
        assertEquals(1, result.size());
        assertEquals(purchase.getId(), result.get(0).getId());
    }

    @Test
    void setNewAmount_shouldUpdatePlannedSpending() {
        budget.setPlannedSpending(100.0);
        budget.setCurrentSpent(20.0);
        budget.setPurchases(new ArrayList<>(List.of(purchase)));
        budget = budgetRepository.save(budget);

        budgetRepository.setNewAmount(budget.getId(), 250.0);
        entityManager.clear();

        Budget updated = budgetRepository.findById(budget.getId()).orElseThrow();

        assertEquals(250.0, updated.getPlannedSpending(), 0.01);
        assertEquals(20.0, updated.getCurrentSpent(), 0.01);
    }
}
