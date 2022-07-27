# :fork_and_knife:momukji-gateway
## :pushpin: 개요
머먹지 프로젝트는 배달 기반의 음식 주문 서비스 입니다. 
Microservice Architecture의 전반적인 이해 및 패턴 구현을 위해 제작되었으며, 현재 해당 momukji-gateway 깃 저장소에는 Gateway와 Eureka Server 만을 다루고 있습니다. 프로젝트의 주요 기능을 다루고 있는 Microservices는 다른 깃 저장소인 momukji-service 에 구현되었습니다. 진행 상황에 있어 세분화된 기록을 위해 저장소를 분리하였습니다. 프로젝트 진행에 따라 ReadMe 업데이트 예정입니다.
* momukji-service : https://github.com/f-lab-edu/momukji-services
## :pushpin: 목표 및 사용 기술 스택 
<img src="https://img.shields.io/badge/Gateway-Spring%20Cloud%20Gateway-green?logo=spring%20boot&logoColor=green"/> <img src="https://img.shields.io/badge/Service%20Discovery-Netflix%20Eureka%20Server-red?logo=netflix&logoColor=red"> <img src = "https://img.shields.io/badge/DataBase-MongoDB-green?logo=mongoDB&logoColor=green">  <img src = "https://img.shields.io/badge/Code%20Style-Naver%20CheckStyle-brightgreen?logo=naver&logoColor=brightgreen"> <img src = "https://img.shields.io/badge/Circuit%20Breaker-Resilience4j-yellow">
* 배달 앱 서비스를 구현해 내는 것이 목표입니다.
* 이유와 근거가 명확한 기술의 사용을 지향합니다.
* 이 프로젝트를 통해 기존의 Monolithic Architecture을 Microservice Architecture로 전환함으로써 두 구조에 대한 전반적인 이해도를 높이고 패턴 구조를 구현하는데 목표를 두고 있습니다. 

## :pushpin: 중점사항 
* 라우팅을 통한 Microservice와의 연결  
* Eureka Server를 이용한 각 Client 별 요청에 대한 원활한 응답
* MongoDB를 이용한 캐시성 데이터, 집계성 데이터에 대한 구체적인 기록
## :pushpin: 프로젝트 구조도
![image](https://user-images.githubusercontent.com/68679529/181180293-6230bca5-b95a-4c0f-81a0-31e7c7d1336e.png)


## :pushpin: 이슈 해결 과정
* [#3] Eureka Server 적용
  https://velog.io/@sweet_sumin/Eureka-%EC%A0%81%EC%9A%A9