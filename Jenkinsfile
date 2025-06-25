pipeline {
    agent any

    environment {
        IMAGE_NAME = 'chat4me-message-service'
        IMAGE_TAG = 'latest'
        DOCKER_REGISTRY = 'localhost'
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

        stage('Avvia Minikube (se necessario)') {
            steps {
                bat 'minikube start || exit /b 0'
            }
        }

        stage('Configura Docker per Minikube') {
            steps {
                bat 'minikube docker-env --shell cmd > set_docker_env.bat'
                bat 'call set_docker_env.bat'
            }
        }

        stage('Build Docker image') {
            steps {
                bat "docker build -t %DOCKER_REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG% ."
            }
        }

        stage('Deploy su Kubernetes') {
            steps {
                bat 'kubectl apply -f k8s/deployment.yaml'
            }
        }
    }

    post {
        failure {
            echo '❌ Errore durante il processo.'
        }
        success {
            echo '✅ Deploy completato con successo!'
        }
    }
}