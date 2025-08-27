# About 주인장 버전 v1.1.5

![주인장기획서이미지](https://github.com/user-attachments/assets/b2e72c8f-0425-4571-946e-866cf56704ac)

# Backend Team

| 코난 ([hyeonahhh](https://github.com/hyeonahhh))                                                           | 시니 ([2hy2on](https://github.com/2hy2on))                                                                 | 여니 ([yeon015](https://github.com/yeon015))                                                               | 진 ([PicturePark1101](https://github.com/PicturePark1101))                                                |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/f9cf4cdb-0e39-4b53-aa2d-92bb5e81bb72" width="300"/> | <img src="https://github.com/user-attachments/assets/9f41cd55-0b63-4bfb-b91c-4f6473548122" width="300"/> | <img src="https://github.com/user-attachments/assets/153a7961-97fa-46da-87d0-214f52710ac3" width="300"/> | <img src="https://github.com/user-attachments/assets/50e1e9fd-d687-4416-81ed-950105551a74" width="300"/> |
| ● 녹음 관련 API <br> ● 체크리스트 관련 API                                                                          | ● Spring Security, JWT 기반 인가 인증 구현<br> ● Oauth 연동 Kakao, Apple 로그인 구현 <br> ●CI/CD 구축                     | ● Spring Security, JWT 기반 인가 인증 구현<br> ● Oauth 연동 Kakao, Apple 로그인 구현                                    | ● 임장 관련 API<br> ● Prometheus & Grafana 기반 모니터링 구축                                                        |

# Architecture

![아키텍쳐22](https://github.com/user-attachments/assets/29561ca0-7194-4ac9-aa65-1a04cad7e410)

# Stack

![주인장리드미](https://github.com/user-attachments/assets/e57dbed8-d923-40f6-91cc-04ecda91f96b)

# Convention

## tag type

| 유형           | 설명                                          |
|--------------|---------------------------------------------|
| **add**      | 새로운 파일 추가                                   |
| **feat**     | 만들어진 파일에 새로운 기능 추가, 수정. 기존 기능을 요구 사항에 맞게 수정 |
| **fix**      | 기능에 대한 버그 수정                                |
| **build**    | 빌드 관련 수정                                    |
| **chore**    | 패키지 매니저 수정, 기타 수정 (ex: `.gitignore`)        |
| **ci**       | CI 관련 설정 수정                                 |
| **docs**     | 문서 수정                                       |
| **comment**  | 주석 추가                                       |
| **style**    | 코드 스타일, 포매팅 관련 수정                           |
| **refactor** | 기능 변화 없이 코드 리팩터링 (ex: 변수 이름 변경)             |
| **test**     | 테스트 코드 추가/수정                                |
| **release**  | 버전 릴리즈                                      |
| **remove**   | 코드 또는 파일, 리소스 제거                            

## Commit message

[type : 작업내용 #이슈번호]  <br>
예시 :  `add : 이미지 파일 추가 #223`

## branch

type/작업내용  <br>
예시 : `fix/loginerror`
