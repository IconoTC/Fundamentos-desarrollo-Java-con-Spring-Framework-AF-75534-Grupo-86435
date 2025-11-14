package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.ActorsRepository;
import com.example.contracts.domain.repositories.CategoriesRepository;
import com.example.contracts.domain.repositories.FilmsRepository;
import com.example.domain.entities.Actor;
import com.example.domain.entities.Category;

@Component
public class EjemplosDatos {
	public void run() {
		System.out.println("Elemplos");
		transaccion();
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

	public void consultas() {
		daoActors.findTop5ByFirstNameStartingWithIgnoreCaseOrderByLastNameDesc("p").forEach(IO::println);
		daoActors.findTop5ByFirstNameStartingWithIgnoreCase("p", Sort.by("FirstName").descending()).forEach(IO::println);
//		daoActors.findByIdGreaterThanEqual(200).forEach(IO::println);
//		daoActors.findNovedadesJPQL(200).forEach(IO::println);
//		daoActors.findNovedadesSQL(200).forEach(IO::println);
		daoActors.findAll((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("id"), 200)).forEach(IO::println);
		daoActors.findAll((root, query, builder) -> builder.lessThan(root.get("id"), 10)).forEach(IO::println);
	}

//	@Transactional
	public void navegacion() {
		daoActors.findByIdGreaterThanEqual(200).forEach(item->{
			IO.println(item);
			item.getFilmActors().forEach(p->{
				IO.println("\t" + p.getFilm().getTitle());
			});
		});
	}
	
	@Autowired
	FilmsRepository daoPelis;

	public void peliculas() {
		daoPelis.findById(1).ifPresent(
				item->IO.println("%d %s (%s)".formatted(item.getId(), item.getTitle(), item.getLanguage().getName()))
				);
	}

	@Autowired
	CategoriesRepository daoCategories;

	@Transactional(rollbackFor = Exception.class)
	public void transaccion() {
		daoActors.save(new Actor("uno", "Coton1"));
		daoActors.save(new Actor(222, "dos", "Coton2"));
		daoActors.save(new Actor("dos", "Coton2"));
		daoActors.deleteById(1);
		daoCategories.findByIdGreaterThanEqual(1);
		daoCategories.save(new Category(1, "Serie B"));
	}

}
