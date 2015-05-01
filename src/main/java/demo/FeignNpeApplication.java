package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.context.ConfigurableApplicationContext;

@EnableHystrix
@EnableEurekaClient
@EnableFeignClients
@EnableOAuth2Resource
@SpringBootApplication
public class FeignNpeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(
				FeignNpeApplication.class, args);
		ClientApi api = context.getBean(ClientApi.class);
		System.out.println(api);
	}
}