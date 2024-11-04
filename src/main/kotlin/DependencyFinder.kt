import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ClassInfoList
import io.github.classgraph.ScanResult

class DependencyFinder(private val clazz: String, private val jarFiles: List<String>) {

    fun process(): Boolean {
        //open the scanner
        val scanResult = ClassGraph()
            .overrideClasspath(jarFiles)
            .enableClassInfo()
            .enableMethodInfo()
            .enableFieldInfo()
            .enableAnnotationInfo()
            .enableInterClassDependencies()
            .enableExternalClasses()
            .scan()

        val rootClass = scanResult.getClassInfo(clazz)

        val seen = mutableSetOf<String>()
        val missingClasses = mutableSetOf<String>()

        //check case when main class is not part of the Jars
        if (rootClass != null) {
            collectDependencies(rootClass, seen, missingClasses, scanResult)
        } else {
            println("Class not found: $clazz")
            scanResult.close()
            return false
        }

        scanResult.close()

        //check missing dependencies
        return if (missingClasses.isNotEmpty()) {
            println("Failure: Missing dependencies:")
            missingClasses.forEach { println(it) }
            false
        } else {
            println("Success: All dependencies are satisfied.")
            true
        }
    }

    private fun collectDependencies(
        classInfo: ClassInfo,
        seen: MutableSet<String>,
        missingClasses: MutableSet<String>,
        scanResult: ScanResult
    ) {
        val className = classInfo.name
        if (seen.contains(className)) {
            return
        }
        seen.add(className)

        // Use getClassDependencies() to retrieve all dependent classes
        val dependencies: ClassInfoList = classInfo.classDependencies

        // Remove array types, primitives, and standard Java classes
        val filteredDependencies = dependencies
            .map { it.name.replace("\\[]*".toRegex(), "") } // Remove array brackets
            .filterNot {
                it in listOf(
                    "byte", "short", "int", "long", "float",
                    "double", "char", "boolean", "void"
                ) || it.startsWith("java.") || it.startsWith("javax.") || it.startsWith("sun.")
            }

        // Recursively collect dependencies
        for (depClassName in filteredDependencies) {
            if (!seen.contains(depClassName)) {
                val depClassInfo = scanResult.getClassInfo(depClassName)
                if (depClassInfo != null) {
                    if (depClassInfo.isExternalClass) {
                        // Class is external, meaning it's missing from the classpath
                        missingClasses.add(depClassName)
                    } else {
                        collectDependencies(depClassInfo, seen, missingClasses, scanResult)
                    }
                } else {
                    // ClassInfo not found, consider it missing
                    missingClasses.add(depClassName)
                }
            }
        }
    }
}
