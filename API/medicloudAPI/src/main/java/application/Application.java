package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.RepositoryConfiguration;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"application","model", "repository","provider", "service", "healthProfessionalService", "patientService", "personService"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "model")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
