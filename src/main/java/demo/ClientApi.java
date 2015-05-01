package demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("clientsample")
interface ClientApi {

	@RequestMapping(
			value = "/who",
			method = RequestMethod.GET
	)
	String getWho ();
}