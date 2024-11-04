import io.github.classgraph.ClassGraph
import java.io.File


fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Usage: <MainClassName> <Path to JarFile1> <Path to JarFile2> ...")
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
    val df = DependencyFinder(mainClassName, jarFiles)
    println(df.process())

}