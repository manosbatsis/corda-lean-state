/*
 * Corda Restate: Generate Corda Contract and Persistent states
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


package com.github.manosbatsis.corda.restate.processor.state

import com.github.manosbatsis.corda.restate.annotation.PropertyMappingMode
import com.github.manosbatsis.corda.restate.annotation.RestateProperty
import com.github.manosbatsis.corda.restate.annotation.RestateModel
import com.github.manosbatsis.corda.restate.processor.state.mapping.PersistentPropertyMapperCache
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoMembersStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.SimpleDtoMembersStrategy
import com.squareup.kotlinpoet.*
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter

abstract class BaseStateMembersStrategy(
        rootDtoStrategy: DtoStrategyLesserComposition
) : DtoMembersStrategy, SimpleDtoMembersStrategy(
        rootDtoStrategy
) {

    protected val persistentPropertyMapperCache: PersistentPropertyMapperCache by lazy {
        PersistentPropertyMapperCache(this)
    }

    abstract fun addParamAndProperty(typeSpecBuilder: TypeSpec.Builder, mappedProperty: MappedProperty, fields: List<VariableElement>): TypeSpec.Builder

    fun getPersistentMappingModes(variableElement: VariableElement): List<PropertyMappingMode> {
        val propModes = variableElement.getAnnotation(RestateProperty::class.java)
                ?.mappingModes?.toList()
        return if(propModes != null && propModes.isNotEmpty()) propModes
        else annotatedElementInfo.primaryTargetTypeElement
                        .getAnnotation(RestateModel::class.java)!!.mappingModes.toList()

    }

    override fun defaultMutable(): Boolean = false

    override fun toPropertyTypeName(variableElement: VariableElement): TypeName =
            variableElement.asKotlinTypeName()

    override fun addProperty(
            originalProperty: VariableElement,
            fieldIndex: Int,
            typeSpecBuilder: TypeSpec.Builder,
            fields: List<VariableElement>
    ): Pair<String, TypeName> {
        val propertyName = rootDtoMembersStrategy.toPropertyName(originalProperty)
        val propertyDefaults = rootDtoMembersStrategy.toDefaultValueExpression(originalProperty)
        val propertyType = rootDtoMembersStrategy
                .toPropertyTypeName(originalProperty)
                .let { propType ->
                    if (propertyDefaults != null) propType.copy(nullable = propertyDefaults.second)
                    else propType
                }

        addParamAndProperty(typeSpecBuilder, MappedProperty(
                propertyName = propertyName,
                propertyType = propertyType,
                propertyDefaults = propertyDefaults,
                fieldIndex = fieldIndex,
                variableElement = originalProperty),
                fields)
        // TODO
        //rootDtoMembersStrategy.addPropertyAnnotations(propertySpecBuilder, originalProperty)
        //typeSpecBuilder.addProperty(propertySpecBuilder.build())
        return Pair(propertyName, propertyType)
    }

    override fun processFields(typeSpecBuilder: TypeSpec.Builder, fields: List<VariableElement>) {
        super.processFields(typeSpecBuilder, fields)
    }

    override fun toPropertySpecBuilder(
            fieldIndex: Int, variableElement: VariableElement, propertyName: String, propertyType: TypeName
    ): PropertySpec.Builder = PropertySpec.builder(propertyName, propertyType)
            .mutable(defaultMutable())
            .addModifiers(*toPropertySpecModifiers(variableElement, propertyName, propertyType))
            .initializer(propertyName)

    override fun findDefaultValueAnnotationValue(
            variableElement: VariableElement
    ): Pair<String, Boolean>? = variableElement.getAnnotation(RestateProperty::class.java)
            ?.initializer
            ?.let { it to toPropertyTypeName(variableElement).isNullable }

    abstract fun toPropertySpecModifiers(
            variableElement: VariableElement, propertyName: String, propertyType: TypeName
    ): Array<KModifier>

    fun declaresMethod(methodName: String, typeElement: TypeElement): Boolean {
        val allMembers = processingEnvironment.elementUtils.getAllMembers(typeElement)
        return ElementFilter.methodsIn(allMembers).find {
            "${it.simpleName}" == methodName && "${it.enclosingElement.simpleName}" == "${typeElement.simpleName}"
        } != null
    }


}
