# ADR-001: Lựa chọn kiến trúc Layered
## Trạng thái
Accepted

## Bối cảnh
Chúng tôi cần xây dựng hệ thống quản lý công việc theo dạng Kanban board (Trello Clone) với các tính năng: quản lý workspace, board, task, gán thành viên, bình luận và thống kê.

Hệ thống có quy mô vừa, team 3 người, thời gian phát triển 6 tuần.

Đối tượng sử dụng (Actors): Thành viên, Quản lý dự án, Admin.

## Quyết định
Sử dụng kiến trúc Layered (Phân tầng) 4 tầng để đảm bảo tính tổ chức và dễ dàng mở rộng các tính năng quản lý:

- Presentation Layer:
    - React JS: Xử lý hiển thị và tương tác người dùng.
- API & Security Layer (Giao tiếp & Bảo mật)
    - Spring MVC: Đóng vai trò bộ khung điều hướng (Controller), tiếp nhận request và trả về dữ liệu chuẩn RESTful API.
    - Spring Security & JWT: Hệ thống bảo mật cấp "thẻ thông hành" (Token) để xác thực và phân quyền người dùng sau khi đăng nhập.
- Business Logic Layer:
    - Spring Service: Trung tâm xử lý logic lõi của ứng dụng (như tính toán tiến độ, kiểm tra hạn chót, xử lý quy trình di chuyển Task giữa các cột).
- Data Access Layer (Spring Data JPA / Hibernate)
    - Spring Data JPA (Hibernate): Thay vì dùng JDBC thủ công, JPA giúp ánh xạ trực tiếp các bảng MySQL thành đối tượng Java, giúp thao tác dữ liệu cực nhanh qua các hàm có sẵn.

## Lý do
1. Dễ hiểu và triển khai cho team
2. Tách biệt trách nhiệm (Separation of Concerns)
3. Dễ bảo trì và test
4. Phù hợp với quy mô dự án
## Hệ quả
Tích cực: Dễ bảo trì và viết unit test cho từng tầng riêng biệt. Cấu trúc rõ ràng giúp việc bàn giao hoặc thêm thành viên mới vào team thuận tiện.

Tiêu cực: Với các tác vụ cực kỳ đơn giản (như lấy tên một nhãn màu), việc phải đi qua cả 3 tầng có thể làm tăng thời gian viết code ban đầu.

## Các lựa chọn khác
- **Kiến trúc Monolithic không phân tầng rõ ràng:**
    1. Ưu điểm: Dễ bắt đầu, ít cấu trúc ban đầu, phù hợp cho prototype rất nhỏ.
    2. Nhược điểm: Khó bảo trì khi tính năng tăng; controller dễ chứa lẫn logic nghiệp vụ và truy cập dữ liệu; khó viết unit test tách biệt.

- **Microservices:**
    1. Ưu điểm: Tách biệt service theo domain; dễ scale từng thành phần độc lập khi hệ thống lớn.
    2. Nhược điểm: Quá phức tạp với team 3 người và thời gian 6 tuần; cần thêm hạ tầng service discovery, gateway, logging, monitoring và xử lý giao tiếp liên service.

- **Clean Architecture / Hexagonal Architecture:**
    1. Ưu điểm: Tách domain khỏi framework rất tốt; dễ thay đổi adapter như database, UI hoặc external service.
    2. Nhược điểm: Nhiều lớp abstraction hơn Layered Architecture; tăng thời gian setup và có thể vượt quá nhu cầu của dự án quy mô vừa.

## Ngày quyết định
2026-03-03
