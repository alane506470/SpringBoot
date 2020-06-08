package com.alangroup.vo;

import org.springframework.data.mongodb.core.mapping.Document;

// 指定product物件在mongoDB要存到打個集合(TABLE)，若沒定義，預設儲存的集合會直接依類別名稱而定。
// Java類別有什麼欄位，MongoDB的文件就儲存什麼欄位。函式庫會偵測欄位的資料型態
// 圖中的「_id」欄位，是MongoDB固定使用的文件主鍵，不會重複。並且Java類別中若有名稱為「id」的欄位，都將被轉換成「_id」。程式讀取資料時，也會轉換回來。
@Document(collection = "products")
public class Product {
	private String id;
	private String name;
	private Integer price;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	
}
