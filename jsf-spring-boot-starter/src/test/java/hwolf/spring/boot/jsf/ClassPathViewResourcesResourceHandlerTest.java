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

import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.mock;

public class ClassPathViewResourcesResourceHandlerTest {

    private static final String NAME_EXISTING_RESOURCE = "/dummy-resource";

    @Test
    public void createViewResource_searchForAnExistingVieeResource_returnViewResource() {

        // Given
        FacesContext context = mock(FacesContext.class);

        ResourceHandler wrapped = mock(ResourceHandler.class);
        ResourceHandler handler = new ClassPathViewResourcesResourceHandler(wrapped);

        // When
        ViewResource resource = handler.createViewResource(context, NAME_EXISTING_RESOURCE);

        // Then
        assertNotNull(resource);
    }

    @Test
    public void createViewResource_searchForNonExistingVieeResource_returnNull() {

        // Given
        FacesContext context = mock(FacesContext.class);

        ResourceHandler wrapped = mock(ResourceHandler.class);
        ResourceHandler handler = new ClassPathViewResourcesResourceHandler(wrapped);

        // When
        ViewResource resource = handler.createViewResource(context, "/non-existing-resource");

        // Then
        assertNull(resource);
    }
}
