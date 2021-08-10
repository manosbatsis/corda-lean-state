/*
 * Corda LeanState: Generate Corda Contract and Persistent states
 * from a simplified model interface.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */


package com.github.manosbatsis.corda.leanstate.processor.state.persistent


import com.github.manosbatsis.corda.leanstate.processor.state.contract.ContractStateNameStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.SimpleDtoTypeStrategy
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Builder
import net.corda.core.schemas.PersistentState
import javax.persistence.Entity
import javax.persistence.Table

open class PersistentStateTypeStrategy(
        rootDtoStrategy: DtoStrategyLesserComposition
) : SimpleDtoTypeStrategy(rootDtoStrategy) {

    val rootStrategy by lazy { rootDtoStrategy }

    val contractStateClassName = ContractStateNameStrategy(rootDtoStrategy).getClassName()

    override fun addKdoc(typeSpecBuilder: TypeSpec.Builder) {
        typeSpecBuilder.addKdoc("Custom database schema mapping for [%T], generated based on [%T].",
                contractStateClassName,
                rootStrategy.annotatedElementInfo.primaryTargetTypeElement)
    }

    override fun addModifiers(typeSpecBuilder: TypeSpec.Builder) {
        // For non-data class
    }

    override fun addSuperTypes(typeSpecBuilder: TypeSpec.Builder) {
        typeSpecBuilder.superclass(PersistentState::class.java)
    }

    override fun addAnnotations(typeSpecBuilder: Builder) {
        typeSpecBuilder.addAnnotation(Entity::class.java)
        val tableName = rootStrategy.getClassName().simpleName
                .removeSuffix(rootStrategy.getClassNameSuffix())
                .camelToUnderscores()
        typeSpecBuilder.addAnnotation(
                AnnotationSpec.builder(Table::class.java)
                        .addMember("name = %S", tableName)
                        .build())
    }
}
