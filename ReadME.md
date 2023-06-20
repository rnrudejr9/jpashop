# 실전 스프링 부트 JPA 활용 1편
## 프로젝트 생성과 환경설정
* gradle 빌드 툴, 핵심 라이브러리 이해
## DataBase 설정, yml (h2 database)
* aplication.yml 파일 설정
* 쿼리파라미터 로그 남기기
## 요구사항 명세서 분석
* 매핑관계 설정
  * XToX 방식의 이해
  * 연관관계의 주인
* 엔티티 설계, DB설계
  * 지연로딩, 즉시로딩
  * setter 사용 금기
  * 비즈니스 로직을 Domain과 Service에서 개발
## JPA 및 계층구조
* Service, Repository, Entity, Controller 계층 이해
  * 스프링 빈 등록
  * 의존성 주입(@RequiredArgsConstructor)
* SpringMVC 동작
* Model, Attribute
## JPA 변경감지 기능 vs 병합
* 가능하면 변경감지 기능 사용해라
## 정적쿼리, 동적쿼리 
* jpql + queryDSL
## TESTCASE 작성 (단위테스트 말고 통합테스트)
* 인메모리 DB 사용 (default)