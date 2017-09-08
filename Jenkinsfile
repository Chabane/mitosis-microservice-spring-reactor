node {

    def MANAGER_IP = env.MANAGER_IP
    def APP_NAME = env.APP_NAME

    def server = Artifactory.newServer url: "http://${MANAGER_IP}:9999/artifactory", credentialsId: 'MitosisArtifactoryCredentialsId'
    def rtGradle = Artifactory.newGradleBuild()
    def buildInfo = Artifactory.newBuildInfo()

    def retstat = sh(script: 'docker service inspect microservice-spring-reactor', returnStatus: true)

    stage('Artifactory configuration') {
        rtGradle.tool = 'GRADLE_TOOL' // Tool name from Jenkins configuration
        rtGradle.deployer repo: 'libs-snapshot-local', server: server
        rtGradle.resolver repo: 'jcenter', server: server
    }

    stage('checkout') {
        git url: 'https://github.com/NirbyApp/mitosis-microservice-spring-reactor.git'
    }

    stage('Config Build Info') {
        buildInfo.env.capture = true
        buildInfo.env.filter.addInclude("*")
        buildInfo.env.filter.addExclude("DONT_COLLECT*")
    }

    stage('Extra gradle configurations') {
        rtGradle.deployer.artifactDeploymentPatterns.addExclude("*.war")
        rtGradle.usesPlugin = true // Artifactory plugin already defined in build script
    }

    stage('test') {
        rtGradle.run rootDir: "/", buildFile: 'build.gradle', tasks: 'clean test', buildInfo: buildInfo
    }

    stage('SonarQube analysis') {
        withSonarQubeEnv('Mitosis Sonar') {
            sh 'gradle sonarqube'
        }
    }

    stage('build') {
        rtGradle.run rootDir: "/", buildFile: 'build.gradle', tasks: 'build', buildInfo: buildInfo
    }

    stage('publish') {
        server.publishBuildInfo buildInfo
    }

    stage('deploy to Docker') {
        sh 'docker build -t mitosis/microservice-spring-reactor:1 .'
        if (retstat != 1) {
            // sh 'docker service update --replicas 2 --image mitosis/microservice-spring-reactor microservice-spring-reactor:1'
            sh 'docker service rm microservice-spring-reactor'
        }
        sh "docker service create --name microservice-spring-reactor  --log-driver=gelf --log-opt gelf-address=udp://${MANAGER_IP}:12201 --publish 9991:8080 --network microservices-net  --network ${APP_NAME}-net --replicas 2 mitosis/microservice-spring-reactor:1"
    }
}