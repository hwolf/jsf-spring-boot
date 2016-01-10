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

package hwolf.spring.boot.servlet;

import java.util.Collection;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;

/**
 * Invokes a {@link ServletContainerInitializer} hopefully similar in the way a JEE Servlet
 * container would do that.
 */
public class ServletContainerInitializerInvoker implements ServletContextInitializer {

    private static final Log LOGGER = LogFactory.getLog(ServletContainerInitializerInvoker.class);

    private final ServletContainerInitializer initializer;
    private final String[] basePackages;

    public ServletContainerInitializerInvoker(ServletContainerInitializer initializer,
            Collection<String> basePackages) {
        this.initializer = initializer;
        this.basePackages = basePackages.toArray(new String[0]);
    }

    @Override
    public void onStartup(ServletContext context) throws ServletException {

        Set<Class<?>> handledTypes = findHandledTypes(
                initializer.getClass().getAnnotation(HandlesTypes.class).value());

        LOGGER.info("Initialize " + initializer.getClass() + " by calling onStartup(...)");
        initializer.onStartup(handledTypes, context);
    }

    private Set<Class<?>> findHandledTypes(Class<?>[] types) {
        return new ClassPathScanner(types, basePackages).scan();
    }
}
