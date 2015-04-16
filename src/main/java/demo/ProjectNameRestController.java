package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author Spencer Gibb
*/
@RestController
@RefreshScope
public class ProjectNameRestController {

	@Value("${spring.application.name}")
	private String projectName;

	@RequestMapping("/")
	public String projectName() {
		return this.projectName;
	}
}
