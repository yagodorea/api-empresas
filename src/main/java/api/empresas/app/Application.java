package api.empresas.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "api.empresas")
@EnableJpaRepositories(basePackages = "api.empresas.repository")
@EntityScan(basePackages = "api.empresas.domain")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
