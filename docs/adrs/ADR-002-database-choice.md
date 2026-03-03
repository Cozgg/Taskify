# ADR-002: Lựa chọn hệ quản trị cơ sở dữ liệu (Database)

## Trạng thái
Accepted

## Bối cảnh
Hệ thống Trello Clone (quản lý công việc) có các thực thể dữ liệu liên kết rất chặt chẽ với nhau. Với kiến trúc Backend là Java (Spring Boot) và Frontend là React, chúng tôi cần một hệ quản trị cơ sở dữ liệu phù hợp để lưu trữ, đảm bảo tính toàn vẹn dữ liệu và dễ dàng truy vấn các mối quan hệ này trong thời gian phát triển 6 tuần của team 4 người.

## Quyết định
Sử dụng Cơ sở dữ liệu quan hệ (RDBMS) - cụ thể là MySQL.

## Lý do
1. Phù hợp với bản chất dữ liệu có tính quan hệ cao của hệ thống.
2. Xử lý đồng thời và nhất quán.
3. Tối ưu hóa thời gian phát triển.
4. Khả năng mở rộng linh hoạt.
## Hệ quả
- Tích cực: Dữ liệu nhất quán, an toàn. Dễ dàng thực hiện các truy vấn phức tạp (join) để lấy toàn bộ dữ liệu của một Board.
- Tiêu cực: Đòi hỏi thiết kế lược đồ (Schema) kỹ lưỡng từ đầu; việc thay đổi cấu trúc bảng sau này tốn nhiều công sức hơn NoSQL.

## Ngày quyết định
2026-03-03