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

package capital.scalable.restdocs.snippet;

import static capital.scalable.restdocs.OperationAttributeHelper.getHandlerMethod;
import static capital.scalable.restdocs.OperationAttributeHelper.getTemplateFormat;
import static capital.scalable.restdocs.constraints.ConstraintReader.CONSTRAINTS_ATTRIBUTE;
import static capital.scalable.restdocs.constraints.ConstraintReader.OPTIONAL_ATTRIBUTE;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.web.method.HandlerMethod;

public abstract class StandardTableSnippet extends TemplatedSnippet {

    private static final String LINE_BREAK_ASCIIDOC = " +\n";
    private static final String LINE_BREAK_MARKDOWN = "<br>";

    protected StandardTableSnippet(String snippetName, Map<String, Object> attributes) {
        super(snippetName, attributes);
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        HandlerMethod handlerMethod = getHandlerMethod(operation);

        Collection<FieldDescriptor> fieldDescriptors = emptyList();
        if (handlerMethod != null) {
            fieldDescriptors = createFieldDescriptors(operation, handlerMethod);
        }

        String lineBreak = determineLineBreak(operation);

        return createModel(handlerMethod, fieldDescriptors, lineBreak);
    }

    protected abstract Collection<FieldDescriptor> createFieldDescriptors(Operation operation,
            HandlerMethod handlerMethod);

    protected void enrichModel(Map<String, Object> model, HandlerMethod handlerMethod) {
        // can be used to add additional fields
    }

    private Map<String, Object> createModel(HandlerMethod handlerMethod,
            Collection<FieldDescriptor> fieldDescriptors, String lineBreak) {
        Map<String, Object> model = new HashMap<>();
        enrichModel(model, handlerMethod);

        List<Map<String, Object>> fields = new ArrayList<>();
        model.put("content", fields);
        for (FieldDescriptor descriptor : fieldDescriptors) {
            fields.add(createModelForDescriptor(descriptor, lineBreak));
        }
        model.put("hasContent", !fieldDescriptors.isEmpty());
        model.put("noContent", fieldDescriptors.isEmpty());
        return model;
    }

    protected Map<String, Object> createModelForDescriptor(FieldDescriptor descriptor,
            String lineBreak) {
        String path = descriptor.getPath();
        String type = stringOrEmpty(descriptor.getType());
        String description = stringOrEmpty(descriptor.getDescription());

        List<String> optionalMessages = (List<String>) descriptor.getAttributes().get(
                OPTIONAL_ATTRIBUTE);
        String optional = "" + join(optionalMessages, lineBreak);

        List<String> constraints = (List<String>) descriptor.getAttributes().get(
                CONSTRAINTS_ATTRIBUTE);
        if (constraints != null && !constraints.isEmpty()) {
            String constraintMessages = join(constraints, lineBreak);
            if (isNotBlank(description)) {
                description += lineBreak;
            }
            description += constraintMessages;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("path", path);
        model.put("type", type);
        model.put("optional", optional);
        model.put("description", description);
        return model;
    }

    private String stringOrEmpty(Object value) {
        if (value != null) {
            return value.toString();
        } else {
            return "";
        }
    }

    private String determineLineBreak(Operation operation) {
        return getTemplateFormat(operation).getId().equals(TemplateFormats.asciidoctor().getId())
                ? LINE_BREAK_ASCIIDOC : LINE_BREAK_MARKDOWN;
    }
}
