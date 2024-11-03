import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import java.io.File

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Usage: <MainClassName> <JarFile1> [JarFile2 ...]")
        return
    }

    val mainClassName = args[0]
    val jarFiles = args.slice(1 until args.size)

    // Check that jar files exist
    for (jar in jarFiles) {
        if (!File(jar).exists()) {
            println("Jar file not found: $jar")
            return
        }
    }

    // Scan the jars and build the dependency graph
    val scanResult = ClassGraph()
        .overrideClasspath(jarFiles)
        .enableClassInfo()
        .enableInterClassDependencies()
        .scan()

    val classInfo = scanResult.getClassInfo(mainClassName)
    if (classInfo == null) {
        println("Main class not found in the provided jars")
        scanResult.close()
        return
    }

    val dependencyClassNames = mutableSetOf<String>()
    collectDependencies(classInfo, dependencyClassNames, scanResult)

    // Check that all dependencies are present
    val missingClasses = mutableSetOf<String>()
    for (className in dependencyClassNames) {
        // Exclude standard Java classes
        if (className.startsWith("java.") || className.startsWith("javax.") || className.startsWith("sun.")) {
            continue
        }
        val ci = scanResult.getClassInfo(className)
        if (ci == null) {
            missingClasses.add(className)
        }
    }

    if (missingClasses.isEmpty()) {
        println("Success: All dependencies are satisfied.")
    } else {
        println("Failure: Missing dependencies:")
        for (missing in missingClasses) {
            println(missing)
        }
    }

    scanResult.close()
}

fun collectDependencies(
    classInfo: ClassInfo,
    dependencyClassNames: MutableSet<String>,
    scanResult: ScanResult
) {
    if (dependencyClassNames.contains(classInfo.name)) {
        return
    }
    dependencyClassNames.add(classInfo.name)

    // Get the classes that this class depends on
    val directDependencies = classInfo.classDependencies
    for (dep in directDependencies) {
        val depClassName = dep.name
        if (!dependencyClassNames.contains(depClassName)) {
            dependencyClassNames.add(depClassName)
            val depClassInfo = scanResult.getClassInfo(depClassName)
            if (depClassInfo != null) {
                collectDependencies(depClassInfo, dependencyClassNames, scanResult)
            }
        }
    }
}

