package pl.company.foodatu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class FoodatuApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodatuApplication.class, args);
	}

}
