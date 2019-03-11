package com.skubana.assessment.repository;

import com.skubana.assessment.domain.Order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>{

}
