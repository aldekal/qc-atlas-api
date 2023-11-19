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

import java.util.Collection;
import java.util.UUID;

import org.planqk.atlas.core.model.ComputeResourceProperty;
import org.planqk.atlas.core.model.ConcreteSolution;
import org.planqk.atlas.core.model.File;
import org.planqk.atlas.core.model.Implementation;
import org.planqk.atlas.core.model.ImplementationPackage;
import org.planqk.atlas.core.model.Publication;
import org.planqk.atlas.core.model.Tag;
import org.planqk.atlas.core.services.ComputeResourcePropertyService;
import org.planqk.atlas.core.services.ConcreteSolutionService;
import org.planqk.atlas.core.services.FileService;
import org.planqk.atlas.core.services.ImplementationPackageService;
import org.planqk.atlas.core.services.ImplementationService;
import org.planqk.atlas.core.services.LinkingService;
import org.planqk.atlas.core.services.PublicationService;
import org.planqk.atlas.core.services.SoftwarePlatformService;
import org.planqk.atlas.core.services.TagService;
import org.planqk.atlas.web.Constants;
import org.planqk.atlas.web.dtos.ComputeResourcePropertyDto;
import org.planqk.atlas.web.dtos.ConcreteSolutionDto;
import org.planqk.atlas.web.dtos.DiscussionCommentDto;
import org.planqk.atlas.web.dtos.DiscussionTopicDto;
import org.planqk.atlas.web.dtos.FileDto;
import org.planqk.atlas.web.dtos.ImplementationDto;
import org.planqk.atlas.web.dtos.ImplementationPackageDto;
import org.planqk.atlas.web.dtos.PublicationDto;
import org.planqk.atlas.web.dtos.SoftwarePlatformDto;
import org.planqk.atlas.web.dtos.TagDto;
import org.planqk.atlas.web.utils.ListParameters;
import org.planqk.atlas.web.utils.ListParametersDoc;
import org.planqk.atlas.web.utils.ModelMapperUtils;
import org.planqk.atlas.web.utils.ValidationGroups;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/" + Constants.PATTERNS + "/{patternId}/" + Constants.CONCRETESOLUTIONS)
@AllArgsConstructor
@Slf4j
public class ConcreteSolutionController {

        private final ConcreteSolutionService concreteSolutionService;
        private final FileService fileService;

        @Operation(responses = {
                @ApiResponse(responseCode = "201"),
                @ApiResponse(responseCode = "400"),
                @ApiResponse(responseCode = "404",
                             description = "Not Found. given ID doesn't exist.")
        }, description = "Create a concrete solution of an pattern."
        )
        @PostMapping
        public ResponseEntity<ConcreteSolutionDto> createConcreteSolutionOfPattern(
                @PathVariable UUID patternId,
                @Validated(ValidationGroups.Create.class) @RequestBody ConcreteSolutionDto concreteSolutionDto) {
            final ConcreteSolution concreteSolution = ModelMapperUtils.convert(concreteSolutionDto, ConcreteSolution.class);
            final ConcreteSolution concreteSolutionToSave =
                    concreteSolutionService.create(concreteSolution, patternId);
            return new ResponseEntity<>(ModelMapperUtils.convert(concreteSolutionToSave, ConcreteSolutionDto.class), HttpStatus.CREATED);
        }

        @Operation(responses = {
                @ApiResponse(responseCode = "201"),
                @ApiResponse(responseCode = "400", description = "Bad Request. Invalid request body."),
        }, description = "Uploads and adds a file to a given concrete solution")
        @PostMapping(value = "/{concreteSolutionId}/" + Constants.FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<FileDto> createFileForConcreteSolution(
                @PathVariable UUID concreteSolutionId,
                @RequestParam("file") MultipartFile multipartFile) {
            final File file = concreteSolutionService.addFileToConcreteSolution(concreteSolutionId, multipartFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(ModelMapperUtils.convert(file, FileDto.class));
        }

        @Operation(responses = {
                @ApiResponse(responseCode = "200"),
        }, description = "Retrieve the file of an implementation package")
        @GetMapping("/{concreteSolutionId}/" + Constants.FILE)
        public ResponseEntity<FileDto> getFileOfConcreteSolution(
                @PathVariable UUID concreteSolutionId
        ) {
            final File file =
                    concreteSolutionService.findLinkedFile(concreteSolutionId);
            return ResponseEntity.ok(ModelMapperUtils.convert(file, FileDto.class));
        }

        @Operation(responses = {
                @ApiResponse(responseCode = "204"),
                @ApiResponse(responseCode = "400"),
                @ApiResponse(responseCode = "404", description = "Not Found. Concrete Solution or File with given IDs don't exist")
        }, description = "Delete a file of an Concrete Solution.")
        @DeleteMapping("/{concreteSolutionId}/" + Constants.FILE)
        public ResponseEntity<Void> deleteFileOfConcreteSolution(
                @PathVariable UUID concreteSolutionId) {
            final File file =
                    concreteSolutionService.findLinkedFile(concreteSolutionId);
            if (file == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            final ConcreteSolution concreteSolution = concreteSolutionService.findById(concreteSolutionId);
            concreteSolution.setFile(null);
            fileService.delete(file.getId());
            concreteSolutionService.update(concreteSolution);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
   
}
