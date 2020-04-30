package com.alangroup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alangroup.dao.MockProductDAO;
import com.alangroup.exception.NotFoundException;
import com.alangroup.exception.UnprocessableException;
import com.alangroup.mapper.ProductRepository;
import com.alangroup.vo.Product;
import com.alangroup.vo.ProductQueryParameter;

@Service
public class ProductService {

	@Autowired
	private MockProductDAO productDAO;
	@Autowired
	private ProductRepository repository;
	
	public Product createProduct(Product request) {
		boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
		if (isIdDuplicated) {
			throw new UnprocessableException("Id is Duplicated");
		}
		
		Product product = new Product();
		product.setId(request.getId());
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		return repository.insert(product);
	}

	public Product replaceProduct(String id, Product request) {
		   Product oldProduct = getProduct(id);

	        Product product = new Product();
	        product.setName(request.getName());
	        product.setPrice(request.getPrice());

	        return repository.save(product);
		
	}

	public void deleteProduct(String id) {
		 repository.deleteById(id);
	}

	public Product getProduct(String id) {
		return repository.findById(id).orElseThrow(()-> new NotFoundException("can't find product"));
	}

	public List<Product> getProducts(ProductQueryParameter param) {
		  return productDAO.find(param);
	}
	
	public List<Product> findByName(String name) {
		return productDAO.findByName(name);
	}
}
