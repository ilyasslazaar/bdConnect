node{
     def mvnHome = tool name: 'maven', type: 'maven'
     def mvnCmd = "${mvnHome}/bin/mvn"
    stage(' SCM Checkout '){
        checkout scm
    }
    stage('Maven package'){
        sh "${mvnCmd} clean package -P$PROFILE verify  -Dmaven.test.skip=$SKIP_TEST"
    }
    stage('build docker image'){
        sh "docker build -t $IMAGE_NAME ."
    }
     stage('Start docker image'){
        sh "docker-compose -f src/main/docker/$COMPOSE_FILE up -d"
    }
    
}
