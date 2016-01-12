/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hwolf.showcase;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.atmosphere.cpr.AnnotationScanningServletContainerInitializer;
import org.atmosphere.cpr.ContainerInitializer;
import org.primefaces.push.PushServlet;
import org.primefaces.showcase.filter.UserAgentListener;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import hwolf.spring.boot.jsf.EnableJsf;
import hwolf.spring.boot.servlet.ServletContainerInitializerInvoker;

@SpringBootApplication
@EnableJsf("org.primefaces")
public class Application implements ServletContextInitializer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).run(args);
    }

    @Override
    public void onStartup(ServletContext context) throws ServletException {

        context.addListener(new FileCleanerCleanup());
        context.addFilter("PrimeFaces FileUpload Filter", new FileUploadFilter()) //
                .addMappingForServletNames(null, false, "FacesServlet");

        context.addFilter("Browser Stats Filter", new UserAgentListener()) //
                .addMappingForUrlPatterns(null, false, "/push/chart.xhtml");
        Dynamic registration = context.addServlet("Push Servlet", new PushServlet());
        registration.setAsyncSupported(true);
        registration.addMapping("/primepush/*");
    }

    @Bean(name = "xxx1")
    public ServletContextInitializer xxx1() {
        return new ServletContainerInitializerInvoker(new ContainerInitializer());
    }

    @Bean
    @DependsOn("xxx1")
    public ServletContextInitializer xxx2() {
        return new ServletContainerInitializerInvoker(new AnnotationScanningServletContainerInitializer(),
                "org");
    }
}
