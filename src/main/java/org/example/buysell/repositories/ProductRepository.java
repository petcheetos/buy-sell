package org.example.buysell.repositories;

import org.example.buysell.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findByCity(String city);

    List<Product> findByCityAndTitleContainingIgnoreCase(String city, String title);
}