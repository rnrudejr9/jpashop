## 간단히 소개
Jpa 사용할 때 반복되는 코드를 자동화 해준다.

* ex) save, findAll 내부 소스는 반복적이다.

* JpaRepository 상상하는 모든 crud 다 제공해준다.

* findByName 같은 것들도 jpql 만들어서 날린다.

개발자는 인터페이스만 만들면 된다.
스프링 데이터 jpa 가 app 실행 시점에 구현체 주입을 받는다.

## 주의할점
spring data jpa 는 JPA를 사용해서 이런 기능을 제공하기 때문에 결국 **JPA 자체**를 이해하는것이 가장 중요하다.



