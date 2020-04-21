package com.alangroup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alangroup.vo.Product;

@RestController
@RequestMapping(value="/alanapi")
public class ProductController {

	@GetMapping(value="/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id")String id) {
		Product product = new Product();
		product.setId(id);
		product.setName("Horror movie episode" + id);
//		return ResponseEntity.ok().body(product);
// 回傳ResponseEntity時，可以選擇一些常見的HTTP狀態，例如200（OK）、201（Created）、404（Not Found）、422（Unprocessable Entity）等。
		return new ResponseEntity<>(product,HttpStatus.NOT_FOUND);
	}
	
}
