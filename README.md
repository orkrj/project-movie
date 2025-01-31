## [본 과정] 이커머스 핵심 프로세스 구현
[단기 스킬업 Redis 교육 과정](https://hh-skillup.oopy.io/) 을 통해 상품 조회 및 주문 과정을 구현하며 현업에서 발생하는 문제를 Redis의 핵심 기술을 통해 해결합니다.
> Indexing, Caching을 통한 성능 개선 / 단계별 락 구현을 통한 동시성 이슈 해결 (낙관적/비관적 락, 분산락 등)
# redis-movie_app project
<br>



## 1. API 모듈 
### **목적**
- 외부 세계와의 직접적인 통신을 담당함.

### **역할**
- **실행 모듈**
- HTTP 요청 처리
- REST API 엔드포인트 제공
- Spring Boot 애플리케이션 시작점

---

## 2. Application 모듈 
### **목적**
- 비즈니스 로직을 처리하는 서비스 레이어.

### **역할**
- 비즈니스 서비스 구현
- 포트(Port) 인터페이스 정의
- 도메인 객체와 외부 인터페이스 간 데이터 변환 및 조율

---

## 3. Domain 모듈 
### **목적**
- 애플리케이션의 주요 비즈니스 규칙을 정의.

### **역할**
- 엔티티 정의
- 레포지토리 정의
- 엔티티 객체 관계 설정

---

## 4. Infrastructure 모듈 
### **목적**
- 외부 시스템과 상호작용 관리.

### **역할**
- 더미 데이터 생성
- JPA 레포지토리 인터페이스 정의 및 구현
- 데이터베이스 설정
- 외부 시스템 통합

---

## 모듈 구조 요약

```plaintext
project-movie/       
├── api/           
├── application/    
├── domain/         
└── infrastructure/ 
```
---

## ERD
[movie_app-erd](https://github.com/user-attachments/assets/aa4d568c-948c-4334-bae1-d29ca2602efa)



## 분산 락 성능 테스트
[reserve-distributed-test](https://github.com/orkrj/redis-movie_app/blob/ea78d73f8650d8c9168ab71928fa06f49dbbcba1/reserve-distributed-lock.png)

#### waitTime, leaseTime
- waitTime = 1s : 영화 예매는 대체 선택지가 많기 때문에 대기 시간을 짧게 줌으로써 쾌적한 환경 제공을 우선.
- leaseTime = 2s : 분산 락 테스트 결과 응답에 평균 63ms, 최대 515ms 가 소요되어 최대 응답의 4배를 지정. 
---
