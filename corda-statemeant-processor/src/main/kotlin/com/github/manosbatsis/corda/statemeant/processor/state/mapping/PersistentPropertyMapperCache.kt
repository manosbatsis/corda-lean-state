/*
 * Corda Statemeant: Generate Corda Contract and Persistent states
 * from a single interface
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
package com.github.manosbatsis.corda.statemeant.processor.state.mapping

import com.github.manosbatsis.corda.statemeant.annotation.StatemeantProperty
import com.github.manosbatsis.corda.statemeant.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.statemeant.processor.state.MappedProperty
import com.github.manosbatsis.kotlin.utils.ProcessingEnvironmentAware
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import net.corda.core.contracts.Contract
import net.corda.core.contracts.ContractState
import net.corda.core.schemas.PersistentState
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror

class PersistentPropertyMapperCache(
        val delegate: BaseStateMembersStrategy
) : ProcessingEnvironmentAware by delegate {
    companion object {
        // TODO: make extensible by config
        val buildInMappers = listOf(
                ::UniqueIdentifierPersistentPropertyMapper,
                ::AccountPartyPersistentPropertyMapper,
                ::PartyPersistentPropertyMapper,
                ::CordaX500NamePersistentPropertyMapper,
                ::LinearPointerPersistentPropertyMapper,
                ::IdentityPersistentPropertyMapper
        )
    }

    private val mappings: Map<String, PersistentPropertyMapper<*>>

    init {
        mappings = buildInMappers.map { creator ->
            val mapper = creator(delegate)
            mapper.supportedTypes().map { it to mapper }
        }.flatten().toMap()
    }

    /** Converts a [ContractState] field to one or more [PersistentState] fields */
    fun toPersistentProperties(mappedProperty: MappedProperty) =
            getMapper(mappedProperty)?.map(mappedProperty)
                    ?: emptyList()

    fun getMapper(mappedProperty: MappedProperty): PersistentPropertyMapper<*>? {
        return getMapper(mappedProperty.variableElement)
                ?: getMapper(mappedProperty.propertyType)
    }

    fun getMapper(typeName: TypeName): PersistentPropertyMapper<*>? {
        return getMapper(typeName.copy(nullable = false).toString())
    }

    fun getMapper(variableElement: VariableElement): PersistentPropertyMapper<*>? {
        return variableElement.annotationMirrors
                .find {
                    it.annotationType.asTypeElement().asClassName().canonicalName ==
                            StatemeantProperty::class.java.canonicalName
                }
                ?.let {
                    it.findValueAsKClass("mapper")?.java
                            ?.getConstructor(BaseStateMembersStrategy::class.java)
                            ?.newInstance(delegate) as PersistentPropertyMapper<*>
                }
                ?: getMapper(variableElement.asType())
                ?: getMapper(variableElement.asKotlinTypeName())
    }

    fun getMapper(type: TypeMirror): PersistentPropertyMapper<*>? {
        return getMapper(type.asTypeName())
                ?: getMapper(type.asKotlinTypeName())
                ?: getMapper(type.asTypeElement())

    }

    fun getMapper(typeElement: TypeElement): PersistentPropertyMapper<*>? {
        return getMapper(typeElement.asClassName())
    }

    fun getMapper(name: Name): PersistentPropertyMapper<*>? {
        return getMapper("$name")
    }

    fun getMapper(typeHint: String): PersistentPropertyMapper<*>? = mappings[typeHint]
}