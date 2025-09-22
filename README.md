# AI Personal Assistant

**개인정보 기반 AI 어시스턴트 Android 앱**

OpenAI GPT API를 활용한 개인정보 검색 기반 AI 어시스턴트입니다. 사용자가 입력한 개인정보를 바탕으로 맞춤형 답변을 제공합니다.

## 주요 기능

### **핵심 기능**

- **개인정보 저장**: 자유 텍스트 형식으로 개인정보 입력 및 저장
- **AI 질문 답변**: 저장된 개인정보를 기반으로 한 맞춤형 AI 응답
- **로컬 저장소**: SharedPreferences를 통한 안전한 데이터 저장

### **UI/UX**

- **반응형 디자인**: 모바일 친화적 웹뷰 인터페이스
- **직관적 UI**: 간단하고 사용하기 쉬운 인터페이스

## 🚀 시작하기

### 📋 요구사항

- **Android API Level**: 24 이상 (Android 7.0+)
- **OpenAI API Key**: GPT-3.5-turbo 사용 권한
- **인터넷 연결**: API 통신용

### ⚙️ 설정

1. **저장소 클론**

   ```bash
   git clone https://github.com/YooKwangyeal/ABC.git
   cd ABC
   ```

2. **API 키 설정**

   ```bash
   # gradle.properties 파일 생성
   echo "OPENAI_API_KEY=your_openai_api_key_here" >> gradle.properties
   ```

3. **빌드 및 실행**
   ```bash
   ./gradlew assembleDebug
   ```

## 🏗️ 프로젝트 구조

```
app/
├── src/main/
│   ├── java/com/example/abc/
│   │   ├── MainActivity.kt           # 메인 액티비티 (WebView)
│   │   ├── WebInterface.kt           # JavaScript ↔ Android 인터페이스
│   │   └── api/
│   │       ├── OpenAIService.kt      # OpenAI API 서비스
│   │       └── OpenAIModels.kt       # API 데이터 모델
│   ├── assets/
│   │   └── index.html                # 웹 인터페이스 (다크모드 지원)
│   └── AndroidManifest.xml           # 권한 설정
├── build.gradle.kts                  # 의존성 및 빌드 설정
└── README.md
```

## 🔧 기술 스택

### **Android**

- **Language**: Kotlin
- **UI**: WebView + HTML/CSS/JavaScript
- **Storage**: SharedPreferences
- **HTTP**: OkHttp3
- **JSON**: Gson

### **Frontend (WebView)**

- **HTML5**: 반응형 웹 인터페이스
- **CSS3**: 다크모드 지원, 애니메이션
- **JavaScript**: Android 네이티브 통신

### **Backend/API**

- **OpenAI GPT-3.5-turbo**: AI 응답 생성
- **REST API**: HTTP 통신

## 📖 사용법

### 1. **개인정보 입력**

```
나는 26세 홍길동이고 축구를 좋아하는 초등학교 선생님입니다.
서울에 살고 있으며 취미는 독서와 영화 감상입니다.
```

### 2. **AI에게 질문**

- "내 나이는?"
- "내 직업은 뭐야?"
- "내가 좋아하는 취미는?"
- "내가 사는 곳은?"

### 3. **AI 응답 확인**

AI가 저장된 개인정보에서 관련 내용을 찾아 정확한 답변을 제공합니다.

## 🔐 보안

- **API 키 보호**: `gradle.properties`를 통한 안전한 API 키 관리
- **로컬 저장**: 개인정보는 디바이스 내부에만 저장
- **HTTPS 통신**: OpenAI API와의 암호화된 통신

## 🚧 개발 중인 기능

- [ ] **음성 인식**: 음성으로 질문 입력
- [ ] **대화 히스토리**: 이전 대화 내용 저장
- [ ] **다국어 지원**: 영어, 일본어 지원
- [ ] **위젯**: 홈 화면 위젯 제공

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request