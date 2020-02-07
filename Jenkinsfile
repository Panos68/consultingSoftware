pipeline {
    agent any
    tools {
       maven 'MAVEN_HOME'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -e -DskipTests clean package'
            }
        }
    }
}