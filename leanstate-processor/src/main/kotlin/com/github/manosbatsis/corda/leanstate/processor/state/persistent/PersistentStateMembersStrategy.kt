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

import com.github.manosbatsis.corda.leanstate.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.leanstate.processor.state.MappedProperty
import com.github.manosbatsis.corda.leanstate.processor.state.contract.ContractStateNameStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoMembersStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.squareup.kotlinpoet.*
import org.hibernate.annotations.Formula
import javax.lang.model.element.VariableElement
import javax.persistence.Column

open class PersistentStateMembersStrategy(
        rootDtoStrategy: DtoStrategyLesserComposition
) : DtoMembersStrategy, BaseStateMembersStrategy(
        rootDtoStrategy
) {

    private val contractStateClassName: ClassName = ContractStateNameStrategy(rootDtoStrategy).getClassName()

    override fun toDefaultValueExpression(variableElement: VariableElement): Pair<String, Boolean>? =
            if (rootDtoMembersStrategy.toPropertyTypeName(variableElement).isNullable) Pair("null", true) else null

    override fun addParamAndProperty(
            typeSpecBuilder: TypeSpec.Builder, baseMappedProperty: MappedProperty, fields: List<VariableElement>
    ): TypeSpec.Builder {
        persistentPropertyMapperCache.toPersistentProperties(baseMappedProperty).forEach { mappedProperty ->
            val nullable = mappedProperty.propertyType.isNullable
            val propertyType = if (mappedProperty.asString)
                String::class.java.asTypeName().asKotlinTypeName().copy(nullable = nullable)
            else mappedProperty.propertyType

            dtoConstructorBuilder.addParameter(
                    ParameterSpec.builder(mappedProperty.propertyName, propertyType)
                            .apply { mappedProperty.propertyDefaults?.first?.let { defaultValue(it) } }.build()
            )
            val propertySpecBuilder = rootDtoMembersStrategy.toPropertySpecBuilder(
                    mappedProperty.fieldIndex, mappedProperty.variableElement,
                    mappedProperty.propertyName, propertyType)
                    .addKdoc("Enables query criteria for [%T.%L]${if (mappedProperty.asString) " as a [String]." else "."}", contractStateClassName, mappedProperty.propertyPathSegments.joinToString("."))
                    .copyOrAddColumnAnnotation(mappedProperty)
            typeSpecBuilder.addProperty(propertySpecBuilder.build())
        }

        return typeSpecBuilder
    }

    /** Copy @Column from source if it exists, create one otherwise */
    private fun PropertySpec.Builder.copyOrAddColumnAnnotation(mappedProperty: MappedProperty): PropertySpec.Builder {
        val annotationSpec = mappedProperty.variableElement.annotationMirrors
                .find {
                    it.annotationType.asTypeElement().asClassName().canonicalName ==
                            Column::class.java.canonicalName
                }
                ?.let { AnnotationSpec.get(it) }
                ?: mappedProperty.formula?.let{
                    AnnotationSpec.builder(Formula::class)
                            .addMember("value = %S", it)
                            .build()
                }
                ?: AnnotationSpec.builder(Column::class)
                        .addMember("name = %S", mappedProperty.propertyName.camelToUnderscores())
                        .also {
                            // Mark non-nullables explicitly
                            if (!mappedProperty.propertyType.isNullable)
                                it.addMember("nullable = %L", mappedProperty.propertyType.isNullable)
                        }
                        .build()
        return addAnnotation(annotationSpec)
    }


    override fun toPropertySpecModifiers(
            variableElement: VariableElement, propertyName: String, propertyType: TypeName
    ): Array<KModifier> = arrayOf(KModifier.PUBLIC)

    override fun finalize(typeSpecBuilder: TypeSpec.Builder) {
        typeSpecBuilder.primaryConstructor(dtoConstructorBuilder.build())
    }
}
