package com.example;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.example.ioc.ClaseNoComponente;
import com.example.ioc.ConstructorConValores;
import com.example.ioc.NotificationService;
import com.example.ioc.Rango;
import com.example.ioc.anotaciones.Remoto;
import com.example.ioc.contratos.Servicio;
import com.example.ioc.contratos.ServicioCadenas;
import com.example.ioc.notificaciones.Sender;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada ...");
	}
	
	@Autowired
	NotificationService notify;
	
//	@Bean
	CommandLineRunner ejemplosIoC(@Autowired(required = false) ClaseNoComponente demo) {
		return arg -> {
//			notify.clear();
			if(demo != null)
				demo.saluda();
//			var demo2 = new ClaseNoComponente(new ConfiguracionImpl(notify, "Yo mismo"));
//			var demo2 = ((BeanFactory)ctx).getBean(ClaseNoComponente.class);
//			demo2.saluda();
			notify.add("Adios mundo");
			IO.println("ejemplosIoC --------------------------->");
			notify.getListado().forEach(IO::println);
			IO.println("< ---------------------------");
		};
	}

//	@Bean
	CommandLineRunner cadenaDeDependencias(ServicioCadenas srv) {
		return _ -> {
//			srv = new ServicioCadenasImpl(new RepositorioCadenasImpl(new ConfiguracionImpl(notify), notify), notify);
			
			srv.get().forEach(notify::add);
			srv.add("Añado");
			srv.modify("Modifico");
			IO.println("ejemplosIoC --------------------------->");
			notify.getListado().forEach(IO::println);
			IO.println("< ---------------------------");
		};
	}
	
//	@Bean
	CommandLineRunner porNombre(Sender correo, Sender fichero, Sender twittea) {
		return _ -> {
			correo.send("Hola mundo");
			fichero.send("Hola mundo");
			twittea.send("Hola mundo");
		};
	}
	
//	@Bean
	CommandLineRunner cualificados(@Qualifier("local") Sender local, @Remoto Sender remoto, Sender primario) {
		return _ -> {
			primario.send("Hola por defecto");
			local.send("Hola local");
			remoto.send("Hola remoto");
		};
	}

//	@Bean
	CommandLineRunner cualificados(List<Sender> senders, Map<String, Sender> mapa, List<Servicio> servicios) {
		return _ -> {
			senders.forEach(s -> s.send(s.getClass().getCanonicalName()));
			mapa.forEach((k, v) -> IO.println("%s -> %s".formatted(k, v.getClass().getCanonicalName())));
			servicios.forEach(s -> IO.println(s.getClass().getCanonicalName()));
		};
	}
	
//	@Bean
	CommandLineRunner valores(ConstructorConValores obj, @Value("${mi.valor:Sin valor}") String cad, Rango rango, @Value("${spring.datasource.url}") String url ) {
		return _ -> {
			notify.clear();
			notify.add(cad);
			notify.add(rango.toString());
			notify.add(url);
			notify.getListado().forEach(IO::println);
			notify.delete(0);
			notify.clear();
		};
	}

	@Bean
	CommandLineRunner xml() {
		return arg -> {
//			IO.println(System.getProperty("java.class.path"));
			try (var contexto = new FileSystemXmlApplicationContext("applicationContext.xml")) {
				var notify = contexto.getBean(NotificationService.class);
				IO.println("===================>");
				var srv = (ServicioCadenas)contexto.getBean("servicioCadenas");
				IO.println(srv.getClass().getName());
				contexto.getBean(NotificationService.class).getListado().forEach(IO::println);
				IO.println("===================>");
				srv.get().forEach(notify::add);
				srv.add("Hola mundo");
				notify.add(srv.get(1));
				srv.modify("modificado");
				IO.println("===================>");
				notify.getListado().forEach(IO::println);
				notify.clear();
				IO.println("<===================");
				((Sender)contexto.getBean("sender")).send("Hola mundo");
			}
		};
	}

}
