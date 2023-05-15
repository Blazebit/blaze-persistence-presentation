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
import com.blazebit.persistence.DefaultKeyset;
import com.blazebit.persistence.DefaultKeysetPage;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.examples.showcase.base.model.Cat;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.presentation.demo.rest.model.PaginatedResult;
import com.blazebit.presentation.demo.view.CatView;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Path("cats")
public class CatsResource {

    @Inject
    private EntityManager em;

    @Inject
    private EntityViewManager evm;

    @Inject
    private CriteriaBuilderFactory cbf;

    @Inject
    private Instance<CatSubResource> catSubResourceFactory;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PaginatedResult<CatView> getCats(@QueryParam("page") @DefaultValue("1") int page,
                                            @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                                            @QueryParam("firstKey") Integer firstKey,
                                            @QueryParam("lastKey") Integer lastKey,
                                            @QueryParam("lastPageOffset") Integer lastPageOffset,
                                            @QueryParam("lastPageSize") Integer lastPageSize) {
        CriteriaBuilder<Cat> cb = cbf.create(em, Cat.class).orderByAsc("id");

        final EntityViewSetting<CatView, PaginatedCriteriaBuilder<CatView>> catSetting;
        if (firstKey == null || lastKey == null || lastPageOffset == null || lastPageSize == null) {
            catSetting = EntityViewSetting.create(CatView.class, (page - 1) * pageSize, pageSize);
        } else {
            KeysetPage keysetPage = new DefaultKeysetPage(lastPageOffset, lastPageSize, new DefaultKeyset(new Serializable[]{ firstKey }), new DefaultKeyset(new Serializable[]{ lastKey }));
            catSetting = EntityViewSetting.create(CatView.class, (page - 1) * pageSize, pageSize).withKeysetPage(keysetPage);
        }

        PaginatedCriteriaBuilder<CatView> pcb = evm.applySetting(catSetting, cb)
                .withKeysetExtraction(true);

        String countQueryString = pcb.getPageCountQueryString();
        String idQueryString = pcb.getPageIdQueryString();
        String objectQueryString = pcb.getQueryString();

        PagedList<CatView> result = pcb.getResultList();

        return PaginatedResult.from(result, uriInfo, keyset -> keyset.getTuple()[0].toString());
    }

    @Path("{catId}")
    public CatSubResource getCatResource(@PathParam("catId") Integer catId) {
        CatSubResource catSubResource = catSubResourceFactory.get();
        catSubResource.setCatId(catId);
        return catSubResource;
    }

}
