package com.nix.phoneapp.comp.orders.persistence;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
	

}
