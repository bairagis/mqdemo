package com.mqdemo.product.dao;

import com.mqdemo.product.dao.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ProductDao extends CrudRepository<Product, Long> {
}
