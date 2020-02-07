pipeline {
    agent any
    tools {
       maven 'apache-maven-3.6.3'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -e -DskipTests clean package'
            }
        }
    }
}