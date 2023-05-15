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

package com.blazebit.presentation.demo.startup;

import com.blazebit.persistence.examples.showcase.base.model.Cat;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * @author Moritz Becker (moritz.becker@gmx.at)
 * @since 1.2
 */
@Singleton
@Startup
public class TestDataGenerator {

    @Inject
    private EntityManager em;

    @PostConstruct
    public void generateTestData() {
        Cat moac = new Cat("Mother of all cats");
        em.persist(moac);

        Cat foac = new Cat("Father of all cats");
        em.persist(foac);

        Cat gen1_willi = new Cat("Willi - Generation 1");
        gen1_willi.setMother(moac);
        gen1_willi.setFather(foac);
        em.persist(gen1_willi);

        Cat gen1_susan = new Cat("Susan - Generation 1");
        gen1_susan.setMother(moac);
        gen1_susan.setFather(foac);
        em.persist(gen1_susan);

        Cat gen1_trixy = new Cat("Trixy - Generation 1");
        gen1_trixy.setMother(moac);
        gen1_trixy.setFather(foac);
        em.persist(gen1_trixy);

        Cat gen2_michael = new Cat("Michael - Generation 2");
        gen2_michael.setMother(gen1_trixy);
        gen2_michael.setFather(gen1_willi);
        em.persist(gen2_michael);

        Cat gen2_george = new Cat("George - Generation 2");
        gen2_george.setMother(gen1_susan);
        gen2_george.setFather(gen1_willi);
        em.persist(gen2_george);

        Cat gen2_daisy = new Cat("Daisy - Generation 2");
        gen2_daisy.setMother(gen1_susan);
        gen2_daisy.setFather(gen1_willi);
        em.persist(gen2_daisy);

        Cat gen3_georgejr = new Cat("George Jr. - Generation 3");
        gen3_georgejr.setMother(gen2_daisy);
        gen3_georgejr.setFather(gen2_michael);
        em.persist(gen3_georgejr);

        Cat gen3_hillary = new Cat("Hillary - Generation 3");
        gen3_hillary.setMother(gen2_daisy);
        gen3_hillary.setFather(gen2_george);
        em.persist(gen3_hillary);

    }

}
