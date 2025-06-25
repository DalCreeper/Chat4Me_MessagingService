pipeline {
    agent any

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

        stage('Espone informazioni Minikube') {
            steps {
                bat '''
                    echo ===== Docker containers in esecuzione =====
                    docker ps

                    echo ===== Minikube info =====
                    minikube status -p minikube-jenkins

                    echo ===== Docker logs del container Minikube =====
                    docker logs minikube-jenkins --tail 50
                '''
            }
        }

        stage('Configura Docker per Minikube') {
            steps {
                bat 'for /f "delims=" %%i in (\'minikube -p minikube-jenkins docker-env --shell cmd\') do %%i'
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