pipeline {
    agent any
    stages {
      stage('Tests') {
        steps {
            checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'github_private_key', url: 'https://github.com/iaukhim/rentapp']])
            sh 'mvn clean test'
        }
      }
      stage('Packaging') {
         steps {
            sh 'mvn package -DskipTests=true'
            }
      }
      stage('Building docker image') {
         steps {
            sh 'docker build -t iaukhim/rent-app -f docker/rent-app/Dockerfile .'
            }
      }
      stage('Pushing image to dockerHub') {
         steps {
            sh 'docker push iaukhim/rent-app'
            }
      }
      stage('Deploying app locally') {
         steps {
            sh 'cd ./docker; docker compose up --build -d'
            }
      }
    }
}