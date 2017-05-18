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

package com.blazebit.presentation.demo.rest.model;

import com.blazebit.persistence.Keyset;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
public class PaginatedResult<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private URI nextPageWithOffset;
    private URI previousPageWithOffset;
    private URI nextPageWithKeyset;
    private URI previousPageWithKeyset;
    private int numPages;
    private int currentPage;
    private List<T> results;
    private KeysetInfo keysetInfo;

    private PaginatedResult() { }

    public URI getNextPageWithOffset() {
        return nextPageWithOffset;
    }

    public void setNextPageWithOffset(URI nextPageWithOffset) {
        this.nextPageWithOffset = nextPageWithOffset;
    }

    public URI getPreviousPageWithOffset() {
        return previousPageWithOffset;
    }

    public void setPreviousPageWithOffset(URI previousPageWithOffset) {
        this.previousPageWithOffset = previousPageWithOffset;
    }

    public URI getNextPageWithKeyset() {
        return nextPageWithKeyset;
    }

    public void setNextPageWithKeyset(URI nextPageWithKeyset) {
        this.nextPageWithKeyset = nextPageWithKeyset;
    }

    public URI getPreviousPageWithKeyset() {
        return previousPageWithKeyset;
    }

    public void setPreviousPageWithKeyset(URI previousPageWithKeyset) {
        this.previousPageWithKeyset = previousPageWithKeyset;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public KeysetInfo getKeysetInfo() {
        return keysetInfo;
    }

    public void setKeysetInfo(KeysetInfo keysetInfo) {
        this.keysetInfo = keysetInfo;
    }

    private static UriBuilder getPageRelativeUriBuilder(UriInfo uriInfo) {
        return uriInfo.getAbsolutePathBuilder()
                .queryParam("pageSize", uriInfo.getQueryParameters().getFirst("pageSize"));
    }

    public static <T> PaginatedResult<T> from(PagedList<T> results, UriInfo uriInfo, Function<Keyset, String> keysetConverter) {
        PaginatedResult<T> paginatedResult = new PaginatedResult<T>();
        paginatedResult.setResults(results);
        paginatedResult.setCurrentPage(results.getPage());
        paginatedResult.setNumPages(results.getTotalPages());

        final UriBuilder basePageWithKeysetUriBuilder;
        if (results.getKeysetPage() == null) {
            basePageWithKeysetUriBuilder = null;
        } else {
            final KeysetPage keysetPage = results.getKeysetPage();
            final KeysetInfo keysetInfo = new KeysetInfo();
            keysetInfo.setLastPageOffset(keysetPage.getFirstResult());
            keysetInfo.setLastPageSize(keysetPage.getMaxResults());
            keysetInfo.setFirstKey(keysetPage.getLowest().getTuple());
            keysetInfo.setLastKey(keysetPage.getHighest().getTuple());

            paginatedResult.setKeysetInfo(keysetInfo);

            final String firstKeyJson, lastKeyJson;
            try {
                firstKeyJson = objectMapper.writeValueAsString(keysetInfo.getFirstKey());
                lastKeyJson = objectMapper.writeValueAsString(keysetInfo.getLastKey());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            basePageWithKeysetUriBuilder = getPageRelativeUriBuilder(uriInfo)
                    .queryParam("firstKey", keysetConverter.apply(keysetPage.getLowest()))
                    .queryParam("lastKey", keysetConverter.apply(keysetPage.getHighest()))
                    .queryParam("lastPageOffset", keysetInfo.getLastPageOffset())
                    .queryParam("lastPageSize", keysetInfo.getLastPageSize());
        }

        if (results.getPage() < results.getTotalPages()) {
            URI nextPageUri = getPageRelativeUriBuilder(uriInfo)
                    .queryParam("page", results.getPage() + 1)
                    .build();
            paginatedResult.setNextPageWithOffset(nextPageUri);

            if (basePageWithKeysetUriBuilder != null) {
                URI nextPageWithKeysetUri = basePageWithKeysetUriBuilder.clone()
                        .queryParam("page", results.getPage() + 1)
                        .build();
                paginatedResult.setNextPageWithKeyset(nextPageWithKeysetUri);
            }
        }
        if (results.getPage() > 1) {
            URI previousPageUri = getPageRelativeUriBuilder(uriInfo)
                    .queryParam("page", results.getPage() - 1)
                    .build();
            paginatedResult.setPreviousPageWithOffset(previousPageUri);

            if (basePageWithKeysetUriBuilder != null) {
                URI previousPageWithKeysetUri = basePageWithKeysetUriBuilder.clone()
                        .queryParam("page", results.getPage() - 1)
                        .build();
                paginatedResult.setPreviousPageWithKeyset(previousPageWithKeysetUri);
            }
        }

        return paginatedResult;
    }
}
