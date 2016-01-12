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
        URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/faces" + resourceName);
        if (url != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Load resource " + resourceName + " from " + url);
            }
            return new ViewResourceImpl(url);
        }
        ViewResource resource = wrapped.createViewResource(context, resourceName);
        if (resource != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Load resource " + resourceName + " from wrapped resource handler " + wrapped);
            }
            return resource;
        }
        LOGGER.warn("Did not found resource " + resourceName);
        return null;
    }
}
