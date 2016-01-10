package hwolf.spring.boot.jsf;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.sun.faces.config.FacesInitializer;

import hwolf.spring.boot.servlet.ServletContainerInitializerInvoker;

@Configuration
public class JsfConfiguration implements ImportAware, EnvironmentAware {

    private Environment environment;
    private AnnotationMetadata metaData;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata metaData) {
        this.metaData = metaData;
    }

    @Bean
    public ServletContextInitializer configureJsf() {
        return new ServletContainerInitializerInvoker(new FacesInitializer(), getBasePackages());
    }

    private Collection<String> getBasePackages() {
        Set<String> basePackages = new LinkedHashSet<String>();
        AnnotationAttributes attrs = new AnnotationAttributes(
                metaData.getAnnotationAttributes(EnableJsf.class.getName()));
        String[] basePackagesArray = attrs.getAliasedStringArray("basePackages", EnableJsf.class,
                metaData.getClassName());
        for (String pkg : basePackagesArray) {
            String[] tokenized = StringUtils.tokenizeToStringArray(environment.resolvePlaceholders(pkg),
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            basePackages.addAll(Arrays.asList(tokenized));
        }
        for (Class<?> clazz : attrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(metaData.getClassName()));
        }
        return basePackages;
    }
}
