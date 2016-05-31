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

package capital.scalable.example.items;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;
import org.javamoney.moneta.Money;

/**
 * Java object for a single JSON item.
 *
 * @author Florian Benz
 */
@AllArgsConstructor
class ItemResponse {
    /**
     * Unique ID. This text comes directly from JavaDoc.
     */
    @NotBlank
    private String id;

    @JsonIgnore
    @NotBlank
    private String desc;

    /**
     * Item attributes.
     */
    private Attributes attributes;

    /**
     * Child items.
     */
    private List<ItemResponse> children;

    /**
     * Various attributes about the item.
     */
    @Value
    static class Attributes {
        /**
         * Textual attribute.
         */
        private String text;
        /**
         * Integer attribute.
         */
        private Integer number;
        /**
         * Boolean attribute.
         */
        private Boolean bool;
        /**
         * Decimal attribute.
         */
        private BigDecimal decimal;
        /**
         * Amount attribute.
         */
        private Money amount;
    }

    /**
     * Some information about the item.
     */
    public String getDescription(){
        return desc;
    }
}