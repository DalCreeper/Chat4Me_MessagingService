pipeline {
    agent any
	
	environment {
        PROXY_PORT = '8001'
        PROXY_URL = "http://127.0.0.1:${env.PROXY_PORT}"
    }
	
    stages {
		stage('Stato attuale servizi') {
			steps {
				bat '''
					echo ===== Setting del context corretto =====
					kubectl config use-context minikube
					kubectl config set-cluster minikube --server=https://127.0.0.1:58270
					
					echo ===== Test connessione al server Kubernetes tramite proxy... =====
                    curl %PROXY_URL%/api || exit /b 1
					
					echo ===== Visualizzazione context =====
					kubectl config current-context
					
					echo ===== Visualizzazione configurazioni =====
					kubectl config view --minify
					
					echo ===== Stato dei pod =====
					kubectl get pods
				'''
			}
		}
	
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

        stage('Build Docker image') {
            steps {
                bat 'docker build -t chat4me-message-service .'
            }
        }

        stage('Deploy su Kubernetes') {
            steps {
                bat '''
                    echo Applico i manifest...
                    kubectl apply -f k8s/deployment.yaml

                    echo ===== Stato dei pod =====
                    kubectl get pods

                    echo ===== Servizi esposti =====
                    kubectl get svc
                '''
            }
        }
    }

    post {
        failure {
            echo '❌ Errore durante il processo.'
        }
        success {
            echo '✅ Pipeline completata con successo!'
        }
    }
}