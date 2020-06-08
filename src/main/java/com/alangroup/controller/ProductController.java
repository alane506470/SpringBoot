package com.alangroup.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alangroup.service.ProductService;
import com.alangroup.vo.Product;
import com.alangroup.vo.ProductQueryParameter;

@RestController
@RequestMapping(value="/alanapi")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	// 測試api
	@GetMapping(value="/productstest/{id}")
	public ResponseEntity<Product> getProductTest(@PathVariable("id")String id) {
		Product product = new Product();
		product.setId(id);
		product.setName("Horror movie episode" + id);
		
// 回傳ResponseEntity時，可以選擇一些常見的HTTP狀態，例如200（OK）、201（Created）、404（Not Found）、422（Unprocessable Entity）等。
		return ResponseEntity.ok().body(product);
	}
	
	// 查詢
	@GetMapping(value="/productbyid/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id")String id) {
		Product product = productService.getProduct(id);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", 
		"application/json;charset=UTF-8");
		return ResponseEntity.ok().headers(responseHeaders).body(product);
		
	}
	
	@GetMapping(value="/products/{name}")
	public ResponseEntity<List<Product>> getProductsByName(@PathVariable("name")String name) {
		List<Product> products= new ArrayList<>();
		products = productService.findByNameLike(name);
		return ResponseEntity.ok().body(products);
	}
	
	@PostMapping(value="/products")
	public ResponseEntity<Product> createProduct(@RequestBody Product request) {
		Product product = productService.createProduct(request);
		
		URI location = ServletUriComponentsBuilder.fromUriString("http://localhost/alanapi/productbyid")
				.path("/{id}")
				.buildAndExpand(product.getId())
				.toUri();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", 
		"application/json;charset=UTF-8");
		return ResponseEntity.created(location).headers(responseHeaders).body(product);
	}
	
	@PutMapping(value="/products/{id}")
	public ResponseEntity<Product> replaceProduct(@RequestBody Product request,@PathVariable(value="id")String id) {
		Product product = productService.replaceProduct(id, request);
	    return ResponseEntity.ok().body(product);
				
	}
	
	//操作完畢後可回傳狀態碼204（No Content）
	//，意思是請求成功，但回應主體沒有內容。它跟200很像，只差在有無內容而已。
	@DeleteMapping(value="/products/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
	
	// 三、網址參數
	@GetMapping(value="/products/oneParam")
	public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "keyword",required = false)String keyword) {
		return null;
//		List<Product> products = new ArrayList<>();
//		
//		if(keyword != null) {
//			products = productService.findByName(keyword);
//		}		
//		return ResponseEntity.ok(products);
	}
	
	@GetMapping(value="/products")
	public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param) {
		 List<Product> products = productService.getProducts(param);
		 return ResponseEntity.ok(products);
	}
	
}
