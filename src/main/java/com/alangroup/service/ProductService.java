package com.alangroup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
// org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Service;

import com.alangroup.dao.MockProductDAO;
import com.alangroup.exception.NotFoundException;
import com.alangroup.exception.UnprocessableException;
import com.alangroup.repository.ProductRepository;
import com.alangroup.vo.Product;
import com.alangroup.vo.ProductQueryParameter;

@Service
public class ProductService {

	@Autowired
	private MockProductDAO productDAO;
	@Autowired
	private ProductRepository productoryRepository;

	public Product createProduct(Product request) {
//		boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
//		if (isIdDuplicated) {
//			throw new UnprocessableException("Id is Duplicated");
//		}
		
		Product product = new Product();
		product.setId(request.getId());
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		return (Product) productoryRepository.insert(product);
	}

	public Product replaceProduct(String id, Product request) {
//		boolean isPresent = productDAO.find(id).isPresent();
//	    if (!isPresent) {
//	        throw new NotFoundException("Can't find product.");
//	    }
		
		Product oldProduct = getProduct(id);
		Product product = new Product();
		product.setId(oldProduct.getId());
		product.setName(request.getName());
		product.setPrice(request.getPrice());
	    return (Product) productoryRepository.save(product);
		
	}

	public void deleteProduct(String id) {
//		productDAO.delete(id);
		productoryRepository.deleteById(id);
	}

	public Product getProduct(String id) {
//		return productDAO.find(id)
//				.orElseThrow(()-> new NotFoundException("can't find product"));
		try {
			return (Product) productoryRepository.findById(id)
					.orElseThrow(()-> new NotFoundException("can't find product"));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Product> getProducts(ProductQueryParameter param) {
		String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
		String orderBy = param.getOrderBy();
	    String sortRule = param.getSortRule();
	    Sort sort = Sort.unsorted();
	    if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
	        Sort.Direction direction = Sort.Direction.fromString(sortRule);
	        System.out.println(sortRule);
	        sort = Sort.by(direction, orderBy);
	    }

	    return productoryRepository.findByNameLikeIgnoreCase(nameKeyword, sort);
	}
	
	public List<Product> findByNameLike(String name) {
//		return productDAO.findByName(name);
		Sort sort = Sort.by(Sort.Direction.DESC, "price");
		return productoryRepository.findByNameLikeIgnoreCase(name,sort);
	}
}
