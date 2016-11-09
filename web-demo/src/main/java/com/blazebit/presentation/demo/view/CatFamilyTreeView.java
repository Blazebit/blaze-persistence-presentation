/*
 * Copyright 2014 Blazebit.
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

package com.blazebit.presentation.demo.view;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.examples.base.model.Cat;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.MappingSubquery;
import com.blazebit.persistence.view.SubqueryProvider;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
@EntityView(Cat.class)
public interface CatFamilyTreeView extends CatView{

    CatView getMother();

    CatView getFather();

    @MappingSubquery(ChildCountSubqueryProvider.class)
    Long getChildCount();

    class ChildCountSubqueryProvider implements SubqueryProvider {

        @Override
        public <T> T createSubquery(SubqueryInitiator<T> subqueryInitiator) {
            return subqueryInitiator.from(Cat.class)
                    .select("COUNT(*)")
                    .whereOr()
                        .where("father.id").eqExpression("OUTER(id)")
                        .where("mother.id").eqExpression("OUTER(id)")
                    .endOr()
                    .end();
        }
    }

}
