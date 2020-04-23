package com.alangroup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alangroup.dao.MockProductDAO;
import com.alangroup.vo.Product;
import com.alangroup.vo.ProductQueryParameter;

@Service
public class ProductSercvice {

	@Autowired
	private MockProductDAO productDAO;

	public Product createProduct(Product request) {
		boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
		if (isIdDuplicated) {
			throw new Exception("Id is Duplicated");
		}
		
		Product product = new Product();
		product.setId(request.getId());
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		return productDAO.insert(product);
	}

	public Product replaceProduct(String id, Product request) {
		boolean isPresent = productDAO.find(id).isPresent();
	    if (!isPresent) {
	        throw new NotFoundException("Can't find product.");
	    }

	    return productDAO.replace(id, request);
		
	}

	public void deleteProduct(String id) {
		productDAO.delete(id);
	}

	public Product getProduct(String id) {
		return productDAO.find(id)
				.orElseThrow(()-> new NotFoundException("can't find product"));
	}

	public List<Product> getProducts(ProductQueryParameter param) {
		  return productDAO.find(param);
	}
}
