def imageTag = ''

pipeline {
    agent any

    options {
        timeout(time: 15, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    environment {
        DOCKER_CREDENTIALS_ID = ''
    }

    stages {
        stage('Test') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    registryCredentialsId ''
                    additionalBuildArgs '--target builder'
                    args '-u $AGENT_UID:$AGENT_GID -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            environment {
                HOME = "${env.WORKSPACE}"
            }
            steps {
                sh './gradlew clean build --no-daemon'
                junit 'build/test-results/test/TEST-*.xml'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true, allowEmptyArchive: false
            }
        }

        stage('Build') {
            when { not { branch 'main' } }
            steps
            {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        def image = docker.build('remove-me-gradle')

                        sh "docker rmi ${image.id}"
                    }
                }
            }
        }

    post {
        always {
            cleanWs()
        }
    }
}
