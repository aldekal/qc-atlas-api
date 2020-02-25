/*
 *  /*******************************************************************************
 *  * Copyright (c) 2020 University of Stuttgart
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License. You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License
 *  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  * or implied. See the License for the specific language governing permissions and limitations under
 *  * the License.
 *  ******************************************************************************
 */

package org.planqk.quality.repository;

import java.util.List;

import org.planqk.quality.model.Algorithm;
import org.planqk.quality.model.Implementation;
import org.planqk.quality.model.Sdk;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access {@link Implementation}s available in the data base with different queries.
 */
public interface ImplementationRepository extends CrudRepository<Implementation, Long> {

    Implementation findById(long id);

    List<Implementation> findByImplementedAlgorithm(Algorithm implementedAlgorithm);

    List<Implementation> findBySdk(Sdk sdk);
}
