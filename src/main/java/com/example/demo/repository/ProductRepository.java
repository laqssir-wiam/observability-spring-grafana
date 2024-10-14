package com.example.demo.repository;

import com.example.demo.enteties.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author WIAM
 **/
public interface ProductRepository extends JpaRepository<Product,Long> {
}
