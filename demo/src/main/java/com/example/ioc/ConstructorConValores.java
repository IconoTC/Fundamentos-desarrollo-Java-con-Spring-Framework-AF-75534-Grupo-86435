package com.example.ioc;

import java.beans.ConstructorProperties;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class ConstructorConValores {
	private final NotificationService notify;
	
	@ConstructorProperties({"version", "otroAutor"})
	public ConstructorConValores(int version, String otroAutor, NotificationService notify) {
		this.notify = notify;
		notify.add(getClass().getSimpleName() + " - Version: " + version + " Autor: " + otroAutor);
	}
	
	public void titulo(String tratamiento, String autor) {
		System.err.println(tratamiento.toUpperCase() + " " + autor.toUpperCase());
	}
	public void titulo(String autor) {
		System.err.println(autor.toUpperCase());
	}

//	public String dameValor(String autor) {
//		return Optional.empty();		
//	}

	public Optional<String> dameValor(String autor) {
		return Optional.empty();		
	}
}
