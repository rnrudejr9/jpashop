## 환경설정

프로젝트 생성
View 환경설정
H2 Database 설치
JPA와 DB설정

```java
Tomcat started on port(s): 8080 (http) with context path ''
```

### 라이브러리

gradle

starter - 톰캣, webMVC, core, logging(slf4j)
thymeleaf
data jpa - aop, jdbc, hikariCP, spring jdbc, hibernate,spring data jpa
test - assertj, junit, mockito

* 핵심 라이브러리
  springMVC, springORM, JPA, hibernate, Spring Data Jpa
* 기타 라이브러리
  h2 database client, HikariCP, web(thymeleaf), loggin, test

### View 환경설정

Thymeleaf
* 장) 네추렬 템플릿, html markup 안에 넣어 문제를 해결함 - 웹 브라우저에서 열림
* 단) 2버전에서는 태그를 닫아주지 않으면 에러가 남 -> 3버전 해결
* 단) 러닝커브가 있음

최근은 프론트쪽에서 사용하긴 함 React, Vue 등

spring 공식 홈페이지에서 guide 참고

Resource 폴더 안
순수한 렌더링 - static
템플릿 - templates

devtools 반영 - 리컴파일을 통해서 빠른 화면 반영이 됨

### H2 Database

1. h2 설치 후 파일 모드로 연결 실행
2. jdbc:h2:tcp://localhost/~/test 접속정보 변경 후 연결
3. yml 파일 변경

```yml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/test;MVCC=TRUE
    username: user
    password:
    driver-class-name: org.h2.Driver
    
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
#        show_sql: true
#        sout 으로 찍음 -> logging 사용권장
        format_sql: true
        
#        스프링부트 메뉴얼에서 보면 됨
logging:
  level:
    org.hibernate.SQL: debug
    
#    sql이 다 보인다 (log를 통해서 찍음)
```

persistence.xml 파일 없이도 매커니즘을 제공함

### 쿼리파라미터 로그남기기

외부라이브러리를 통해서 파라미터의 로그를 남길 수 있다.

```gradle
implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")
```