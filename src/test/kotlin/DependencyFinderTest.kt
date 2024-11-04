import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DependencyFinderTest {

    private val basePath = "" //TODO specify the base path to JARs

    @Test
    fun `test with missing moduleB for ClassB`() {
        val dependencyFinder = DependencyFinder(
            clazz = "com.jetbrains.internship2024.ClassB",
            jarFiles = listOf("${basePath}ModuleB-1.0.jar")
        )
        assertFalse(dependencyFinder.process(), "Expected false because ModuleA is missing.")
    }

    @Test
    fun `test with both ModuleA and ModuleB for ClassB`() {
        val dependencyFinder = DependencyFinder(
            clazz = "com.jetbrains.internship2024.ClassB",
            jarFiles = listOf("${basePath}ModuleA-1.0.jar", "${basePath}ModuleB-1.0.jar")
        )
        assertTrue(dependencyFinder.process(), "Expected true because both ModuleA and ModuleB are present.")
    }

    @Test
    fun `test with only ModuleA for ClassA`() {
        val dependencyFinder = DependencyFinder(
            clazz = "com.jetbrains.internship2024.ClassA",
            jarFiles = listOf("${basePath}ModuleA-1.0.jar")
        )
        assertTrue(dependencyFinder.process(), "Expected true because ModuleA is sufficient for ClassA.")
    }

    @Test
    fun `test with missing commons-io for SomeAnotherClass`() {
        val dependencyFinder = DependencyFinder(
            clazz = "com.jetbrains.internship2024.SomeAnotherClass",
            jarFiles = listOf("${basePath}ModuleA-1.0.jar")
        )
        assertFalse(dependencyFinder.process(), "Expected false because commons-io is missing.")
    }

    @Test
    fun `test with commons-io for SomeAnotherClass`() {
        val dependencyFinder = DependencyFinder(
            clazz = "com.jetbrains.internship2024.SomeAnotherClass",
            jarFiles = listOf("${basePath}ModuleA-1.0.jar", "${basePath}commons-io-2.16.1.jar")
        )
        assertTrue(dependencyFinder.process(), "Expected true because both ModuleA and commons-io are present.")
    }

    @Test
    fun `test with only ModuleB for ClassB1`() {
        val dependencyFinder = DependencyFinder(
            clazz = "com.jetbrains.internship2024.ClassB1",
            jarFiles = listOf("${basePath}ModuleB-1.0.jar")
        )
        assertTrue(dependencyFinder.process(), "Expected true because ModuleB is sufficient for ClassB1.")
    }
}
