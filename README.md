# 🎵 Music App - Ứng Dụng Nghe Nhạc Streaming

Một ứng dụng nghe nhạc Android hiện đại được xây dựng bằng **Kotlin** với kiến trúc **MVVM + Clean Architecture**, cung cấp trải nghiệm nghe nhạc thú vị.

> **Backend Repository**: [music-app-backend](https://github.com/NTV-CKN/music-app-backend) - Firebase Cloud Functions + Firestore

---

## ✨ Tính Năng Chính

### 🎧 Phát Nhạc & Playlist
- Phát nhạc với chất lượng cao từ ExoPlayer 3
- Tạo, quản lý và lưu playlist cá nhân
- Tổ chức bài hát theo danh sách yêu thích
- Tiếp tục phát từ vị trí trước đó khi quay lại ứng dụng
- Streaming qua Firebase Cloud Functions (Đảm bảo xác thực bài hát VIP và quyền người nghe)

### 👤 Xác Thực & Bảo Mật
- Đăng nhập bằng **Google OAuth 2.0** với JWT token
- Token validation qua Cloud Functions

### 💳 Thanh Toán & Gói Dịch Vụ
- Tích hợp **VNPay Sandbox** để thanh toán gói dịch vụ
- Truy cập bài hát VIP sau khi thanh toán thành công
- Quản lý và theo dõi lịch sử giao dịch
- Callback xác thực từ VNPay

### 🤖 AI & Gợi Ý Thông Minh
- **Google GenAI (Gemini 3.6 Flash)** phân tích cảm xúc từ văn bản
- Gợi ý Top 10 bài hát phù hợp với tâm trạng người dùng
- Hỗ trợ khám phá nhạc mới theo sở thích cá nhân
- Xử lý bằng Cloud Functions bảo mật cao

### 🎼 Quản Lý Nội Dung
- Giao diện Admin quản lý bài hát, album, ca sĩ
- Quản lý danh sách bài hát VIP và các gói dịch vụ
- Xử lý quyền truy cập dựa trên loại gói dịch vụ
- RESTful API qua Firebase Cloud Functions

---

## 🛠️ Công Nghệ & Thư Viện Sử Dụng

### Frontend (Android - Kotlin)

#### Android Framework
- **Kotlin** - Ngôn ngữ lập trình chính
- **MVVM + Clean Architecture** - Kiến trúc phần mềm
- **ViewModel & LiveData** - Quản lý trạng thái UI
- **View Binding** - Liên kết view an toàn

#### Networking & Data
- **Retrofit 2 + Gson Converter** - Gọi API REST
- **OkHttp 4** - HTTP Client
- **Room Database** - Lưu trữ dữ liệu cục bộ
- **Flow & Coroutines** - Thao tác bất đồng bộ, cancel-safe

#### Media & Playback
- **Media3 (ExoPlayer 3)** - Phát nhạc chuyên nghiệp
- **BoundService + MediaController** - Điều khiển phát nhạc background
- **Glide 4** - Tải và cache ảnh hiệu quả

#### Quản Lý Dữ Liệu & Session
- **DataStore** - Lưu preferences an toàn
- **Paging 3** - Phân trang danh sách lớn
- **Room + Flow** - Kết hợp bất đồng bộ với CSDL

#### Dependency Injection & Navigation
- **Hilt** - Tiêm phụ thuộc tự động
- **Navigation Component** - Điều hướng giữa Fragments
- **Safe Args** - Truyền dữ liệu an toàn giữa màn hình

### Backend (Node.js - Firebase Cloud Functions)

#### Runtime & Infrastructure
- **Node.js 24** - Runtime environment
- **Firebase Cloud Functions** - Serverless compute
- **Google Cloud Firestore** - NoSQL database
- **Firebase Admin SDK** - Quản lý Firestore & authentication

#### APIs & Integrations
- **Express.js (embedded)** - RESTful API routing
- **Google GenAI (Gemini 3.6 Flash)** - AI analysis & recommendations
- **VNPay SDK** - Thanh toán sandbox integration
- **firebase-functions** - Cloud Functions SDK

#### Development & Deployment
- **ESLint** (Google config) - Code quality
- **Firebase Emulator** - Local development
- **dotenv** - Environment variable management

---

## 🏗️ Kiến Trúc Ứng Dụng

### Frontend Architecture (Android)

```
music-app/
├── data/                          # Layer dữ liệu
│   ├── dto/                       # Data Transfer Objects (request/response API)
│   ├── model/                     # Data models
│   ├── repository/                # Repository implementation
│   └── source/                    # Data sources
│       ├── local/                 # Nguồn dữ liệu local (Room/DataStore...)
│       │   ├── ai_rcm/            # Local source cho AI Recommendation
│       │   ├── album/             # Local source cho Album
│       │   ├── artist/            # Local source cho Artist
│       │   ├── db/                # Room Database, DAO
│       │   ├── playlist/          # Local source cho Playlist
│       │   ├── recent/            # Lịch sử nghe gần đây
│       │   ├── search/            # Lịch sử/cache tìm kiếm
│       │   ├── song/              # Local source cho Song
│       │   ├── tracking/          # Theo dõi hành vi người dùng
│       │   └── user/              # Local source cho User
│       │
│       └── remote/                # Nguồn dữ liệu remote (API)
│           ├── ai_rcm/            # Remote source cho AI Recommendation
│           ├── album/             # Remote source cho Album
│           ├── artist/            # Remote source cho Artist
│           ├── auth/              # Đăng nhập/xác thực
│           ├── param/             # Query/Request params dùng chung
│           ├── playlist/          # Remote source cho Playlist
│           ├── song/              # Remote source cho Song
│           ├── subscription/      # Gói VIP/Premium
│           ├── user/              # Remote source cho User
│           ├── MusicService.kt    # Retrofit service interface
│           └── RetrofitHelper.kt  # Cấu hình/khởi tạo Retrofit
│
│       # Các DataSource implementation nằm ngay dưới source/ (không rõ local hay remote riêng theo tên):
│       ├── AIRecommendDataSource.kt
│       ├── AlbumDataSource.kt
│       ├── ArtistDataSource.kt
│       └── AuthDataSource.kt
│           ... (và các DataSource khác tương ứng playlist, song, subscription, user...)
│
├── di/                             # Dependency Injection (Hilt)
│   ├── db/
│   ├── now_playing/
│   ├── qualifier/
│   ├── repository/
│   ├── source/
│   ├── FirebaseModule.kt
│   └── RetrofitModule.kt
│
├── enums/
├── media/
│
├── ui/
│   ├── adapter/
│   ├── admin/
│   ├── auth/
│   ├── base/
│   ├── detail/
│   ├── dialog/
│   ├── discovery/
│   ├── home/
│   ├── library/
│   ├── playing/
│   ├── search/
│   ├── settings/
│   ├── user/
│   ├── viewmodels/
│   ├── vip_subscription/
│   └── MainActivity.kt
│
├── utils/
└── MusicApplication.kt
```

### Backend Architecture (Firebase)

```
music-app-backend/
├── .firebaserc           # Firebase project config
├── firebase.json         # Firebase CLI config
├── firestore.rules       # Firestore security rules
├── firestore.indexes.json # Composite indexes
└── functions/            # Cloud Functions codebase
    ├── index.js          # Entrypoint & route mounting
    ├── auth.js           # Google OAuth & JWT logic
    ├── authMiddleware.js # Token verification middleware
    ├── seed.js           # Sample data initialization
    ├── genreSong.js      # Genre constants/enums
    ├── routes/           # API route handlers
    │   ├── auth.js
    │   ├── songs.js
    │   ├── albums.js
    │   ├── artists.js
    │   ├── subscriptions.js
    │   ├── payments.js
    │   ├── ai_rcm.js
    │   └── stream.js
    ├── controllers/      # Business logic controllers
    ├── services/         # Helper services (VNPay, AI, etc)
    ├── utils/            # Utility functions
    └── package.json      # Dependencies & scripts
```


### Stream & VIP Access
- ExoPlayer streaming qua Firebase Cloud Functions
- Kiểm tra quyền VIP trước khi phát bài hát
- Tracking stream events cho analytics
- Cache bài hát locally khi có quyền

---

## 📋 Yêu Cầu Hệ Thống

### Frontend (Android)
- **Android**: API 26+ (Android 8.0 trở lên)
- **Java/Kotlin**: JDK 11+
- **Gradle**: 7.0+
- **Android Studio**: Giraffe (2022.3.1) hoặc mới hơn

### Backend
- **Node.js**: 24.x
- **Firebase CLI**: 13.0+
- **Google Cloud SDK**: Latest

---

## 🚀 Cài Đặt & Chạy Ứng Dụng

### 1. Clone Repository

```bash
# Frontend
git clone https://github.com/NTV-CKN/music-app.git
cd music-app

# Backend (trong thư mục khác)
git clone https://github.com/NTV-CKN/music-app-backend.git
cd music-app-backend
```

### 2. Cấu Hình Backend (Firebase)

#### 2.1 Khởi tạo Firebase Project

```bash
cd music-app-backend

# Đăng nhập Firebase
firebase login

# Chọn hoặc tạo project
firebase use --add
# Chọn project ID: music-app-fcd10
```

#### 2.2 Cài đặt Dependencies

```bash
cd functions
npm install
```

#### 2.3 Cấu hình Biến Môi Trường

Tạo file `.env` trong thư mục `functions/`:

```env
# Google GenAI (AI Recommendations)
GEMINI_API_KEY=your_gemini_api_key_here
MODEL_AI=gemini-2.0-flash

# VNPay Sandbox (Payment)
VNP_TMN_CODE=your_merchant_code
VNP_HASH_SECRET=your_hash_secret
VNP_HOST=https://sandbox.vnpayment.vn
RETURN_URL=https://your-backend-url/v1/subscriptions/payment/vnpay/return

# Firebase
FIREBASE_PROJECT_ID=music-app-fcd10
```

**Lấy API Keys:**
- **GEMINI_API_KEY**: [Google AI Studio](https://aistudio.google.com/api-keys)
- **VNPay Credentials**: Đăng ký tại [VNPay Sandbox](https://sandbox.vnpayment.vn/devreg/)

#### 2.4 Chạy Firebase Emulator (Local Development)

```bash
# Trong thư mục functions/
npm run serve
# Truy cập: http://localhost:5001/music-app-fcd10/eur3/api-name
```

#### 2.5 Seed Dữ Liệu Mẫu

```bash
# Từ thư mục functions/
node seed.js
```

#### 2.6 Deploy lên Firebase

```bash
# Từ thư mục functions/
npm run deploy
```

### 3. Cấu Hình Frontend (Android)

#### 3.1 Google Sign-In Setup

1. Vào [Google Cloud Console](https://console.cloud.google.com)
2. Tạo dự án hoặc chọn dự án hiện có
3. Kích hoạt **Google Identity Services API**
4. Tạo OAuth 2.0 credential cho Android:
   - **Application type**: Android
   - **Package name**: `com.example.musicapp`
   - **SHA-1 fingerprint**: Lấy từ Android Studio hoặc:
     ```bash
     ./gradlew signingReport
     ```
5. Download `google-services.json` và đặt vào `app/`

#### 3.2 Cấu hình Backend URL

Tạo/cập nhật `local.properties`:

```properties
# Backend API
backend_url=http://your-backend-url.com
# Hoặc local emulator:
backend_url=http://192.168.x.x:5001/music-app-fcd10/eur3

# Firebase Configuration
firebase_project_id=music-app-fcd10
firebase_api_key=your_firebase_api_key
```

Hoặc cập nhật trong `BuildConfig`:

```kotlin
object Config {
    const val BACKEND_URL = "https://your-backend-url.com"
    const val FIREBASE_PROJECT = "music-app-fcd10"
}
```

#### 3.3 Build & Run

```bash
# Debug
./gradlew installDebug

# Release (cần signing config)
./gradlew assembleRelease
```

---

## 📱 Hướng Dẫn Sử Dụng

### Người Dùng Mới
1. Tải và cài đặt ứng dụng
2. Nhấn "Đăng nhập với Google"
3. Xác thực qua Google Account
4. Khám phá danh sách bài hát miễn phí
5. Tạo playlist cá nhân

### Nâng Cấp Premium/VIP
1. Vào **Gói Dịch Vụ**
2. Chọn gói phù hợp theo giá tiền
3. Nhấn thanh toán → Chuyển đến VNPay
4. Hoàn tất thanh toán Sandbox
5. Truy cập bài hát VIP ngay lập tức

### Tìm Nhạc Theo Cảm Xúc (AI Recommendation)
1. Mở **AI Gợi Ý**
2. Nhập cảm xúc/tâm trạng hiện tại (VD: "Buồn nhưng yêu thương")
3. Xem Top 10 bài hát được Gemini AI gợi ý
4. Thêm các bài yêu thích vào playlist

---

## 📊 Project Structure Overview

```
NTV-CKN Organization
├── music-app (Kotlin)
│   ├── Frontend MVVM + Clean Architecture
│   ├── ExoPlayer streaming
│   ├── Google OAuth integration
│   └── VNPay payment UI
│
└── music-app-backend (Node.js)
    ├── Firebase Cloud Functions
    ├── Firestore database
    ├── Google GenAI integration
    └── VNPay sandbox processing
```

---

## 🐛 Troubleshooting

### Backend Issues
- **Firebase Emulator không kết nối**: Kiểm tra `firebase.json` emulator config
- **Biến môi trường không load**: Đặt file `.env` trong `functions/` và restart emulator
- **VNPay sandbox lỗi**: Đảm bảo `VNP_TMN_CODE` và `VNP_HASH_SECRET` chính xác

### Frontend Issues
- **Google Sign-In fail**: Kiểm tra SHA-1 fingerprint và `google-services.json`
- **Backend URL not found**: Cập nhật `backend_url` trong `local.properties`
- **Streaming error**: Đảm bảo user có quyền VIP hoặc bài hát miễn phí

---

## 👨‍💻 Tác Giả

**NTV-CKN** - [GitHub Profile](https://github.com/NTV-CKN)

- **Email**: [nguyentruongvu000111@example.com](mailto:nguyentruongvu000111@example.com)
- **Frontend Repo**: [music-app](https://github.com/NTV-CKN/music-app)
- **Backend Repo**: [music-app-backend](https://github.com/NTV-CKN/music-app-backend)

---

## 📞 Hỗ Trợ & Liên Hệ

- **Issues & Bug Reports**: [GitHub Issues](https://github.com/NTV-CKN/music-app/issues)
- **Discussion**: [GitHub Discussions](https://github.com/NTV-CKN/music-app/discussions)
- **Contact**: Liên hệ qua GitHub hoặc email

---

## 📚 Tài Liệu Tham Khảo

- [Android Docs - MVVM](https://developer.android.com/jetpack/guide)
- [Firebase Cloud Functions Docs](https://firebase.google.com/docs/functions)
- [Media3 (ExoPlayer) Docs](https://developer.android.com/guide/topics/media/media3)
- [Google GenAI (Gemini) Docs](https://ai.google.dev/)
- [VNPay Integration Guide](https://sandbox.vnpayment.vn/docs/)

---

## 🙏 Cảm Ơn

Cảm ơn bạn đã sử dụng **Music App**! Nếu thích dự án, vui lòng ⭐ **Star** repository này để ủng hộ!

---

**Last Updated**: August 2026  
**Version**: 2.0.0  
**Status**: Active Development
