// @Library('terraform-pipeline')
// import TerraformValidateStage;

class HolonTerraformValidateStage extends TerraformValidateStage {
    private Jenkinsfile jenkinsfile
    private StageDecorations decorations

    private static globalPlugins = []

    @Override
    public Closure pipelineConfiguration() {
        applyPlugins()

        def validateCommand = TerraformValidateCommand.instance()

        return {
            node(jenkinsfile.getNodeName()) {
                deleteDir()
                //checkout(scm)

                decorations.apply(ALL) {
                    stage("validate") {
                        decorations.apply(VALIDATE) {
                            sh validateCommand.toString()
                        }
                    }
                }
            }
        }
    }
}
