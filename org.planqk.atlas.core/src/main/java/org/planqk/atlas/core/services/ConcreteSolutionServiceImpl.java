/*******************************************************************************
 * Copyright (c) 2020 the qc-atlas contributors.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
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
 *******************************************************************************/

package org.planqk.atlas.core.services;

import java.util.UUID;
import javax.transaction.Transactional;
import org.planqk.atlas.core.model.ConcreteSolution;
import org.planqk.atlas.core.repository.ConcreteSolutionRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ConcreteSolutionServiceImpl implements ConcreteSolutionService {

    private final ConcreteSolutionRepository concreteSolutionRepository;

    @Override
    @Transactional
    public ConcreteSolution create(@NonNull ConcreteSolution concreteSolution, UUID patternId) {
        concreteSolution.setPattern(patternId);
        // reset file to null because the ModelMapper sometimes instanciates the file object
        concreteSolution.setFile(null);
        final ConcreteSolution persistedconcreteSolution = concreteSolutionRepository.save(concreteSolution);
        return persistedconcreteSolution;
    }

}
