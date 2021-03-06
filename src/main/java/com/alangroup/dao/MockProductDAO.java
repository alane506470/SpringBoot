package com.alangroup.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.alangroup.vo.Product;
import com.alangroup.vo.ProductQueryParameter;

@Repository
public class MockProductDAO {

	private List<Product> productDB = new ArrayList();
	
	public Product insert(Product product) {
		productDB.add(product);
		return product;
	}
	
	public Product replace(String id, Product product) {
		Optional<Product> productOp = find(id);
		productOp.ifPresent(p -> {
			p.setName(product.getName());
			p.setPrice(product.getPrice());
		});
		return find(id).get();
	}
	
	public void delete(String id) {
		productDB.removeIf(p -> p.getId().equals(id));
	}
	
	public Optional<Product> find(String id) {
		return productDB.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst();
	}
	
	public List<Product> findByName(String name) {
		return productDB.stream()
		.filter(p -> p.getName().contains(name))
		.collect(Collectors.toList());
	}
	
	public List<Product> find(ProductQueryParameter param) {
		String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
		String orderBy = param.getOrderBy();
		String sortRule = param.getSortRule();
		
		Comparator<Product> comparator = Objects.nonNull(orderBy) && Objects.nonNull(sortRule)
				?configureSortComparator(orderBy,sortRule)
				:(p1,p2) -> 0;
				
		return productDB.stream()
				.filter(p->p.getName().contains(nameKeyword))
				.sorted(comparator)
				.collect(Collectors.toList());
	}
	// 「configureSortComparator」方法會依據排序條件產生比較器（comparator），用於查詢多個產品。
	private Comparator<Product> configureSortComparator(String orderBy,String sortRule) {
		Comparator<Product> comparator = (p1, p2) -> 0;
		
		if(orderBy.equals("price")) {
			comparator = Comparator.comparing(Product::getPrice);
		} else if (orderBy.equalsIgnoreCase("name")) {
			comparator = Comparator.comparing(Product::getName);
		}
		
		if(sortRule.equalsIgnoreCase("desc")) {
			comparator = comparator.reversed();
		}
		
		return comparator;
	}
}
