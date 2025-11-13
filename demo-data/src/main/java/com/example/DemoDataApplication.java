package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@SpringBootApplication
public class DemoDataApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada ...");
	}
	
//	@Bean
	CommandLineRunner ejemplos(EjemplosDatos demos) {
		return arg -> {
			demos.run();
		};
	}
	
//	@Bean
	CommandLineRunner demoJDBC(JdbcTemplate db) {
		return _ -> {
			record Actor(int id, String firstName, String lastName) {}
			class ActorRowMapper implements RowMapper<Actor> {
				@Override
				public Actor mapRow(ResultSet row, int index) throws SQLException {
					return new Actor(row.getInt(1), row.getString("first_name"), row.getString(3));
				}				
			}
			
			try {
				System.out.println(">>> Nº Actores: " + db.queryForObject("select count(*) from actor", Integer.class));
				System.out.println(">>> Listado inicial");
				db.query("select * from actor", new ActorRowMapper()).forEach(System.out::println);
				var id = 2;
				var actor = db.queryForObject(
						"select * from actor where actor_id = ?", // "select * from actor where actor_id=" + id
						(row, index) -> new Actor(row.getInt(1), row.getString("first_name"), row.getString(3)), 
						id);
				System.out.println("Consulta id: %d => %s".formatted(id, actor));
				var rows = db.update(
						"insert into actor(first_name,last_name) values(?, ?)", 
						"Pepito", "Grillo");
				System.out.println(">>> Insertados: " + rows);
				var newId =  db.queryForObject("select max(actor_id) from actor", Integer.class);
				actor = db.queryForObject(
						"select * from actor where actor_id=?", 
						new ActorRowMapper()
						, newId);
				System.out.println(">>> Consulta nuevo: %d => %s".formatted(newId, actor));
				rows = db.update(
						"update actor set first_name = ?, last_name = ? where actor_id=?", 
						actor.firstName().toUpperCase(), actor.lastName().toUpperCase(), actor.id());
				System.out.println(">>> Modificados: " + rows);
				System.out.println(">>> Listado actual");
				db.query("select * from actor", new ActorRowMapper()).forEach(System.out::println);
				rows = db.update("delete actor where actor_id=?", actor.id());
				System.out.println(">>> Borrados: " + rows);
				System.out.println(">>> Sobreviven: ");
				db.query("select * from actor", new ActorRowMapper()).forEach(System.out::println);
				System.out.println(">>> Fallo forzado ");
//				var idDel = "2 OR 1=1; DROP TABLE ...";
//				rows = db.update("delete actor where id="+idDel);
				rows = db.update("delete actor where actor_id=?", 2);
			} catch (DataAccessException e) {
				System.err.println(e.getMessage());
			}
		};
	}

}
