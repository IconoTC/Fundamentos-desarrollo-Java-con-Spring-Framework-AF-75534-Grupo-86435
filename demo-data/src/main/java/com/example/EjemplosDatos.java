package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.contracts.domain.repositories.ActorsRepository;
import com.example.domain.entities.Actor;

@Component
public class EjemplosDatos {
	public void run() {
		System.out.println("Elemplos");
	}

	@Autowired
	ActorsRepository daoActors;
	
	public void actores() {
//		daoActors.findAll().forEach(IO::println);
		var id = daoActors.save(new Actor("Carmelo", "Coton")).getId();
		daoActors.findAll().forEach(IO::println);
		var item = daoActors.findById(id);
		if(item.isEmpty()) {
			System.err.println("No encontrado");
		} else {
			var a = item.get();
			IO.println("Actor leido: " + a);
			a.setFirstName(a.getFirstName().toUpperCase());
			daoActors.save(a);
		}
		daoActors.findAll().forEach(IO::println);
		daoActors.deleteById(id);
		daoActors.findAll().forEach(IO::println);
	}
}
