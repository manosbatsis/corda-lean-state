package com.github.manosbatsis.corda.statemeant.processor.state

import com.github.manosbatsis.kotlin.utils.kapt.dto.DtoInputContext
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoMembersStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoNameStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoTypeStrategy
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotatedElementInfo
import com.github.manosbatsis.vaultaire.processor.dto.BaseVaultaireDtoStrategy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import net.corda.core.contracts.LinearState
import javax.lang.model.element.VariableElement
import kotlin.reflect.KFunction1

abstract class BaseStateStrategy<N: DtoNameStrategy, T: DtoTypeStrategy, M: DtoMembersStrategy>(
        annotatedElementInfo: AnnotatedElementInfo,
        dtoNameStrategyConstructor: KFunction1<DtoStrategyLesserComposition, N>,
        dtoTypeStrategyConstructor: KFunction1<DtoStrategyLesserComposition, T>,
        dtoMembersStrategyConstructor: KFunction1<DtoStrategyLesserComposition, M>
) : BaseVaultaireDtoStrategy<N, T, M>(
        annotatedElementInfo = annotatedElementInfo,
        dtoNameStrategyConstructor = dtoNameStrategyConstructor,
        dtoTypeStrategyConstructor = dtoTypeStrategyConstructor,
        dtoMembersStrategyConstructor = dtoMembersStrategyConstructor
){
    override fun dtoSpec(dtoInputContext: DtoInputContext): TypeSpec {
        checkValidAnnotatedelement(annotatedElementInfo)
        return super.dtoSpec(dtoInputContext)
    }


    override fun getFieldsToProcess(): List<VariableElement> {
        val fields = annotatedElementInfo.primaryTargetTypeElementFields.filtered()
        // add linearId if missing
        return if(annotatedElementInfo.primaryTargetTypeElement.isAssignableTo(LinearState::class.java)
                || fields.find { it.simpleName.toString() == "linearId" } != null) fields
        else processingEnvironment.elementUtils.getTypeElement(LinearState::class.java.canonicalName)
                   .accessibleConstructorParameterFields(adaptInterfaceGetters = true)
                   .filter { it.simpleName.toString() == "linearId" } + fields
    }

    fun checkValidAnnotatedelement(annotatedElementInfo: AnnotatedElementInfo) {
        val annotationName = annotatedElementInfo.annotation.annotationType.asTypeElement().qualifiedName
        val annotatedElementName = annotatedElementInfo.primaryTargetTypeElement.qualifiedName.toString()
        check(annotatedElementInfo.primaryTargetTypeElement.isInterface()) {
            "${annotationName} is only allowed on interfaces but $annotatedElementName is a class"
        }
    }
}