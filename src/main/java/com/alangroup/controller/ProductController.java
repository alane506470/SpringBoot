package com.alangroup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alangroup.vo.Product;

@RestController
@RequestMapping(value="/alanapi")
public class ProductController {

	@GetMapping(value="/products/{id}")
	public Product getProduct(@PathVariable("id")String id) {
		Product product = new Product();
		product.setId(id);
		product.setName("Horror movie episode" + id);
		return product;
	}
	
}
