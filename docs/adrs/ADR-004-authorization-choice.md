# ADR-004: Lựa chọn công nghệ xác thực người dùng

## Trạng thái
Accepted

## Bối cảnh
Hệ thống Trello Clone (quản lý công việc) yêu cầu cơ chế xác thực người dùng để:
-Bảo vệ các tài nguyên như workspace, board, card
-Phân quyền người dùng (owner, member, admin)
-Hỗ trợ frontend (React) giao tiếp với backend (Spring MVC) thông qua REST API
-Đảm bảo hiệu năng và khả năng mở rộng trong phạm vi project (8 tuần, team 3 người)
Hệ thống hoạt động theo mô hình SPA (Single Page Application), do đó cần một cơ chế xác thực phù hợp với kiến trúc stateless.

## Quyết định
Sử dụng JWT (JSON Web Token) để xác thực người dùng.

## Lý do
1. Stateless (không lưu session): JWT không cần lưu session phía server ,phù hợp với RESTful API giảm tải cho backend
2. Dễ tích hợp với React: Token được lưu ở client (cookie hoặc localStorage) ,gửi kèm mỗi request qua header: "Authorization: Bearer <token>"
3. Phù hợp với Spring Boot: Spring Security hỗ trợ JWT tốt ,dễ implement filter để kiểm tra token
4. Hiệu năng tốt: Không cần truy vấn database mỗi lần xác thực, decode token là đủ để lấy thông tin user
5. Phù hợp quy mô project: Dễ triển khai nhanh trong thời gian ngắn, không quá phức tạp như OAuth2

## Hệ quả
1. Ưu điểm: Giảm tải server do không dùng session, dễ mở rộng (microservices, mobile app) ,frontend và backend tách biệt rõ ràng, triển khai nhanh, phù hợp deadline
2. Nhược điểm: Khó revoke token (không logout ngay lập tức), token có thể bị lộ nếu lưu không an toàn, cần xử lý thêm refresh token nếu muốn bảo mật cao

## Các lựa chọn khác
-OAuth2
1. Ưu điểm: Bảo mật cao, hỗ trợ đăng nhập qua Google, Facebook
2. Nhược điểm: Phức tạp ,overkill cho project nhỏ, tốn thời gian implement

## Ngày quyết định
2026-03-03
