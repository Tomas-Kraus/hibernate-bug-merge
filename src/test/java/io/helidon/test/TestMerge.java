/*
 * Copyright (c) 2025 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.helidon.test;

import io.helidon.test.model.Pokemon;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.helidon.test.Data.NEW_POKEMONS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestMerge {

    private static EntityManagerFactory EMF = null;

    public TestMerge() {
    }

    @Test
    public void testMerge() {
        Pokemon pokemon = NEW_POKEMONS.get(100);
        try (EntityManager em = EMF.createEntityManager()) {
            EntityTransaction et = em.getTransaction();
            et.begin();
            try {
                em.merge(pokemon);
                et.commit();
            } catch (Exception e) {
                et.rollback();
                throw e;
            }
            Pokemon fromDb = em.find(Pokemon.class, 100);
            assertThat(fromDb, is(pokemon));
        }
    }

    @BeforeAll
    public static void before() {
        EMF = Persistence.createEntityManagerFactory("test-pu");
        // Initialize data
        try (EntityManager em = EMF.createEntityManager()) {
            Data.init(em);
        }
    }

    @AfterAll
    public static void after() {
        if (EMF != null) {
            EMF.close();
        }
    }

}
