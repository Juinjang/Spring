# Docker Infrastructure

로컬 개발 환경용 인프라 컨테이너 구성.

## 실행

```bash
# 시작
docker compose -f docker/infra-compose.yml up -d

# 중지
docker compose -f docker/infra-compose.yml down

# 볼륨 포함 완전 삭제
docker compose -f docker/infra-compose.yml down -v
```

## 구성

| 서비스 | 이미지 | 포트 | 용도 |
|--------|--------|------|------|
| mysql | mysql:8.0 | 3306 | 메인 데이터베이스 |
| redis | redis:7.0 | 6379 | 캐시 / 세션 스토어 |

## 접속 정보

### MySQL

| 항목 | 값 |
|------|-----|
| Host | localhost:3306 |
| Database | juinjang |
| Root Password | root |
| User | application |
| Password | application |
| Charset | utf8mb4 |

### Redis

| 항목 | 값 |
|------|-----|
| Host | localhost:6379 |
| AOF 영속성 | 활성화 |

## 참고

- 데이터는 Docker named volume에 저장되어 컨테이너 재시작 시에도 유지됨
- 볼륨까지 삭제하려면 `down -v` 사용
- 운영 환경에서는 이 compose 파일을 사용하지 않음 (AWS RDS, ElastiCache 등 사용)
