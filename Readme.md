# 인증 서버 구현
---
### Developer : 황시준

### Build
---
> ./gradlew clean build


### Introduction
---
- 개인 프로젝트로 진행중인 인증 서버 입니다.   
제가 구축하고 있는 서비스는 수평적 확장(HPA)가 수행되기 때문에 해당 서비스의 인증/인가 요청을 수행할 때 세션을 일정하게 유지하기 어렵다는 문제를 극복하고자 JWT인증 토큰을 발급해 Client의 Login/Logout 요청을 수행합니다.  
인증 서버는 `로그인`, `로그아웃`, `토큰 재발급` 역할을 수행합니다.  
또한 인증에 사용자의 정보를 `Redis`에 저장해 발급한 사용자의 `UUID`와 토큰 값을 저장했습니다.
### Skills
---
- Spring Boot
- Spring Security
- Redis
- JPA
- QueryDSL
- RestAPI
- JWT

### Description
---
- `AuthenticationProvider` 에 의해 책임을 받은 `UserDeatilsService`를 Custom한 곳에서 `RestTemplate`으로 API호출 후 인증 과정을 수행합니다.  
또한 `수평적 확장(HPA)`으로 인해 로그인 세션이 일정하게 유지되지 않기에 `Redis`를 사용해 공유 세션 저장소로 사용했고 사용자는 인증 서버로부터 발급한 `UUID Key`를 기준으로 `Redis Session`에 접근하는 방식으로 인증 기능을 수행합니다. 