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

package com.blazebit.presentation.demo.rest;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.examples.base.model.Cat;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.presentation.demo.cte.CatHierarchyCTE;
import com.blazebit.presentation.demo.view.CatFamilyTreeView;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CatSubResource {

    @Inject
    private EntityManager em;

    @Inject
    private CriteriaBuilderFactory cbf;

    @Inject
    private EntityViewManager evm;

    private Integer catId;

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    @GET
    @Path("family-tree")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CatFamilyTreeView> getFamilyTree() {
        final EntityViewSetting<CatFamilyTreeView, CriteriaBuilder<CatFamilyTreeView>> catSetting = EntityViewSetting.create(CatFamilyTreeView.class);
        return getCatHierarchy(catId, catSetting);
    }

    public <T> List<T> getCatHierarchy(Integer catId, EntityViewSetting<T, CriteriaBuilder<T>> setting) {
        CriteriaBuilder<Tuple> cb = cbf.create(em, Tuple.class)
                .withRecursive(CatHierarchyCTE.class)
                    .from(Cat.class)
                    .bind("id").select("id")
                    .bind("motherId").select("mother.id")
                    .bind("fatherId").select("father.id")
                    .bind("generation").select("0")
                    .where("id").eqExpression(catId.toString())
                .unionAll()
                    .from(Cat.class, "cat")
                    .from(CatHierarchyCTE.class, "cte")
                    .bind("id").select("cat.id")
                    .bind("motherId").select("cat.mother.id")
                    .bind("fatherId").select("cat.father.id")
                    .bind("generation").select("cte.generation + 1")
                    .whereOr()
                        .where("cat.id").eqExpression("cte.motherId")
                        .where("cat.id").eqExpression("cte.fatherId")
                    .endOr()
                .end()
                .from(Cat.class, "cat")
                .from(CatHierarchyCTE.class, "cte")
                .where("cte.id").eqExpression("cat.id")
                .groupBy("cat.id")
                .orderByAsc("cte.generation");

        return evm.applySetting(setting, cb, "cat").getResultList();
    }
}
