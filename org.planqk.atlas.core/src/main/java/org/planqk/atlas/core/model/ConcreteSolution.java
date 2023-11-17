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

package org.planqk.atlas.core.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ConcreteSolution extends HasId {

    private String name;

    private String description;

    private UUID pattern;

    private ConcreteSolutionType concreteSolutionType;

    //TODO if the ConcreteSolution is deleted, the associated File will also be deleted.

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @JoinTable(
            name = "ConcreteSolutionFile",
            joinColumns = @JoinColumn(name = "concrete_solution_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private File file;
}
