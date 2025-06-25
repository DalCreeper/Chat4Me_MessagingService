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
		
		stage('Verifica utente Jenkins') {
			steps {
				bat 'echo UTENTE ATTIVO: %USERNAME%'
			}
		}
		
        stage('Avvia Minikube') {
			steps {
				bat '''
				if not exist "%USERPROFILE%\\.minikube\\machines\\minikube-jenkins" (
					minikube start -p minikube-jenkins --driver=docker --docker-env HTTP_PROXY=http://host.docker.internal:3128 --docker-env HTTPS_PROXY=http://host.docker.internal:3128 --docker-env NO_PROXY=localhost,127.0.0.1,registry.k8s.io
				) else (
					echo "Minikube già inizializzato"
				)
				'''
			}
		}
		
		stage('Verify Minikube') {
            steps {
                bat 'minikube status'
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