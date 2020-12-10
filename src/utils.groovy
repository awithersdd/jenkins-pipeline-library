import groovy.io.FileType
import java.nio.file.Paths


/**
 * Get modules in a {baseDir}/{envName}
 *
 * @param baseDir Directory containing environments
 * @param envName Environment name, must be a directory below baseDir
 *
 * returns array of full paths to each module found in the environment, ordered
 *         by priority
 */
String[] getEnvironmentTFDirs(String baseDir, String envName) {
    // Order to return found modules in, rest will simply
    final def modulePriority = ["network", "common", "secrets", "eks", "services", "flux"]
    def modules = [] as HashSet;

    def startDir = Paths.get(baseDir, envName)
    File dir = new File(startDir.toString())
    dir.eachFileRecurse(FileType.FILES) {
        file ->
        if (file.getName() == "_backend.tf" || file.getName() == "_provider.tf") {
            modules.add(file.getParentFile().getName())
        }
    }

    def ret = [];

    modulePriority.each {
        if (modules.contains(it)) {
            ret.add(Paths.get(startDir.toString(), it))
            modules.remove(it)
        }
    }

    def rest = modules.toArray().sort()

    rest.each {
        ret.add(Paths.get(startDir.toString(), it))
    }

    return ret
}

// println(getEnvironmentTFDirs("something/terraform/environments", "qa"));
