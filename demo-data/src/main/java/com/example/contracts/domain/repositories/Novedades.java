package com.example.contracts.domain.repositories;

import java.util.List;

public interface Novedades<E> {
	List<E> findByIdGreaterThanEqual(int primero);

}
