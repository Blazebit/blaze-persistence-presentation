/*
 * Copyright 2014 - 2019 Blazebit.
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

package com.blazebit.persistence.examples.spring.data.graphql.view;

import com.blazebit.persistence.examples.spring.data.graphql.model.Cat;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingCorrelatedSimple;

/**
 * @author Christian Beikov
 * @since 1.4.0
 */
@EntityView(Cat.class)
public interface CatReportView {

    @IdMapping
    Long getId();

    String getName();

    @Mapping("COALESCE(AVG(kittens.age), 0)")
    double getAvgKittenAge();

    @MappingCorrelatedSimple(
        correlated = Cat.class,
        correlationBasis = "age",
        correlationKeyAlias = "correlated",
        correlationExpression = "age = correlated",
        correlationResult = "COUNT(DISTINCT id)",
        fetch = FetchStrategy.JOIN
    )
    long getSameAgedKittens();

}
