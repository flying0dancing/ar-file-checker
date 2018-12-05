#!groovy
server = Artifactory.server '-2131115395@1455437299997'
rtMaven = Artifactory.newMavenBuild()
rtMaven.tool = 'mvn-3.3.9' // Tool name from Jenkins configuration
rtMaven.deployer( releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local', server: server)
rtMaven.resolver( releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server)
rtMaven.deployer.deployArtifacts = false // Disable artifacts deployment during Maven run
buildInfo = Artifactory.newBuildInfo()
pipeline {
    agent { label 'master' } 
    environment {
    JAVA_HOME = tool 'JDK8'
  }
    stages {
        stage('Build') { 
            steps {
                echo "start job ${JOB_URL}..."
                echo "path:${PATH}"
                checkout([$class: 'GitSCM', branches: [[name: ':origin/branch.*']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'jenkins', url: 'https://Jenkins@bitbucket.lombardrisk.com/scm/aut/ar-file-checker.git']]])
                
            }
        }
        stage ('Artifactory configuration') {
            steps{
                // Obtain an Artifactory server instance, defined in Jenkins --> Manage:
            
            echo "build info:${buildInfo}"
            }
            
        }
        stage('Test') { 
            steps {
                sh'''
                ls 
                '''
            }
        }
        stage ('Install') {
            steps{
                 echo 'd333'
                //rtMaven.run pom: 'pom.xml', goals: 'clean install -U -DskipITs -DskipTests', buildInfo: buildInfo
				mavenInstall()
            }
        }
        stage('Deploy') {
            steps {
                echo 'dddddd'
                //rtMaven.deployer.deployArtifacts buildInfo
				mavenDeploy()
            }
        }
    }
}

void mavenInstall(){
	rtMaven.run pom: 'pom.xml', goals: 'clean install -U -DskipITs -DskipTests', buildInfo: buildInfo
}

void mavenDeploy(){
	rtMaven.deployer.deployArtifacts buildInfo
	server.publishBuildInfo buildInfo
}