import groovy.io.FileType
import java.nio.file.Paths


/**
 * Get modules in a {baseDir}
 *
 * @param baseDir Directory containing environments
 *
 * returns array of full paths to each module found in the environment, ordered
 *         by priority
 */
String[] getEnvironmentTFDirs(String baseDir) {
    // Order to return found modules in, rest will simply
    def separator = "/"
    final def modulePriority = ["network", "common", "secrets", "eks", "services", "flux"]
    def modules = [] as HashSet

    findFiles(glob: baseDir + "/**/*.tf").each {
        if (!it.directory && (it.name == "_backend.tf" || it.name == "_provider.tf")) {
            def converted = it.path.split('/')
            if (converted.length < 2) {
                separator = "\\"
                converted = it.path.split("\\\\")
            }

            if (converted.length > 1) {
                modules.add(converted[-2])
            }
        }
    }

    def ret = []

    modulePriority.each {
        if (modules.contains(it)) {
            ret.add(baseDir + separator + it)
            modules.remove(it)
        }
    }

    def rest = modules.toArray().sort()

    rest.each {
        ret.add(baseDir + separator + it)
    }

    return ret
}

// println(getEnvironmentTFDirs("something/terraform/environments/qa"))
