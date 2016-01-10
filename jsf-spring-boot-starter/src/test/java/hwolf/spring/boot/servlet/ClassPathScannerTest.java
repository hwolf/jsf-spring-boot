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

import java.util.EventListener;
import java.util.Set;

import javax.faces.bean.ManagedBean;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClassPathScannerTest {

    @Test
    public void scan_scanForAnnatedClass_returnNoEmptySet() {

        // When
        Set<Class<?>> result = new ClassPathScanner(new Class<?>[] { ManagedBean.class },
                new String[] { getClass().getPackage().getName() }).scan();

        // Then
        assertTrue(result.size() == 1);
        assertTrue(result.contains(TestClassWithAnnotation.class));
    }

    @Test
    public void scan_scanForClassImplementsInterface_returnNoEmptySet() {

        // When
        Set<Class<?>> result = new ClassPathScanner(new Class<?>[] { EventListener.class },
                new String[] { getClass().getPackage().getName() }).scan();

        // Then
        assertTrue(result.size() == 1);
        assertTrue(result.contains(TestClassImplementsInterface.class));
    }

    @Test
    public void scan_noTypes_returnEmptySet() {

        // When
        Set<Class<?>> result = new ClassPathScanner(new Class<?>[] {},
                new String[] { getClass().getPackage().getName() }).scan();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void scan_noBasePackage_returnEmptySet() {

        // When
        Set<Class<?>> result = new ClassPathScanner(new Class<?>[] { ManagedBean.class, EventListener.class },
                new String[] {}).scan();

        // Then
        assertTrue(result.isEmpty());
    }
}
