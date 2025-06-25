pipeline {
    agent any

    environment {
        IMAGE_NAME = 'dalcreeper/docker-loris-repo'
        IMAGE_TAG = 'chat4me-message-0.0.1'
        MINIKUBE_PROFILE = 'minikube-jenkins'
        HTTP_PROXY = 'http://host.docker.internal:3128'
        HTTPS_PROXY = 'http://host.docker.internal:3128'
        NO_PROXY = 'localhost,127.0.0.1,registry.k8s.io'
    }

    stages {
        stage('Clona repo') {
            steps {
                git branch: 'main', url: 'https://github.com/DalCreeper/Chat4Me_MessagingService.git'
            }
        }

        stage('Build con Maven') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Avvia Minikube se non attivo') {
            steps {
                bat """
                echo Verifico stato Minikube...
                minikube status -p %MINIKUBE_PROFILE% || (
                    echo Avvio Minikube...
                    minikube start -p %MINIKUBE_PROFILE% --driver=docker ^
                        --docker-env HTTP_PROXY=%HTTP_PROXY% ^
                        --docker-env HTTPS_PROXY=%HTTPS_PROXY% ^
                        --docker-env NO_PROXY=%NO_PROXY%
                )
                """
            }
        }

        stage('Configura Docker per Minikube') {
            steps {
                bat '''
                @echo off
                for /f "tokens=*" %%i in ('minikube -p %MINIKUBE_PROFILE% docker-env --shell cmd') do %%i
                '''
            }
        }

        stage('Build Docker image') {
            steps {
                bat "docker build -t %IMAGE_NAME%:%IMAGE_TAG% ."
            }
        }

        stage('Deploy su Kubernetes') {
            steps {
                bat """
                kubectl config use-context minikube
                kubectl apply -f k8s\\deployment.yaml --validate=false
                """
            }
        }
    }

    post {
        success {
            echo '✅ Build e deploy completati con successo!'
        }
        failure {
            echo '❌ Errore durante il processo.'
        }
        always {
            echo 'Pipeline terminata.'
        }
    }
}