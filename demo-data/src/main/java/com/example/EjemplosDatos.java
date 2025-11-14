package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.contracts.domain.repositories.ActorsRepository;
import com.example.contracts.domain.repositories.CategoriesRepository;
import com.example.contracts.domain.repositories.FilmsRepository;
import com.example.domain.entities.Actor;
import com.example.domain.entities.Category;
import com.example.domain.entities.models.ActorDto;
import com.example.domain.entities.models.ActorShort;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class EjemplosDatos {
	@Lazy @Autowired EjemplosDatos self;
	
	public void run() {
		System.out.println("Elemplos");
		self.transaccion();
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
	public void validaciones() {
		var a = new Actor(null, "Ñ A");
		if(a.isInvalid()) {
			System.err.println(a.getErrorsMessage());
		} else {
			daoActors.save(a);
		}
	}

	record Pelis(int id, @JsonProperty("titulo") String title) {}
	
	public void proyecciones() {
//		daoActors.findByIdGreaterThanEqual(200).forEach(item -> IO.println(ActorDto.from(item)));
//		daoActors.queryByIdGreaterThanEqual(200).forEach(IO::println);
//		daoActors.getByIdGreaterThanEqual(200).forEach(item -> IO.println(item.getId() + " " + item.getNombre()));
//		daoActors.searchByIdGreaterThanEqual(200, ActorDto.class).forEach(IO::println);
		daoActors.searchByIdGreaterThanEqual(200, ActorShort.class).forEach(item -> IO.println(item.getId() + " " + item.getNombre()));
//		daoPelis.findAllBy(PageRequest.of(0, 10), Pelis.class).getContent().forEach(IO::println);
	}

	@Autowired 
	ObjectMapper jsonMapper;
	
	public void serializacion() {
		XmlMapper xmlMapper = new XmlMapper();
		try {
			var c = daoCategories.findById(1).get();
			IO.println(jsonMapper.writeValueAsString(c));
			IO.println(xmlMapper.writeValueAsString(c));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
