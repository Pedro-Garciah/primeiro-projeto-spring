package com.example.demo.controllers;

import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductRepository;
import com.example.demo.domain.product.RequestProduct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public ResponseEntity getAllProducts() {
//        var allProducts = repository.findAll();
        var allProducts = repository.findAllByActiveTrue();
        return ResponseEntity.ok(allProducts);

    }

    @PostMapping
    public ResponseEntity registerProduct(@RequestBody @Valid RequestProduct data) {
        Product newProduct = new Product(data);
        repository.save(newProduct);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateProduct(@RequestBody @Valid RequestProduct data) {
        Optional<Product> optionalProduct = repository.findById(data.id());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(data.name());
            product.setPrice_in_cents(data.price_in_cents());
            return ResponseEntity.ok(product);
        } else {
            // Exceção que será tratada no Handler
            throw new EntityNotFoundException();
        }
    }

    // Seta o produto como active=false no BD, "deletando" o produto
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteProduct(@PathVariable String id) {
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(false);
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException();
        }
    }

    //Realmente deleta do BD
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteProduct(@PathVariable String id) {
//        repository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }

    // Talvez ajeitar dps?
//    @PutMapping("/{id}")
//    public ResponseEntity updateProduct(@PathVariable String id, @RequestBody @Valid RequestProduct data){
//        System.out.println(data);
//        Optional<Product> product = repository.findById(data.id());
//        product.get().setName(data.name());
//        product.get().setPrice_in_cents(data.price_in_cents());
//        return ResponseEntity.ok(product);
//    }
}
