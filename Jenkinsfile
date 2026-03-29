pipeline {
    agent any

    /*
     * Make sure you have Maven configured in Jenkins Global Tool Configuration
     * with the name 'Maven'. Or you can use a docker container as agent.
     */
    tools {
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean package'
                    } else {
                        bat 'mvn clean package'
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker build -t grocery-delivery-system:latest .'
                    } else {
                        bat 'docker build -t grocery-delivery-system:latest .'
                    }
                }
            }
        }

        /* 
        NOTE: You will need valid kubeconfig on your Jenkins server to run this step 
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'kubectl apply -f deployment.yaml'
                    } else {
                        bat 'kubectl apply -f deployment.yaml'
                    }
                }
            }
        }
        */
    }
}
