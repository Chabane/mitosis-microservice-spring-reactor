node {
                      def retstat = sh(script: 'docker service inspect microservice-spring-reactor', returnStatus: true)

                      def MANAGER_IP = env.MANAGER_IP
                      def APP_NAME = env.APP_NAME

                      stage ('checkout') {
                        git url : 'https://github.com/NirbyApp/mitosis-microservice-spring-reactor.git'
                      }
                      stage ('test') {
                        sh './gradlew test'
                      }

                      stage ('build') {
                        sh './gradlew build'
                      }

                      stage ('deploy') {
                        sh 'docker build -t mitosis/microservice-spring-reactor:1 .'
                        if (retstat != 1) {
                            // sh 'docker service update --replicas 2 --image mitosis/microservice-spring-reactor microservice-spring-reactor:1'
                            sh 'docker service rm microservice-spring-reactor'
                        }
                        sh "docker service create --name microservice-spring-reactor  --log-driver=gelf --log-opt gelf-address=udp://${MANAGER_IP}:12201 --publish 9991:8080 --network microservices-net  --network ${APP_NAME}-net --replicas 2 mitosis/microservice-spring-reactor:1"
                     }
                }