import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.functions.Action1;

import java.io.IOException;
import java.util.ArrayList;

@Configuration
@EnableAutoConfiguration
@EnableHystrix
public class Application {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    @RequestMapping(value = "/makebunchofcalls/{num}")
    public static class EricController {

        @Autowired
        private EricComponent ericComponent;

        @RequestMapping(method = {RequestMethod.GET})
        ArrayList<String> doCalls(@PathVariable Integer num) throws IOException {
            final ArrayList<String> ale = new ArrayList<>(num);
            for (int i = 0; i < num; i++) {
                rx.Observable<Eric> oe = this.ericComponent.doRestTemplateCallAsync(i);
                //oe.subscribe(new Action1<Eric>() {
                oe.toBlockingObservable().forEach(new Action1<Eric>() {
                    @Override
                    public void call(Eric e) {  // AT RUNTIME, ClassCastException
                        ale.add(e.val + " " + e.status);
                    }
                });
            }

            return ale;
        }
    }

    @Component
    public static class EricComponent {

        // async version =========== using reactive execution via rx library from netflix ==============
        @Autowired
        RestTemplate restTemplate;

        @HystrixCommand(fallbackMethod = "defaultRestTemplateCallAsync", commandKey = "dogeAsync")
        public Observable<Eric> doRestTemplateCallAsync(final int callNum) {
            return new ObservableResult<Eric>() {
                @Override
                public Eric invoke() {  // NEVER CALLED
                    ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:8761/v2/apps", String.class);  // actually make a call
                    System.out.println("*************** call successfull: " + new Integer(callNum) + " *************");
                    return new Eric(new Integer(callNum).toString(), "ok");
                }
            };
        }

        public Eric defaultRestTemplateCallAsync(final int callNum) {
            System.out.println("!!!!!!!!!!!!! call bombed " + new Integer(callNum) + "!!!!!!!!!!!!!");
            return new Eric(new Integer(callNum).toString(), "bomb");
        }
    }

    public static class Eric {
        String val;
        String status;

        Eric(String val, String status) {
            this.val = val;
            this.status = status;
        }
    }
}
