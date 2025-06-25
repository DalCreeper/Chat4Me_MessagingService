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
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Avvia Minikube se non è attivo') {
			steps {
				bat '''
					minikube status || minikube start --driver=docker ^
						--docker-env HTTP_PROXY=http://host.docker.internal:3128 ^
						--docker-env HTTPS_PROXY=http://host.docker.internal:3128 ^
						--docker-env NO_PROXY=localhost,127.0.0.1,registry.k8s.io
				'''
			}
		}
		
		stage('Verifica accesso a Internet da Minikube') {
			steps {
				bat 'minikube ssh "curl -I https://registry.k8s.io"'
			}
		}

        stage('Configura Docker per Minikube') {
            steps {
                bat '''
                    for /f "tokens=*" %%i in ('minikube docker-env --shell cmd') do %%i
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
                bat '''
                    timeout 30 kubectl apply -f k8s\\deployment.yaml --validate=false
                    timeout 30 kubectl apply -f k8s\\service.yaml --validate=false
                    timeout 30 kubectl apply -f k8s\\hpa.yaml --validate=false
                '''
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