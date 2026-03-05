# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**주인장 (Juinjang)** - 부동산 임장 관리 서비스의 Spring Boot 백엔드 애플리케이션

## Tech Stack

- **Language**: Java 17 (primary), Kotlin 2.0.20 (being introduced)
- **Framework**: Spring Boot 3.4.4
- **Build**: Gradle 8.5 (Kotlin DSL)
- **ORM**: Spring Data JPA + QueryDSL 5.0.0 (jakarta)
- **Database**: MySQL (production), H2 (test)
- **Cache**: Redis
- **Auth**: Spring Security + OAuth2 (Kakao, Apple) + JWT (jjwt 0.11.5)
- **Cloud**: AWS S3, Google Cloud Vision, Apple StoreKit
- **Monitoring**: Prometheus + Micrometer
- **Lint**: ktlint 12.1.2 (IntelliJ IDEA code style)

## Module Structure

멀티모듈 Gradle 프로젝트:

```
juinjang (root)
└── apps/
    └── juinjang-api/     # API 애플리케이션 모듈
```

소스 코드 위치: `apps/juinjang-api/src/main/java/umc/th/juinjang/`

### Architecture (Layered)

```
api/       → Controllers, Request/Response DTOs, Service interfaces
auth/      → JWT filters, Security config, OAuth2
domain/    → Entities, Repositories, QueryDSL custom repos
common/    → Status codes, exceptions, validation, Redis config
config/    → Application configuration
event/     → Publisher/Subscriber (event-driven 패턴)
```

Kotlin 소스(`src/main/kotlin/`)에 새로운 계층 구조 도입 중:
```
interfaces/ → API 진입점
application/ → Facade (유스케이스 조합)
domain/      → 엔티티, 도메인 로직
infrastructure/ → 외부 연동
support/     → 공통 지원
```

## Common Commands

```bash
# 빌드 (테스트 제외)
./gradlew :apps:juinjang-api:build -x test

# 테스트 실행
./gradlew :apps:juinjang-api:test

# 단일 테스트 클래스 실행
./gradlew :apps:juinjang-api:test --tests "umc.th.juinjang.api.limjang.LimjangControllerTest"

# 클린 빌드
./gradlew clean :apps:juinjang-api:build

# 애플리케이션 실행 (dev 프로필)
./gradlew :apps:juinjang-api:bootRun --args='--spring.profiles.active=dev'

# QueryDSL Q클래스 생성
./gradlew :apps:juinjang-api:compileJava

# ktlint 검사
./gradlew :apps:juinjang-api:ktlintCheck

# ktlint 자동 포맷
./gradlew :apps:juinjang-api:ktlintFormat
```

## Configuration

- `application-dev.yml`, `application-prod.yml`: GitHub Secrets로 관리 (git에 포함되지 않음)
- `application-test.yml`: H2 인메모리 DB (MySQL 모드), 테스트용 더미 값 포함
- 환경별 logback 설정: `logback-dev.xml`, `logback-prod.xml`, `logback-local.xml`

## CI/CD

- **CI**: GitHub Actions — PR to `dev`/`prod` 시 빌드 검증
- **CD**: GitHub Actions — `dev`/`prod` push 시 Docker 빌드 → Docker Hub → EC2 배포
- **Docker Base Image**: `eclipse-temurin:17-jdk`
- **JAR 경로**: `apps/juinjang-api/build/libs/juinjang-api-0.0.1-SNAPSHOT.jar`

## Git Convention

- **Commit**: `[type : 작업내용 #이슈번호]` (예: `add : 이미지 파일 추가 #223`)
- **Branch**: `type/작업내용` (예: `fix/loginerror`)
- **PR Template**: `[type/#n]작업 내용`
- **Main branches**: `dev` (개발), `prod` (운영)

| Type | Description |
|------|-------------|
| add | 새로운 파일 추가 |
| feat | 새로운 기능 추가/수정 |
| fix | 버그 수정 |
| build | 빌드 관련 수정 |
| chore | 패키지 매니저, 기타 수정 |
| ci | CI 관련 설정 수정 |
| docs | 문서 수정 |
| style | 코드 스타일, 포매팅 |
| refactor | 코드 리팩터링 |
| test | 테스트 코드 |
| release | 버전 릴리즈 |
| remove | 코드/파일 제거 |

## Testing

- **Test Support**: `ControllerTestSupport` (MockMvc 기반), `IntegrationTestSupport` (통합 테스트)
- **Mocking**: SpringMockK, Mockito-Kotlin
- **Fixtures**: Instancio로 테스트 데이터 생성
- **Test Profile**: `application-test.yml` (H2, Redis localhost:6379)
