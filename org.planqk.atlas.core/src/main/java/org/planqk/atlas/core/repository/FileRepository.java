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

package org.planqk.atlas.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.planqk.atlas.core.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, UUID> {

    Optional<File> findByFileURL(String fileURL);

    @Query(value = "SELECT * " +
            "FROM file " +
            "INNER JOIN implementation_package_file on file.id = implementation_package_file.file_id " +
            "INNER JOIN knowledge_artifact ka on file.id = ka.id " +
            "WHERE implementation_package_file.implementation_package_id = :implementationPackageId",
           nativeQuery = true)
    Optional<File> findByImplementationPackage_Id(@Param("implementationPackageId") UUID implementationPackageId);
}
