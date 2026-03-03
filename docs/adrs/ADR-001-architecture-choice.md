# ADR-001: Lựa chọn kiến trúc Layered
## Trạng thái
Accepted

## Bối cảnh
Chúng tôi cần xây dựng hệ thống quản lý công việc theo dạng Kanban board (Trello Clone) với các tính năng: quản lý workspace, board, task, gán thành viên, bình luận và thống kê.

Hệ thống có quy mô vừa, team 4 người, thời gian phát triển 6 tuần.

Đối tượng sử dụng (Actors): Thành viên, Quản lý dự án, Admin.

## Quyết định
Sử dụng kiến trúc Layered (Phân tầng) 3 tầng để đảm bảo tính tổ chức và dễ dàng mở rộng các tính năng quản lý:

- Presentation Layer (React)

- Business Logic Layer (Java - Spring Boot)

- Data Access Layer (Spring Data JPA / Hibernate)

## Lý do
1. Dễ hiểu và triển khai cho team
2. Tách biệt trách nhiệm (Separation of Concerns)
3. Dễ bảo trì và test
4. Phù hợp với quy mô dự án
## Hệ quả
Tích cực: Dễ bảo trì và viết unit test cho từng tầng riêng biệt. Cấu trúc rõ ràng giúp việc bàn giao hoặc thêm thành viên mới vào team thuận tiện.

Tiêu cực: Với các tác vụ cực kỳ đơn giản (như lấy tên một nhãn màu), việc phải đi qua cả 3 tầng có thể làm tăng thời gian viết code ban đầu.

## Ngày quyết định
2026-03-03