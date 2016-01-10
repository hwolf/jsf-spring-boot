package pfx.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import hwolf.spring.boot.jsf.EnableJsf;

@SpringBootApplication
@EnableJsf("pfx")
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).run(args);
    }
}
