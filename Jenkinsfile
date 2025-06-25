pipeline {
    agent any

    environment {
        IMAGE_NAME = 'dalcreeper/docker-loris-repo'
        IMAGE_TAG = 'chat4me-message-0.0.1'
    }

    stages {
        stage('Clona repo') {
            steps {
                git branch: 'main', url: 'https://github.com/DalCreeper/Chat4Me_MessagingService.git'
            }
        }

        stage('Build con Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Configura Docker per Minikube') {
            steps {
                sh 'eval $(minikube docker-env)'
            }
        }

        stage('Build Docker image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }
		
		# Applica i manifest (nel repo k8s, tipo k8s/deployment.yaml)
        stage('Deploy su Kubernetes') {
            steps {
                sh 'kubectl apply -f k8s/
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminata.'
        }
        success {
            echo 'Build e deploy completati con successo!'
        }
        failure {
            echo 'Errore durante il processo.'
        }
    }
}