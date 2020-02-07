pipeline {
     agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -e -DskipTests clean package'
            }
        }
    }
}