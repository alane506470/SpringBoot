package com.alangroup.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
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

import com.alangroup.vo.Product;
import com.alangroup.vo.ProductQueryParameter;

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
			// 請求格式正確但是有語意錯誤，無法回應
			return ResponseEntity.unprocessableEntity().build();
		}
		
		Product product = new Product();
		product.setId(request.getId());
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		productDB.add(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(product.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(value="/products/{id}")
	public ResponseEntity<Product> replaceProduct(@RequestBody Product request,@PathVariable(value="id")String id) {
		Optional<Product> productOp = productDB.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst();
		if(!productOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}

	    Product oldProduct = productOp.get();
	    int productIndex = productDB.indexOf(oldProduct);

	    Product product = new Product();
	    product.setId(oldProduct.getId());
	    product.setName(request.getName());
	    productDB.set(productIndex, product);

	    return ResponseEntity.ok().body(product);
				
	}
	
	//操作完畢後可回傳狀態碼204（No Content）
	//，意思是請求成功，但回應主體沒有內容。它跟200很像，只差在有無內容而已。
	@DeleteMapping(value="/products/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {
		Optional<Product> productOp = productDB.stream()
			.filter(p -> p.getId().equals(id))
			.findFirst();
		
		if(productOp.isPresent()) {
			Product product = productOp.get();
			productDB.remove(product);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	// 三、網址參數
	@GetMapping(value="/products/oneParam")
	public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "keyword",required = false)String keyword) {
		List<Product> products;
		
		if(keyword == null) {
			products = productDB; 
		} else {
			products = productDB.stream()
					.filter(p -> p.getName().contains(keyword))
					.collect(Collectors.toList());
		}
		
		return ResponseEntity.ok(products);
	}
	
	@GetMapping(value="/products")
	public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param) {
		  Stream<Product> stream = productDB.stream();

		    if (param.getKeyword() != null) {
		        stream = stream
		                .filter(p -> p.getName().contains(param.getKeyword()));
		    }

		    if ("price".equals(param.getOrderBy()) && param.getSortRule() != null) {
		        Comparator<Product> comparator = param.getSortRule().equals("asc")
		                ? Comparator.comparing(Product::getPrice)
		                : Comparator.comparing(Product::getPrice).reversed();

		        stream = stream.sorted(comparator);
		    }

		    List<Product> products = stream.collect(Collectors.toList());

		    return ResponseEntity.ok(products);
	}
	
}
