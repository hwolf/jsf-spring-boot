package hwolf.spring.boot.jsf;

import java.net.URL;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link ResourceHandler} which loads {@link ViewResource ViewResources} from
 * {@value META-INF/resource} in the class path.
 *
 * Loading view resources from the standard webapp folder doesn't work if I start my Spring Boot
 * application as executable JAR.
 */
public class ClassPathViewResourcesResourceHandler extends ResourceHandlerWrapper {

    private static final Log LOGGER = LogFactory.getLog(ClassPathViewResourcesResourceHandler.class);

    private final ResourceHandler wrapped;

    public ClassPathViewResourcesResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }

    @Override
    public ViewResource createViewResource(FacesContext context, String resourceName) {
        final URL url = Thread.currentThread().getContextClassLoader()
                .getResource("META-INF/resources" + resourceName);
        if (url == null) {
            return createViewResourceWithWrappedHandler(context, resourceName);
        }
        LOGGER.info("Load resource " + resourceName + " from " + url);
        return new ViewResource() {

            @Override
            public URL getURL() {
                return url;
            }
        };
    }

    private ViewResource createViewResourceWithWrappedHandler(FacesContext context, String resourceName) {
        ViewResource resource = wrapped.createViewResource(context, resourceName);
        if (resource == null) {
            LOGGER.info("Did not found resource " + resourceName);
            return null;
        }
        LOGGER.info("Load resource " + resourceName + " from wrapped resource handler " + wrapped);
        return resource;
    }
}
