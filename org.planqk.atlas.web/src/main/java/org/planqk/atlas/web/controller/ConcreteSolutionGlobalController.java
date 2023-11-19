/*******************************************************************************
 * Copyright (c) 2020-2021 the qc-atlas contributors.
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

package org.planqk.atlas.web.controller;

import org.planqk.atlas.core.services.ConcreteSolutionService;
import org.planqk.atlas.web.Constants;
import org.planqk.atlas.web.dtos.ConcreteSolutionDto;
import org.planqk.atlas.web.utils.ListParameters;
import org.planqk.atlas.web.utils.ListParametersDoc;
import org.planqk.atlas.web.utils.ModelMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to access and manipulate implementations of quantum algorithms.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = Constants.PATTERNS)
@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/" + Constants.CONCRETESOLUTIONS)
@AllArgsConstructor
@Slf4j
public class ConcreteSolutionGlobalController {

        private final ConcreteSolutionService concreteSolutionService;

        @Operation(responses = {
            @ApiResponse(responseCode = "200"),
        }, description = "Retrieve all concrete solutions unaffected by its implemented pattern")
        @ListParametersDoc
        @GetMapping
        public ResponseEntity<Page<ConcreteSolutionDto>> getConcreteSolutions(@Parameter(hidden = true) ListParameters listParameters) {
                final var concreteSolutions = concreteSolutionService.findAll(listParameters.getPageable());
                return ResponseEntity.ok(ModelMapperUtils.convertPage(concreteSolutions, ConcreteSolutionDto.class));
        }
   
}
