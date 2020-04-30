package com.alangroup.mapper;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.alangroup.vo.Product;
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

	List<Product> findByNameLike(String productName);
	
	// 找出name欄位值有包含參數的所有文件，且不分大小寫
	List<Product> findByNameLikeIgnoreCase(String name);

	// 找出id欄位值有包含在參數之中的所有文件
	List<Product> findByIdIn(List<String> ids);

	List<Product> findByNameLikeIgnoreCase(String name, Sort sort);
}
