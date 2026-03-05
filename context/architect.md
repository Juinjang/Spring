# Kotlin 계층 구조 아키텍처

## 개요

Java → Kotlin 리팩토링과 함께 도입한 계층형 아키텍처.
각 계층의 책임을 명확히 분리하고, ArchUnit으로 의존성 규칙을 자동 검증한다.

## 계층 구조

```
HTTP Request
    ↓
┌─────────────────────────────────────────┐
│  interfaces/   — API 진입점              │
│  (Controller, DTO, ApiSpec)             │
├─────────────────────────────────────────┤
│  application/  — 유스케이스 조합          │
│  (Facade, Service, Result)              │
├─────────────────────────────────────────┤
│  domain/       — 핵심 비즈니스 로직       │
│  (Entity, Repository Interface, Enum)   │
├─────────────────────────────────────────┤
│  infrastructure/ — 외부 기술 연동         │
│  (JPA Repository, RepositoryImpl)       │
├─────────────────────────────────────────┤
│  support/      — 공통 유틸 (모든 계층 접근) │
└─────────────────────────────────────────┘
    ↓
Database
```

## 각 계층의 역할

### interfaces

API 진입점. HTTP 요청/응답을 처리한다.

| 요소 | 역할 |
|------|------|
| `Controller` | 엔드포인트 매핑, 요청 위임 |
| `ApiSpec` | Swagger/OpenAPI 스펙 인터페이스 |
| `Dto` | 요청/응답 DTO (sealed class 내 중첩) |

- Controller는 Application 계층의 Facade/Service만 호출한다.
- Repository, Infrastructure 클래스에 직접 의존하지 않는다.

### application

유스케이스를 조합하고 트랜잭션 경계를 관리한다.

| 요소 | 역할 |
|------|------|
| `Facade` | 여러 서비스/리포지토리를 조합하는 오케스트레이터 |
| `Service` | 단일 도메인의 CRUD 및 비즈니스 로직 |
| `Result` | 계층 간 전달용 데이터 클래스 (Domain → Interfaces 변환 중간체) |

- Facade: `@Transactional(readOnly = true)` — 조회 조합
- Service: `@Transactional` — 변경 작업

### domain

핵심 비즈니스 로직. 외부 기술에 의존하지 않는다.

| 요소 | 역할 |
|------|------|
| `Entity` | JPA 엔티티 + 도메인 메서드 (예: `markAsRead()`) |
| `Repository` | 도메인 리포지토리 **인터페이스** (Spring Data 아님) |
| `Enum/VO` | 값 객체, 열거형 |

- Entity는 팩토리 메서드(`create()`)로 생성한다.
- Repository는 순수 인터페이스로, 구현은 Infrastructure에 위치한다.
- `@Entity` 어노테이션은 반드시 `..domain..` 패키지에만 존재해야 한다 (ArchUnit 검증).

### infrastructure

외부 기술 연동. 도메인 인터페이스의 구현체를 제공한다.

| 요소 | 역할 |
|------|------|
| `RepositoryImpl` | 도메인 Repository 구현체 (Adapter) |
| `JpaRepository` | Spring Data JPA 인터페이스 |

- `RepositoryImpl`이 도메인 Repository 인터페이스를 구현하고, 내부적으로 `JpaRepository`에 위임한다.
- 다른 계층에서 직접 접근하지 않는다.

### support

모든 계층에서 접근 가능한 공통 유틸리티.

## 의존성 규칙

```
interfaces  →  application  →  domain  ←  infrastructure
                                  ↑
                               support (모든 계층 접근 가능)
```

| 규칙 | 설명 |
|------|------|
| interfaces → 외부 접근 불가 | 다른 계층이 interfaces를 의존하면 안 된다 |
| application → interfaces, infrastructure만 접근 가능 | domain은 application을 의존하면 안 된다 |
| domain → interfaces, application, infrastructure가 접근 가능 | 가장 안쪽 계층 |
| infrastructure → 외부 접근 불가 | 다른 계층이 infrastructure를 직접 의존하면 안 된다 |
| support → 모든 계층이 접근 가능 | 공통 유틸 |
| interfaces → Repository/Infrastructure 직접 의존 금지 | 반드시 application 계층을 거쳐야 한다 |
| `@Entity` → domain 패키지에만 허용 | 엔티티가 다른 계층에 위치하면 안 된다 |

이 규칙들은 `LayerDependencyTest`(ArchUnit)로 자동 검증된다.

> **참고**: Java 기존 코드와의 격리를 위해 `ExcludeJavaClasses` ImportOption을 사용하여 Kotlin 컴파일 결과(`build/classes/kotlin/`)만 스캔한다.

## 패키지 구조 예시 (AcquiredPencil)

```
src/main/kotlin/umc/th/juinjang/
├── interfaces/api/pencil/acquired/
│   ├── AcquiredApiSpec.kt          # OpenAPI 스펙 인터페이스
│   ├── AcquiredDto.kt              # 요청/응답 DTO (List, Read, ReadStatus)
│   └── AcquiredPencilController.kt # REST 엔드포인트
│
├── application/
│   ├── facade/acquired/
│   │   ├── AcquiredPencilFacade.kt # 서비스 조합 (목록 + 건물명 조회)
│   │   └── AcquiredPencilResult.kt # Facade 반환 데이터 클래스
│   └── service/acquired/
│       └── AcquiredPencilService.kt # CRUD, 읽음 처리, 읽음 상태 확인
│
├── domain/pencil/acquired/
│   ├── AcquiredPencil.kt           # JPA 엔티티 + 도메인 메서드
│   ├── AcquiredPencilRepository.kt # 도메인 리포지토리 인터페이스
│   └── AcquiredType.kt             # 획득 유형 열거형 (NOTE, AD, SOLD, VIEWCOUNT)
│
└── infrastructure/pencil/acquired/
    ├── AcquiredPencilRepositoryImpl.kt  # 도메인 Repository 구현체
    └── AcquiredPencilJpaRepository.kt   # Spring Data JPA 인터페이스
```

## 테스트 구조

```
src/test/kotlin/umc/th/juinjang/
├── architecture/
│   └── LayerDependencyTest.kt      # ArchUnit 계층 의존성 검증
├── domain/pencil/acquired/
│   └── AcquiredPencilTest.kt       # 엔티티 단위 테스트
├── application/service/acquired/
│   └── AcquiredPencilServiceTest.kt # 통합 테스트 (TestContainers)
└── support/
    └── IntegrationTestSupport.kt   # 통합 테스트 기반 클래스
```

### 테스트 유형

| 유형 | 위치 | 도구 | 목적 |
|------|------|------|------|
| **단위 테스트** | `domain/` | JUnit 5 | 엔티티 생성, 도메인 메서드 검증 |
| **통합 테스트** | `application/` | TestContainers (MySQL) | 서비스 로직 + DB 연동 검증 |
| **아키텍처 테스트** | `architecture/` | ArchUnit | 계층 의존성 규칙 자동 검증 |

- 통합 테스트는 `IntegrationTestSupport` 기반 클래스를 상속하며, `@ActiveProfiles("test")`와 `MySqlTestContainersConfig`를 사용한다.
- 단위 테스트는 외부 의존 없이 순수 도메인 로직만 검증한다.
