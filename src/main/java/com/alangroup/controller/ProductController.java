package com.alangroup.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alangroup.vo.Product;

@RestController
@RequestMapping(value="/alanapi")
public class ProductController {
	
	private List<Product> productDB = new ArrayList<>();

	@GetMapping(value="/productstest/{id}")
	public ResponseEntity<Product> getProductTest(@PathVariable("id")String id) {
		Product product = new Product();
		product.setId(id);
		product.setName("Horror movie episode" + id);
//		return ResponseEntity.ok().body(product);
// 回傳ResponseEntity時，可以選擇一些常見的HTTP狀態，例如200（OK）、201（Created）、404（Not Found）、422（Unprocessable Entity）等。
		return new ResponseEntity<>(product,HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value="/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id")String id) {
		Optional<Product> productOp = productDB.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst();
		if(productOp.isPresent()) {
			Product product = productOp.get();
			return ResponseEntity.ok().body(product);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(value="/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product request) {
		boolean isIdDuplicated = productDB.stream().anyMatch(p -> p.getId().equals(request.getId()));
		if (isIdDuplicated) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		Product product = new Product();
		product.setId(request.getId());
		product.setName(request.getName());
		productDB.add(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(product.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	
}
