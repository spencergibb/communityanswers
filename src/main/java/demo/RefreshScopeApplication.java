package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RefreshScopeApplication {

	@RestController
	@RefreshScope
	public static class ProjectNameRestController {

		@Value("${spring.application.name}")
		private String projectName;

		@RequestMapping("/")
		public String projectName() {
			return this.projectName;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(RefreshScopeApplication.class, args);
	}
}