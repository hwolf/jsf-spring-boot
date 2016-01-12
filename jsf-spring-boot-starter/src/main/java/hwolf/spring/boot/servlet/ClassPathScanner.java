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

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

final class ClassPathScanner {

    private static final Log LOGGER = LogFactory.getLog(ClassPathScanner.class);

    private final boolean debug = LOGGER.isDebugEnabled();

    private final Class<?>[] types;
    private final String[] basePackages;

    private Set<Class<?>> result;

    ClassPathScanner(Class<?>[] types, String[] basePackages) {
        this.types = types;
        this.basePackages = basePackages;
    }

    Set<Class<?>> scan() {
        result = new HashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = buildClassPathScanner();
        for (String basePackage : basePackages) {
            if (debug) {
                LOGGER.debug("Scan package " + basePackage + " for JSF classes");
            }
            scanPackage(scanner, basePackage);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private ClassPathScanningCandidateComponentProvider buildClassPathScanner() {

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                false);
        for (Class<?> type : types) {
            if (type.isAnnotation()) {
                if (debug) {
                    LOGGER.debug("Search for classes annotated with " + type);
                }
                scanner.addIncludeFilter(new AnnotationTypeFilter((Class<Annotation>) type));
            } else {
                if (debug) {
                    LOGGER.debug("Search for classes assignable to " + type);
                }
                scanner.addIncludeFilter(new AssignableTypeFilter(type));
            }
        }
        return scanner;
    }

    private void scanPackage(ClassPathScanningCandidateComponentProvider scanner, String basePackage) {

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            try {
                if (debug) {
                    LOGGER.debug("Found class " + bd.getBeanClassName());
                }
                result.add(Class.forName(bd.getBeanClassName()));
            } catch (ClassNotFoundException ex) {
                LOGGER.warn("Cannot load class " + bd.getBeanClassName(), ex);
            }
        }
    }
}
