package com.example.demo.web;

import com.example.demo.enteties.Product;
import com.example.demo.model.Post;
import com.example.demo.repository.ProductRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author WIAM
 **/
@RestController
public class ProductController {
    private ProductRepository productRepository;
    private RestClient restClient;
    public ProductController(ProductRepository productRepository, RestClient.Builder restClient) {
        this.productRepository = productRepository;
        this.restClient = restClient.baseUrl("https://jsonplaceholder.typicode.com").build();
    }
    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    @GetMapping("/posts")
    public List<Post> allPosts(){
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Post>>() {});
    }
}
