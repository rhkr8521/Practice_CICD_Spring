# Spring Boot CI/CD - 24.06.24 (Vacation Study)

![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![SpringBoot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

### What is CI/CD? 
- CI/CD는 지속적 통합(Continuous Integration) 및 지속적 배포(Continuous Deployment)를 의미하며, 소프트웨어 개발 라이프사이클을 간소화하고 가속화하는 것을 목표로 한다.
- CI는 언제나 지속적 통합을 의미하며, 코드 변경 사항을 다시 공유 분기로 더 빈번하게 병합하는 것을 용이하게 하는 개발자용 자동화 프로세스이다.
- CD는 지속적인 서비스 제공(Continuous Delivery) 또는 지속적인 배포(Continuous Deployment)를 의미하며 이 두 용어는 상호 교환적으로 사용된다.
<div align="center">
<img src="https://github.com/rhkr8521/Practice_cicd_spring/assets/12209059/09f1c9f9-f3c1-4de1-8704-561e4e5b41e3" width="50%" height="50%"/>
</div>

---
### Why use CI/CD?
- 소프트웨어의 결함이 발견되면, 찾는 데에 더 오래 걸리고 품질이 저하됐을 경우, 고객이 이탈한 확률이 높아진다.
- 뿐만 아니라, 배포하는 프로세스를 수동으로 거친다면, 자동화된 프로세스보다 시간이 더 소요되므로, 배포를 하기에 어려움이 있다.
- 즉, 배포의 지연은 서비스 출시의 지연과 연관이 있다.
- 또한 자주 Commit, Merge 함으로써, 소프트웨어의 품질을 높힐 수 있다는 장점도 있다.
---
### Practice Environment
1. AWS EC2 Free tier - Operating System : Ubuntu 24.04 Server
2. Docker - Docker Hub
3. Spring Boot 3.1.1 - Gradle, JDK17
4. IntelliJ IDE - Ultimate
5. Git
6. GitHub - Action
---
## 1. Start SpringBoot Project
    https://start.spring.io/
    
## 2. Setting GitHub Action Repository Secret
1. Goto Github Repository -> Settings -> Secrets and Variables -> Actions
2. Add 3 Repository secrets
- Name : DOCKERHUB_USERNAME | Your DockerHub Username
- Name : DOCKERHUB_PASSWORD | Your DockerHub Password
- Name : APPLICATION_YML | Your SpringBoot Project application.yml Content

Warning! Why do we separate the application.yml file?
- application.yml 파일에는 데이터베이스 자격 증명, API 키, Secret 키 등 민감한 정보가 포함될 수 있기 때문에 보안 문제로 인해 따로 분리합니다.

## 3. Add Dockerfile to SpringBoot Project
    # Dockerfile

    # jdk17 Image Start
    FROM openjdk:17

    ARG JAR_FILE=build/libs/Vacation_CICD_Practice-0.0.1-SNAPSHOT.jar
    ADD ${JAR_FILE} vacation_cicd.jar
    ENTRYPOINT ["java","-jar","/vacation_cicd.jar"]

## 4. Setting GitHub Action - CI
1. Goto Github Repository -> Actions -> Java with Gradle -> Configure
```
name: Java CI with Gradle

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

permissions:
  contents: read

jobs:
  # Spring Boot 애플리케이션을 빌드하여 도커허브에 푸시하는 과정
  build-docker-image:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    # 1. Java 17 세팅
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    # 2. application.yml 파일 생성
    - name: Create application.yml
      run: |
        mkdir -p src/main/resources
        echo "${{ secrets.APPLICATION_YML }}" > src/main/resources/application.yml

    # 3. Spring Boot 애플리케이션 빌드
    - name: Build with Gradle
      uses: gradle/gradle-build-action@67421db6bd0bf253fb4bd25b31ebb98943c375e1
      with:
        arguments: clean bootJar

    # 4. Docker 이미지 빌드
    - name: docker image build
      run: docker build -t ${{ secrets.DOCKERHUB_USERNAME }}/vacation_cicd_action .

    # 5. DockerHub 로그인
    - name: docker login
      uses: docker/login-action@v2
      with:
        username: ${{ secrets.DOCKERHUB_USERNAME }}
        password: ${{ secrets.DOCKERHUB_PASSWORD }}

    # 6. Docker Hub 이미지 푸시
    - name: docker Hub push
      run: docker push ${{ secrets.DOCKERHUB_USERNAME }}/vacation_cicd_action
```
## 5. Install Docker, Docker Compose to EC2
1. First Ubuntu System Package Update
```
sudo apt-get update
```
2. Install Require Package
```
sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
```
3. Add Docker's Official GPG Key
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```
4. Add Docker's Offical APT Repository
```
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```
5. Ubuntu System Package Update
```
sudo apt-get update
```
6. Install Docker
```
sudo apt-get install docker-ce docker-ce-cli containerd.io
```
7. Check install Docker
```
sudo systemctl status docker
```
If you see "Active: active (running)," it means the installation was successful.

8. Check Docker-compose late version (At the time of writing, the version is 2.28.0.)
```
https://github.com/docker/compose/releases
```
9. Install Docker Compose
```
sudo curl -L https://github.com/docker/compose/releases/download/v2.28.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
```
10. Grant Permission
```
sudo chmod +x /usr/local/bin/docker-compose
```
11. Check install Docker Compose
```
docker-compose --version
```
If you see "Docker Compose version : [version]," it means the installation was successful.

## 6. Setting GitHub Action Runner (self-hosted runner)
1. Goto Github Repository -> Settings -> Code and automation -> Actions -> Runners -> New self-hosted runner
2. Execute all commands up to the ./run.sh command in the Configure section on the EC2 instance.
3. If you encounter the error message "Must not run with sudo" while executing the ./config.sh command, please enter the following command and try again.
```
export RUNNER_ALLOW_RUNASROOT="1"
```
4. Finally, enter the following commands in sequence to complete the setup for receiving GitHub Actions.
```
sudo ./svc.sh install

sudo ./svc.sh start
```

## FINAL. Setting GitHub Action - CD
1. Edit .github/workflows/gradle.yml
2. Add gradle.yml Content
```
# 위 과정에서 푸시한 이미지를 ec2에서 풀받아서 실행시키는 과정 
  run-docker-image-on-ec2:
    # build-docker-image (위)과정이 완료되어야 실행됩니다.
    needs: build-docker-image
    runs-on: self-hosted

    steps:
      # 1. 최신 이미지를 풀받습니다
      - name: docker pull
        run: sudo docker pull ${{ secrets.DOCKERHUB_USERNAME }}/vacation_cicd_action
      
      # 2. 기존의 컨테이너를 중지시킵니다
      - name: docker stop container
        run: sudo docker stop $(sudo docker ps -q) 2>/dev/null || true

      # 3. 최신 이미지를 컨테이너화하여 실행시킵니다
      - name: docker run new container
        run: sudo docker run --name github-actions-demo --rm -d -p 8080:8080 ${{ secrets.DOCKERHUB_USERNAME }}/vacation_cicd_action
      # 4. 미사용 이미지를 정리합니다
      - name: delete old docker image
        run: sudo docker system prune -f
```

### CI/CD Practice Result
![image](https://github.com/rhkr8521/Practice_cicd_spring/assets/12209059/f13f263a-aadd-474f-8168-eb468e38b402)<br>
<img width="872" alt="image" src="https://github.com/rhkr8521/Practice_cicd_spring/assets/12209059/0ff1e115-fcd5-4992-b13d-6fd779c8dd0a"><br>
<img width="688" alt="image" src="https://github.com/rhkr8521/Practice_cicd_spring/assets/12209059/9471edda-fb34-4ae5-b451-29fd04f25d56">

