/*******************************************************************************
 * Copyright (c) 2020 University of Stuttgart
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

import java.util.UUID;

import org.planqk.atlas.core.services.ComputingResourceService;
import org.planqk.atlas.web.Constants;
import org.planqk.atlas.web.dtos.ComputingResourceTypeDto;
import org.planqk.atlas.web.linkassembler.QuantumResourceTypeAssembler;
import org.planqk.atlas.web.utils.HateoasUtils;
import org.planqk.atlas.web.utils.ModelMapperUtils;
import org.planqk.atlas.web.utils.RestUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "computing-resource-types")
@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/" + Constants.COMPUTING_RESOURCE_TYPES)
@AllArgsConstructor
public class ComputingResourceTypeController {

    private final QuantumResourceTypeAssembler assembler;
    private final ComputingResourceService service;
    private final PagedResourcesAssembler<ComputingResourceTypeDto> paginationAssembler;

    @Operation(responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ComputingResourceTypeDto>> getComputingResourceType(@PathVariable UUID id) {
        var resourceType = service.findResourceTypeById(id);
        var resourceTypeDto = ModelMapperUtils.convert(resourceType, ComputingResourceTypeDto.class);
        var entityModel = HateoasUtils.generateEntityModel(resourceTypeDto);
        assembler.addLinks(entityModel);
        return ResponseEntity.ok(entityModel);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "404"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComputingResourceType(@PathVariable UUID id) {
        service.deleteComputingResourceType(id);
        return ResponseEntity.ok().build();
    }

    @Operation(responses = {@ApiResponse(responseCode = "200")})
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<ComputingResourceTypeDto>>> getResourceTypes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable p = RestUtils.getPageableFromRequestParams(page, size);
        var types = service.findAllResourceTypes(p);
        var typeDtoes = ModelMapperUtils.convertPage(types, ComputingResourceTypeDto.class);
        var pagedModel = paginationAssembler.toModel(typeDtoes);
        assembler.addLinks(pagedModel);
        return ResponseEntity.ok(pagedModel);
    }
}
