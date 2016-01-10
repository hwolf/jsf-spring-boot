package hwolf.spring.boot.jsf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PropertySource("classpath:jsf-spring-boot-starter.properties")
@Import(JsfConfiguration.class)
public @interface EnableJsf {

    /**
     * Alias for {@link #basePackages}.
     * <p>
     * Allows for more concise annotation declarations if no other attributes are needed &mdash; for
     * example, {@code @ComponentScan("org.my.pkg")} instead of
     * {@code @EnableJsf(basePackages = "org.my.pkg")}.
     */
    @AliasFor("basePackages")
    String[]value() default {};

    /**
     * Base packages to scan for annotated JSF components.
     * <p>
     * {@link #value} is an alias for (and mutually exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses} for a type-safe alternative to String-based package names.
     */
    @AliasFor("value")
    String[]basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages} for specifying the packages to scan for
     * annotated JSF components. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that serves no
     * purpose other than being referenced by this attribute.
     */
    Class<?>[]basePackageClasses() default {};

}
