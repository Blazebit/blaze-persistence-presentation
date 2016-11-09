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

import javax.json.JsonObject;
import java.io.Serializable;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
public class KeysetInfo {

    private int lastPageOffset;
    private int lastPageSize;
    private Serializable[] firstKey;
    private Serializable[] lastKey;

    public int getLastPageOffset() {
        return lastPageOffset;
    }

    public void setLastPageOffset(int lastPageOffset) {
        this.lastPageOffset = lastPageOffset;
    }

    public int getLastPageSize() {
        return lastPageSize;
    }

    public void setLastPageSize(int lastPageSize) {
        this.lastPageSize = lastPageSize;
    }

    public Serializable[] getFirstKey() {
        return firstKey;
    }

    public void setFirstKey(Serializable[] firstKey) {
        this.firstKey = firstKey;
    }

    public Serializable[] getLastKey() {
        return lastKey;
    }

    public void setLastKey(Serializable[] lastKey) {
        this.lastKey = lastKey;
    }
}
