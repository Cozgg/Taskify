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
  [![Quản lý Workspace]](link_video_cua_ban)
* **Screenshots:**
  ![Workspace Overview](<img width="1895" height="966" alt="Screenshot 2026-04-30 110838" src="https://github.com/user-attachments/assets/7f4b09ac-e021-4484-bf70-9fa7fdb81438" />
)

### 2. Bảng Kanban & Kéo Thả Trực Quan (Drag & Drop)
Hỗ trợ kéo thả (Drag & Drop) mượt mà các Thẻ (Cards) giữa các Cột (Lists) và thay đổi vị trí các Cột một cách dễ dàng, giúp cập nhật tiến độ công việc ngay lập tức.
* **Video Demo / GIF:**
  [![Kéo thả Kanban]](<img width="1890" height="866" alt="move-card-taskify" src="https://github.com/user-attachments/assets/4c70bc51-3777-419a-9350-ae10d6d8e1b4" />
)
* **Screenshots:**
  ![Board Detail](<img width="1899" height="871" alt="Screenshot 2026-04-30 111054" src="https://github.com/user-attachments/assets/0b954f84-b8ca-4a33-a346-a850a2ed0350" />)(<img width="1900" height="870" alt="Screenshot 2026-04-30 111010" src="https://github.com/user-attachments/assets/aa757ea6-391b-4713-8c77-d9d79e765f59" />)



### 3. Chi Tiết Thẻ Công Việc (Card Details & Collaboration)
Bên trong mỗi thẻ công việc, người dùng có thể:
- Thay đổi mô tả, cài đặt ngày hạn (Due Date) & ngày nhắc nhở (Reminder).
- Tải lên tệp đính kèm (Attachments).
- Gán thành viên chịu trách nhiệm (Assign Members).
- Thảo luận và bình luận thời gian thực (Comments).
* **Screenshots:**
  ![Card Detail Modal](link_anh_card_detail_modal.png)
  ![Comments & Attachments](link_anh_card_comments_attachments.png)

### 4. Thống Kê & Báo Cáo (Board Statistics)
Hệ thống cung cấp biểu đồ trực quan (Biểu đồ tròn & Biểu đồ cột chồng) thống kê tỷ lệ hoàn thành công việc của toàn bộ Bảng và khối lượng công việc đang nắm giữ của từng thành viên.
* **Screenshots:**
  ![Board Statistics](link_anh_thong_ke_board.png)

### 5. Trang Quản Trị Hệ Thống (Admin Dashboard)
Dành riêng cho quyền Admin để quản lý toàn bộ Người dùng (Users), Không gian làm việc (Workspaces), và thống kê tổng quan hoạt động của hệ thống.
* **Screenshots:**
  ![Admin Dashboard](link_anh_admin_dashboard.png)
  ![Quản lý Users](link_anh_admin_users.png)
## Tài liệu
- [ADRs](docs/adrs/)
- [API Documentation](docs/api/)
