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


package com.github.manosbatsis.corda.restate.processor.state.contract

import com.github.manosbatsis.corda.restate.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.restate.processor.state.MappedProperty
import com.github.manosbatsis.corda.restate.processor.state.persistent.PersistentStateNameStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoMembersStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.github.manosbatsis.vaultaire.dto.AccountParty
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.AnonymousParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import java.security.PublicKey
import javax.lang.model.element.VariableElement

open class ContractStateMembersStrategy(
        rootDtoStrategy: DtoStrategyLesserComposition
) : DtoMembersStrategy, BaseStateMembersStrategy(
        rootDtoStrategy
) {

    companion object {
        val participantClasses = listOf<Class<*>>(
                Party::class.java,
                AnonymousParty::class.java,
                AbstractParty::class.java,
                AccountParty::class.java,
                PublicKey::class.java
        )
    }

    private val persistentStateClassName: ClassName = PersistentStateNameStrategy(rootDtoStrategy).getClassName()


    val generateMappedObjectFuncSpecBuilder = FunSpec.builder("generateMappedObject")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("schema", MappedSchema::class.java)
            .returns(persistentStateClassName)


    val generateMappedObjectFuncCodeBuilder by lazy {
        CodeBlock.builder().addStatement("return %L(", persistentStateClassName.simpleName)
                .indent().indent().indent()
    }


    override fun toDefaultValueExpression(variableElement: VariableElement): Pair<String, Boolean>? =
            if ("${variableElement.simpleName}" == "linearId") "UniqueIdentifier()" to false
            else super.toDefaultValueExpression(variableElement)


    fun addGenerateMappedObjectStatements(
            typeSpecBuilder: TypeSpec.Builder, baseMappedProperty: MappedProperty, fields: List<VariableElement>
    ): TypeSpec.Builder {

        val upperCommaOrEmpty = if (baseMappedProperty.fieldIndex + 1 < fields.size) "," else ""
        val persistentProperties = persistentPropertyMapperCache.toPersistentProperties(baseMappedProperty)
        persistentProperties.forEachIndexed() { propertyIndex, mappedProperty ->
            val pathSeparator = if(mappedProperty.propertyType.isNullable) "?." else "."
            val maybeToString = if(mappedProperty.asString) "${pathSeparator}toString()" else ""
            val isLast = propertyIndex + 1 == persistentProperties.size
            val commaOrEmpty = if (isLast) upperCommaOrEmpty else ","
            generateMappedObjectFuncCodeBuilder.addStatement(
                    DtoMembersStrategy.Statement("${mappedProperty.propertyName} = ${mappedProperty.mappedObjectInitializer}${maybeToString}$commaOrEmpty")
            )
        }

        return typeSpecBuilder
    }


    override fun addParamAndProperty(
            typeSpecBuilder: TypeSpec.Builder, mappedProperty: MappedProperty, fields: List<VariableElement>
    ): TypeSpec.Builder {
        addGenerateMappedObjectStatements(typeSpecBuilder, mappedProperty, fields)
        dtoConstructorBuilder.addParameter(
                ParameterSpec.builder(mappedProperty.propertyName, mappedProperty.propertyType)
                        .apply { mappedProperty.propertyDefaults?.first?.let { defaultValue(it) } }.build()
        )
        val propertySpecBuilder = rootDtoMembersStrategy.toPropertySpecBuilder(
                mappedProperty.fieldIndex,
                mappedProperty.variableElement,
                mappedProperty.propertyName,
                mappedProperty.propertyType)
        typeSpecBuilder.addProperty(propertySpecBuilder.build())
        return typeSpecBuilder
    }


    override fun toPropertySpecModifiers(
            variableElement: VariableElement, propertyName: String, propertyType: TypeName
    ): Array<KModifier> = arrayOf(KModifier.OVERRIDE, KModifier.PUBLIC)

    override fun finalize(typeSpecBuilder: TypeSpec.Builder) {

        // Add participants if needed
        maybeImplementParticipants(typeSpecBuilder)

        // Complete generateMappedObject function
        typeSpecBuilder.addFunction(generateMappedObjectFuncSpecBuilder
                .addCode(generateMappedObjectFuncCodeBuilder.unindent().unindent().addStatement(")\n").build().toString())
                .build())

        // Add supportedSchemas func and schema objects if needed
        maybeImplementSupportedSchemas(typeSpecBuilder)

        typeSpecBuilder.primaryConstructor(dtoConstructorBuilder.build())


    }


    /**
     * Implement `supportedSchemas` (will also add `Schema` and `SchemaV1` objects) if not explicitly declared
     * within the annotated type
     */
    fun maybeImplementSupportedSchemas(typeSpecBuilder: TypeSpec.Builder) {
        if (!declaresMethod("supportedSchemas", annotatedElementInfo.primaryTargetTypeElement)) {

            // Complete generateMappedObject function
            typeSpecBuilder.addFunction(FunSpec.builder("supportedSchemas")
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(Iterable::class.java.parameterizedBy(MappedSchema::class.java).asKotlinTypeName())
                    .addCode("return listOf(SchemaV1)")
                    .build())

            // Add schema objects
            val schemaObjSpec = TypeSpec.objectBuilder("Schema")
            val schemaV1ObjSpec = TypeSpec.objectBuilder("SchemaV1")
                    .superclass(MappedSchema::class.java)
                    .addSuperclassConstructorParameter("Schema::class.java")
                    .addSuperclassConstructorParameter("1")
                    .addSuperclassConstructorParameter("listOf(%T::class.java)", persistentStateClassName)

            typeSpecBuilder.addType(schemaObjSpec.build())
            typeSpecBuilder.addType(schemaV1ObjSpec.build())
        }
    }

    /**
     * Implement participants field if not explicitly declared
     * within the annotated type
     */
    fun maybeImplementParticipants(typeSpecBuilder: TypeSpec.Builder) {
        if (!declaresMethod("getParticipants", annotatedElementInfo.primaryTargetTypeElement)) {
            val participantTypes = participantClasses.map { it.asTypeName().copy(nullable = false).toString() }
            val participantFields = annotatedElementInfo.primaryTargetTypeElementFields
                    .filter { participantTypes.contains(it.asType().asTypeName().copy(nullable = false).toString()) }

            val participantsSpec = PropertySpec.builder(
                    "participants", List::class.parameterizedBy(AbstractParty::class), KModifier.OVERRIDE)
                    .getter(FunSpec.getterBuilder()
                            .addStatement("return toParticipants(${participantFields.joinToString(",") { it.simpleName }})")
                            .build())
                    .build()
            typeSpecBuilder.addProperty(participantsSpec)
        }
    }
}
