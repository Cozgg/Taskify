# ADR-005: Chiến lược Container hóa và Reverse Proxy với Nginx
## Trạng thái
Accepted

## Bối cảnh
Chúng tôi cần một phương thức triển khai đồng nhất cho cả Frontend (React), Backend (Spring Boot) và Database (MySQL) để đảm bảo ứng dụng chạy giống nhau trên mọi môi trường (Development, Staging, Production). 

Thách thức đặt ra là làm thế nào để phục vụ các file tĩnh của React, xử lý Routing của Single Page Application (SPA), và giải quyết vấn đề CORS khi Frontend gọi API sang Backend.

## Quyết định
Sử dụng **Docker Compose** để quản lý đa container và tích hợp **Nginx** trực tiếp vào container Frontend thông qua cơ chế **Multi-stage build**:

1.  **Container hóa:** Mỗi thành phần (DB, Backend, Frontend) chạy trong một container riêng biệt.
2.  **Nginx tích hợp:** Container Frontend sẽ không chỉ chứa mã nguồn đã build mà còn chạy một server Nginx bên trong để:
    - Phục vụ (serve) các file tĩnh của React.
    - Xử lý `try_files` để hỗ trợ React Router (tránh lỗi 404 khi refresh trang).
    - Đóng vai trò **Reverse Proxy**, điều hướng các request có tiền tố `/api/` về container Backend qua mạng nội bộ của Docker.

## Lý do
1.  **Giải quyết CORS triệt để:** Bằng cách dùng Nginx làm proxy, Frontend và API dường như chạy trên cùng một Origin (cùng port 80), giúp loại bỏ các lỗi cấu hình CORS phức tạp.
2.  **Tính đóng gói (Encapsulation):** Container Frontend trở thành một đơn vị hoàn chỉnh. Bạn chỉ cần chạy image này là có cả web server và mã nguồn, không cần cài đặt thêm Nginx ngoài máy host.
3.  **Tối ưu hóa tài nguyên:** Sử dụng Multi-stage build giúp image cuối cùng cực kỳ nhẹ (chỉ chứa Nginx và file đã build, không chứa Node_modules hay source code thô).
4.  **Đơn giản hóa hạ tầng:** Việc gộp Nginx vào Frontend giúp `docker-compose.yml` gọn gàng hơn so với việc tách Nginx thành một service riêng biệt (vốn chỉ cần thiết khi có quá nhiều service cần cân bằng tải).

## Hệ quả
**Tích cực:** 
- Triển khai cực nhanh chỉ với lệnh `docker-compose up`.
- Bảo mật hơn vì Backend có thể ẩn hoàn toàn trong mạng nội bộ Docker, chỉ lộ ra qua Proxy của Nginx.
- Xử lý mượt mà các yêu cầu của một ứng dụng SPA.

**Tiêu cực:** 
- Cần kiến thức về cấu hình Nginx (`nginx.conf`) khi muốn thay đổi quy tắc điều hướng API.
- Thời gian build image Frontend lâu hơn một chút do phải trải qua giai đoạn build mã nguồn React.

## Ngày quyết định
2026-04-20
