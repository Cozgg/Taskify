# Task Management
## Mô tả
Hệ thống Quản lý công việc (Trello Clone) là nền tảng cộng tác dựa trên mô hình Kanban, cho phép quản lý dự án trực quan qua các bảng công việc và cột trạng thái linh hoạt. Người dùng có thể tạo nhiệm vụ, gán thành viên, đặt hạn chót và tương tác qua bình luận, đồng thời theo dõi tiến độ tổng thể bằng các công cụ thống kê. Hệ thống phân quyền chặt chẽ cho Admin, Quản lý dự án và Thành viên, giúp tối ưu hóa quy trình làm việc và đảm bảo tính minh bạch cho nhóm.
## Thành viên nhóm
| MSSV | Họ tên | Vai trò |
|------|--------|---------|
| 2351010180 | Phạm Anh Quyền | Trưởng nhóm |
| 2351010025 | Nguyễn Hữu Công | Thành Viên |
| 2351010022 | Huỳnh Thế Cảnh | Thành Viên |
## Công nghệ sử dụng
- Backend: Java + Spring boot
- Frontend: React
- Database: MySQL
- Message Queue: RabbitMQ
- Container: Docker + Docker Compose
## Kiến trúc
![Architecture](docs/architecture/c4-container.png)
## Cài đặt và chạy
### Yêu cầu
- Docker Desktop
- Git
### Chạy với Docker Compose
- git clone https://github.com/quyendzvcb/Taskify.git

- cd Taskify

- docker-compose up -d
### Truy cập
- Frontend: http://localhost:3000
- Backend API: http://localhost:8000
- RabbitMQ Management: http://localhost:15672
## Demo
Dưới đây là một số hình ảnh và video thực tế demo các chức năng cốt lõi của **Taskify**.
### 1. Quản lý Workspace & Bảng (Workspaces & Boards)
Người dùng có thể tạo không gian làm việc chung, mời thành viên tham gia,tạo các Bảng (Boards) bên trong từng không gian.
* **Video Demo / GIF:**
  [![Quản lý Workspace và mời thành viên tham gia]](https://github.com/user-attachments/assets/59187b51-d6c2-424f-b842-c0af506d6b77)

### 2. Bảng Kanban & Kéo Thả Trực Quan (Drag & Drop)
Hỗ trợ kéo thả (Drag & Drop) mượt mà các Thẻ (Cards) giữa các Cột (Lists) và thay đổi vị trí các Cột một cách dễ dàng, giúp cập nhật tiến độ công việc ngay lập tức.

* **Video Demo / GIF:**
  ![Kéo thả Kanban](https://github.com/user-attachments/assets/4c70bc51-3777-419a-9350-ae10d6d8e1b4)


### 3. Chi Tiết Thẻ Công Việc (Card Details & Collaboration)
Bên trong mỗi thẻ công việc, người dùng có thể:
- Thay đổi mô tả, cài đặt ngày hạn (Due Date) & ngày nhắc nhở (Reminder).
- Tải lên tệp đính kèm (Attachments).
- Gán thành viên chịu trách nhiệm (Assign Members).
- Thảo luận và bình luận thời gian thực (Comments).
* **Video Demo / GIF:**
  ![Card Detail Modal](https://github.com/user-attachments/assets/cf0f0d32-e161-45f3-9f76-f602eb108bd0)
  ![Comments & Attachments](https://github.com/user-attachments/assets/23a09caa-d9b4-481f-b2aa-e9adb3c3a232)

### 4. Thống Kê & Báo Cáo (Board Statistics)
Hệ thống cung cấp biểu đồ trực quan (Biểu đồ tròn & Biểu đồ cột chồng) thống kê tỷ lệ hoàn thành công việc của toàn bộ Bảng và khối lượng công việc đang nắm giữ của từng thành viên.
* **Screenshots:**
  ![Board Statistics](https://github.com/user-attachments/assets/f5a57a1b-df4a-42e4-9b67-1e6d13d97a53)
  ![Board Statistics](https://github.com/user-attachments/assets/e8ac65f3-9c39-423e-8912-7896dd253e95)


### 5. Trang Quản Trị Hệ Thống (Admin Dashboard)
Dành riêng cho quyền Admin để quản lý toàn bộ Người dùng (Users), Không gian làm việc (Workspaces), và thống kê tổng quan hoạt động của hệ thống.
* **Screenshots:**
  ![Admin Dashboard](https://github.com/user-attachments/assets/2902cdd3-cda5-488d-976f-cf4d259958e2)
  ![Quản lý Users](https://github.com/user-attachments/assets/ca44b4ab-7999-45c0-b689-31190f8c3fa8)
  ![Quản lý workspace](https://github.com/user-attachments/assets/86b0a719-ee79-48d7-9dad-8844e0e49424)
## Tài liệu
- [ADRs](docs/adrs/)
- [API Documentation](docs/api/)
