package hwolf.spring.boot.servlet;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * Invokes a {@link ServletContainerInitializer} hopefully similar in the way a JEE Servlet
 * container would do that.
 */
public class ServletContainerInitializerInvoker implements ServletContextInitializer {

    private static final Log LOGGER = LogFactory.getLog(ServletContainerInitializerInvoker.class);

    private final ServletContainerInitializer initializer;
    private final Collection<String> basePackages;

    public ServletContainerInitializerInvoker(ServletContainerInitializer initializer,
            Collection<String> basePackages) {
        this.initializer = initializer;
        this.basePackages = new HashSet<>(basePackages);
    }

    @Override
    public void onStartup(ServletContext context) throws ServletException {
        Class<?>[] types = initializer.getClass().getAnnotation(HandlesTypes.class).value();
        Set<Class<?>> facesTypes = findFacesTypes(types);
        initializer.onStartup(facesTypes, context);
    }

    private Set<Class<?>> findFacesTypes(Class<?>[] types) throws ServletException {
        ClassPathScanningCandidateComponentProvider scanner = buildClassPathScanner(types);

        Set<Class<?>> result = new HashSet<>();
        for (String basePackage : basePackages) {
            LOGGER.info("Scan package " + basePackage + " for JSF classes");
            scanPackage(scanner, basePackage, result);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private ClassPathScanningCandidateComponentProvider buildClassPathScanner(Class<?>[] types) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                false);
        for (Class<?> type : types) {
            if (type.isAnnotation()) {
                scanner.addIncludeFilter(new AnnotationTypeFilter((Class<Annotation>) type));
            } else {
                scanner.addIncludeFilter(new AssignableTypeFilter(type));
            }
        }
        return scanner;
    }

    private void scanPackage(ClassPathScanningCandidateComponentProvider scanner, String basePackage,
            Collection<Class<?>> result) throws ServletException {
        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            try {
                LOGGER.info("Found JSF class " + bd.getBeanClassName());
                result.add(Class.forName(bd.getBeanClassName()));
            } catch (ClassNotFoundException ex) {
                throw new ServletException(ex);
            }
        }
    }
}
