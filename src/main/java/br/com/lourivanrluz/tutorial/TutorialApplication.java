package br.com.lourivanrluz.tutorial;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableJpaAuditing
public class TutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialApplication.class, args);
	}

	@Bean
	NewTopic notificationTopic() {
		return TopicBuilder.name("Transaction-notification").build();
	}

}
