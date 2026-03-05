package umc.th.juinjang.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import jakarta.persistence.Entity
import org.junit.jupiter.api.DisplayName

/**
 *
 * Kotlin 컴파일 결과물(build/classes/kotlin/)만 스캔하여
 * 기존 Java 코드(build/classes/java/)의 위반을 제외한다.
 */
class ExcludeJavaClasses : ImportOption {
    override fun includes(location: Location): Boolean = !location.contains("classes/java/")
}

@AnalyzeClasses(
    packages = ["umc.th.juinjang"],
    importOptions = [ImportOption.DoNotIncludeTests::class, ExcludeJavaClasses::class],
)
class LayerDependencyTest {

    // ── 규칙 1: 계층 의존성 ──

    @ArchTest
    @DisplayName("계층 간 의존성 규칙을 준수해야 한다")
    fun layerDependencyRule(classes: JavaClasses) {
        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Interfaces").definedBy("..interfaces..")
            .optionalLayer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .optionalLayer("Infrastructure").definedBy("..infrastructure..")
            .optionalLayer("Support").definedBy("..support..")
            .whereLayer("Interfaces").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Interfaces", "Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Interfaces", "Application", "Infrastructure")
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
            .whereLayer("Support").mayOnlyBeAccessedByLayers(
                "Interfaces",
                "Application",
                "Domain",
                "Infrastructure",
            )
            .check(classes)
    }

    // ── 규칙 2: Entity 위치 ──

    @ArchTest
    @DisplayName("@Entity 클래스는 domain 패키지에만 위치해야 한다")
    fun entityShouldResideInDomainPackage(classes: JavaClasses) {
        ArchRuleDefinition.classes()
            .that().areAnnotatedWith(Entity::class.java)
            .should().resideInAPackage("..domain..")
            .because("@Entity 클래스는 반드시 domain 패키지에 위치해야 합니다")
            .check(classes)
    }

    // ── 규칙 4: Interfaces 계층 규칙 ──

    @ArchTest
    @DisplayName("Interfaces 계층은 Repository를 직접 의존하지 않아야 한다")
    fun interfacesShouldNotDependOnRepository(classes: JavaClasses) {
        ArchRuleDefinition.noClasses()
            .that().resideInAPackage("..interfaces..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..repository..", "..infrastructure..")
            .because("Interfaces 계층은 application/domain 계층을 거쳐 데이터에 접근해야 합니다")
            .check(classes)
    }
}
