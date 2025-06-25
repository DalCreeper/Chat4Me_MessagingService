pipeline {
    agent any

    environment {
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
                bat '''
                @echo off
                echo Verifico stato Minikube...
                set MINIKUBE_STATUS=
                for /f "tokens=2 delims=:" %%A in ('minikube status -p minikube-jenkins ^| findstr "host:"') do (
                    set MINIKUBE_STATUS=%%A
                )
                if "%MINIKUBE_STATUS:~1%"=="Stopped" (
                    echo Minikube non è attivo. Lo elimino e lo riavvio...
                    call minikube delete -p minikube-jenkins
                    call minikube start -p minikube-jenkins --driver=docker ^
                        --docker-env HTTP_PROXY=%HTTP_PROXY% ^
                        --docker-env HTTPS_PROXY=%HTTPS_PROXY% ^
                        --docker-env NO_PROXY=%NO_PROXY%
                ) else (
                    echo Minikube già attivo.
                )
                '''
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