# Yêu cầu phi chức năng

| Tiêu chí | Mã yêu cầu | Mô tả chi tiết |
| :--- | :--- | :--- |
| **Hiệu năng (Performance)** | PR1 | Tối ưu hóa tốc độ tải trang Board và các thao tác CRUD (tản hồi < 500ms). |
| | PR2 | Trải nghiệm kéo-thả (Drag & Drop) mượt mà, không có độ trễ cảm nhận được trên UI. |
| **Tính sẵn sàng & Tin cậy (Reliability)** | RA1 | Dữ liệu được lưu trữ bền vững trong MySQL 8.4 thông qua Docker Volumes (Persistence). |
| | RA2 | Cơ chế tự khởi động lại (Restart Policy) được cấu hình trong Docker Compose cho toàn bộ dịch vụ. |
| | RA3 | Xử lý tác vụ nặng (như gửi Email) bất đồng bộ qua RabbitMQ để đảm bảo hiệu suất hệ thống. |
| **Bảo mật (Security)** | SR1 | Xác thực người dùng bằng JWT và mã hóa mật khẩu bằng BCrypt trước khi lưu vào DB. |
| | SR2 | Phân quyền truy cập (RBAC) nghiêm ngặt giữa vai trò Admin và User. |
| | SR3 | Chặn truy cập trái phép: Người dùng chỉ được thao tác trên Workspace/Board mà mình tham gia. |
| **Khả năng sử dụng (Usability)** | UR1 | Giao diện hiện đại, nhất quán dựa trên thư viện Ant Design. |
| | UR2 | Tự động hóa việc triển khai toàn bộ ứng dụng (Backend, Frontend, DB) bằng một câu lệnh `docker-compose up`. |
| **Khả năng bảo trì (Maintainability)** | MR1 | Tổ chức mã nguồn Backend theo mô hình phân lớp (Controller-Service-Repository) chuẩn Maven. |
| | MR2 | Toàn bộ log lỗi được đẩy ra Standard Output để dễ dàng quản lý qua hệ thống Docker Logs. |
| **Tích hợp (Integration)** | IR1 | Hỗ trợ lưu trữ và quản lý tệp đính kèm thông qua dịch vụ đám mây Cloudinary. |
