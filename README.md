AndroidStreamService
======
AndroidStreamService is a real-time streaming service between android smartphones.
This project was created using Red5 Pro.

>This project has the following features:
1. Sign up / Sign in
2. 


#### 주 기능은 회원가입, 로그인 기능, 화면 촬영, 화면 시청기능이 있다.

>
    BaseballPub: 화면 촬영 (서버 역할)
    BaseballSub: 화면 시청 (클라이언트 역할)
    BaseballSRV: 기타 웹 서비스 기능 (로그인, 회원가입, 현재 실시간 촬영중인 화면 목록들)

#### BaseballSRV는 클라이언트와 간단한 통신을 하는 nodejs 파일이다.

cf swift
========
cf swift is a dashboard for openstack object storage.

# 1. Building
### 1.1 Setting for manifest.yml
>if the file does not exist, you must create the file in the root directory. 

    applications:
    name: swift-portal
    command: node app.js
    buildpack: nodejs_buildpack
    env:
        CF_STAGING_TIMEOUT: 25
        CF_STARTUP_TIMEOUT: 15

### 1.2 Add the following code in node.js main javascript file(such as app.js)
    //Disables HTTPS / SSL / TLS checking across entire node.js environment
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

### 1.3 Setting for cloudfoundry.json
>if the file does not exist, you must create the file in the root directory.

    {
        "ignoreNodeModules": true
    }

# 2. Deploying
    cf login -a https://<api endpoint> --skip-ssl-validation
    cf pushcf swift is a dashboard for openstack object storagekcf swift is a dashboard for openstack object storagev