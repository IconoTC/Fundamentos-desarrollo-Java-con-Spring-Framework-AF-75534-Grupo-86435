package com.example.contracts.application.services;

import com.example.core.contracts.domain.services.ProjectionDomainService;
import com.example.domain.entities.Actor;

public interface ActorsService extends ProjectionDomainService<Actor, Integer> {
	void repartePremios(String[] premiados);
}
