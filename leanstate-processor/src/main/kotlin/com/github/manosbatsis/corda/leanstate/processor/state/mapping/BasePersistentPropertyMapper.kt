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


package com.github.manosbatsis.corda.leanstate.processor.state.mapping

import com.github.manosbatsis.corda.leanstate.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.leanstate.processor.state.MappedProperty
import com.github.manosbatsis.kotlin.utils.ProcessingEnvironmentAware
import com.squareup.kotlinpoet.asTypeName

abstract class BasePersistentPropertyMapper<T>(
        val delegate: BaseStateMembersStrategy
) : PersistentPropertyMapper<T>, ProcessingEnvironmentAware by delegate {

    override fun map(original: MappedProperty): List<MappedProperty> {
        return map(original, delegate.getPersistentMappingModes(original.variableElement))
    }


    fun MappedProperty.asStringifiedMappedProperty(formula: String? = null): MappedProperty{
        return asChildMappedProperty(subPath = emptyList(), asString = true, formula = formula)
    }

    fun MappedProperty.asChildMappedProperty(
            subPath: List<String>,
            childPropertyName: String = "${propertyName}${subPath.map { it.capitalize() }.joinToString("")}",
            asString: Boolean = false,
            formula: String? = null
    ): MappedProperty {

        println("asChildMappedProperty, propertyName: $propertyName, childPropertyName: $childPropertyName")
        var contextFieldElement = variableElement
        val mappedObjectInitializer = StringBuilder(propertyName)
        subPath.forEach { pathStep ->

            val childPropertyDefaults = delegate.toDefaultValueExpression(contextFieldElement)
            val pathSegmentNullable = if (childPropertyDefaults != null)
                propertyType.isNullable || childPropertyDefaults.second
            else propertyType.isNullable
            mappedObjectInitializer.append(if (pathSegmentNullable) "?." else ".").append(pathStep)
            println("asChildMappedProperty, pathStep: $pathStep")
            val contextFieldTypeElement = contextFieldElement.asType().asTypeElement()
            println("asChildMappedProperty, pathStep: $pathStep, contextFieldTypeElement: $contextFieldTypeElement")
            val contextFieldChildren = contextFieldTypeElement.accessibleConstructorParameterFields(true)
            println("asChildMappedProperty, pathStep: $pathStep, contextFieldChildren(${contextFieldChildren.size}): ${contextFieldChildren.joinToString(",") { "${it.simpleName}" }}")
            contextFieldElement = contextFieldChildren.map {
                val match = "${it.simpleName}" == pathStep
                println("asChildMappedProperty, pathStep: $pathStep, field: ${it.simpleName}, match: $match")
                it
            }
                    .find { "${it.simpleName}" == pathStep }
                    ?: error("Could not find field $pathStep in ${contextFieldTypeElement.asKotlinClassName().canonicalName}")
        }
        val childPropertyDefaults = delegate.toDefaultValueExpression(contextFieldElement)
        println("asChildMappedProperty, childPropertyDefaults: $childPropertyDefaults")
        val childPropertyType = delegate.rootDtoMembersStrategy
                .toPropertyTypeName(contextFieldElement)
                .let { origPropType ->

                    // Either the base of inner property may be nulll,
                    // both apply equally to the result mapped prop
                    val nullable = if (childPropertyDefaults != null)
                        propertyType.isNullable || childPropertyDefaults.second
                    else propertyType.isNullable

                    // Force string if needed
                    val propType = if (asString) String::class.java.asTypeName().asKotlinTypeName()
                    else origPropType

                    propType.copy(nullable = nullable)
                }
        println("asChildMappedProperty, childPropertyType: $childPropertyType")
        return MappedProperty(
                propertyName = "$childPropertyName${if (asString) "String" else ""}",
                propertyPathSegments = propertyPathSegments + subPath,
                propertyType = childPropertyType,
                fieldIndex = fieldIndex,
                propertyDefaults = childPropertyDefaults,
                variableElement = contextFieldElement,
                asString = asString,
                mappedObjectInitializer = mappedObjectInitializer.toString(),
                formula = formula
        )
    }
}