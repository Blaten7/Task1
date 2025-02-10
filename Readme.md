# 🛠 Task1 - AI 챗봇 API

AI 챗봇 서비스의 백엔드 API입니다.  
사용자 인증, 대화 관리, 피드백 시스템, 보고서 생성 기능을 제공합니다.

## 📌 프로젝트 개요
### ✅ 주요 기능
- 사용자 **회원가입, 로그인, JWT 인증**
- **AI 챗봇 대화 관리 (OpenAI API 연동)**
- **피드백 시스템** (대화에 대한 사용자 피드백 제공)
- **관리자 대시보드** (사용자 활동 기록, 보고서 다운로드)

## 🛠 기술 스택
- **Backend:** Spring Boot 3.2, Java 17, Spring Security, JPA (Hibernate)
- **Database:** PostgreSQL
- **Authentication:** JWT (JSON Web Token)
- **API 문서화:** Postman

## 📜 API 사용 방법
1️⃣ **Postman 에서 API 테스트**
🔗 **[Postman API 보기](https://documenter.getpostman.com/view/38985084/2sAYX9mzX6)**

2️⃣ **로컬 실행 방법**
```sh
git clone https://github.com/Blaten7/Task1.git
cd Task1
./gradlew bootRun
```
