package com.example.domain.entities.models;

import org.springframework.data.annotation.PersistenceCreator;

import com.example.domain.entities.Actor;

import lombok.Data;

@Data
public class ActorDto {
	private int id;
	private String nombre;
	private String apellido;
	
	public ActorDto() {}
	
	@PersistenceCreator
	public ActorDto(int id, String firstName, String lastName) {
		super();
		this.id = id;
		this.nombre = firstName;
		this.apellido = lastName;
	}

	public static Actor from(ActorDto source) {
		return new Actor(source.getId(), source.getNombre(), source.getApellido());
	}

	public static ActorDto from(Actor source) {
		return new ActorDto(source.getId(), source.getFirstName(), source.getLastName());
	}
}
