# 📱 Ứng dụng Đặt vé tàu – Train Booking App (Android + Firebase)

## 📝 Mô tả / Description (VN/EN)

**🇻🇳 Tiếng Việt:**  
Đây là ứng dụng di động Android giúp người dùng tìm kiếm, xem thông tin và đặt vé tàu trực tuyến. Ứng dụng hỗ trợ Google Map để hiển thị vị trí ga tàu, tích hợp Firebase Realtime Database để lưu trữ và xử lý dữ liệu theo thời gian thực.

---

## 🔧 Công nghệ sử dụng / Technologies Used

| Công nghệ                  | Mục đích                        | Ghi chú                          |
|---------------------------|----------------------------------|----------------------------------|
| Android (Java/Kotlin)     | Nền tảng chính                   | Android Studio                   |
| Firebase Realtime Database| Lưu trữ & quản lý dữ liệu thời gian thực |            |
| Firebase Storage          | Lưu trữ file (ảnh, vé PDF,...)  | Tải lên và tải xuống tệp        |
| Google Maps SDK           | Tích hợp bản đồ                  | Hiển thị vị trí ga tàu          |
| Firebase Authentication   | (Tuỳ chọn) Xác thực người dùng   | Đăng ký/Đăng nhập                |


---

## 🚀 Các tính năng chính / Key Features

- 🔍 Tìm kiếm tàu theo điểm đi – điểm đến  
  *Search trains based on departure and destination*

- 📅 Đặt vé theo thời gian  
  *Book tickets for a selected date and time*

- 🗺️ Tích hợp Google Maps  
  *Integrated Google Maps to show train station locations*

- 🔄 Dữ liệu thời gian thực qua Firebase  
  *Real-time data sync using Firebase Realtime Database*

- 🔐 (Tuỳ chọn) Đăng ký & Đăng nhập người dùng  
  *(Optional) User login & registration with Firebase Authentication*

---

## 📷 Ảnh minh hoạ (Screenshots)

>![image](https://github.com/user-attachments/assets/fbeed0e1-0289-4d3c-9f6b-690eaa63f355)

>![image](https://github.com/user-attachments/assets/822cd043-5636-4dae-83b7-a64998511f32)



## 🛠️ Cài đặt / Installation

```bash
1. Clone repo về máy:
   git clone https://github.com/Ohdatew41003/TrainBookingOnline.git

2. Mở bằng Android Studio

3. Kết nối Firebase: Tools → Firebase → Realtime Database → Connect

4. Build & Run trên điện thoại hoặc trình giả lập Android


💡 Định hướng phát triển / Future Ideas
✅ Thêm tính năng thanh toán trực tuyến (VNPay, Momo, v.v.)

✅ Tích hợp xác nhận email/SMS khi đặt vé

