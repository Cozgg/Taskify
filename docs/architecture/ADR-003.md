# Bản ghi Quyết định Kiến trúc

---

## ADR-003: [Sử dụng Message Queue (RabbitMQ) cho xử lý bất đồng bộ]

**Trạng thái**: [Đã chấp nhận]  
**Ngày**: 2026-02-25  
**Người quyết định**: [Nguyễn Hữu Công - Thành viên dự án]  
**Tags**: [công nghệ]

---

## Ngữ cảnh

**Vấn đề kỹ thuật**: Hệ thống cần thực hiện các tác vụ tốn thời gian như gửi email thông báo khi thành viên được gán vào task hoặc khi cập nhật trạng thái thẻ công việc.

**Hạn chế hiện tại**: Nếu xử lý các tác vụ này đồng bộ trong luồng request-response của API, người dùng sẽ gặp tình trạng treo ứng dụng hoặc phản hồi chậm, làm giảm trải nghiệm người dùng.

**Yêu cầu đồ án**: Theo mục IV, yêu cầu kỹ thuật số 12 của đồ án bắt buộc phải áp dụng RabbitMQ cho ít nhất 1 use case để xử lý bất đồng bộ.

**Bất kỳ ràng buộc nào**: Nhóm cần hoàn thành dự án trong 8 tuần và hệ thống phải chạy được hoàn toàn trong môi trường Docker Compose.

---

## Quyết định

Chúng tôi quyết định sử dụng RabbitMQ làm hệ thống Message Broker để quản lý hàng đợi tin nhắn cho các tác vụ thông báo và gửi email. 
Hệ thống sẽ tách rời logic nghiệp vụ chính (quản lý task) và logic thông báo thành các dịch vụ độc lập.
API Server sẽ đóng vai trò là Producer đẩy tin nhắn vào hàng đợi, và một Worker Service sẽ đóng vai trò là Consumer để xử lý việc gửi email/thông báo.

---

## Các Tùy chọn Đã Xem xét

**Những lựa chọn thay thế nào chúng ta đã xem xét?**

Liệt kê các tùy chọn đã được đánh giá, với ưu và nhược điểm ngắn gọn cho mỗi tùy chọn.

### Tùy chọn 1: [Xử lí đồng bộ (Synchronous)]

**Ưu điểm:**
- Dễ dàng triển khai, không yêu cầu thêm hạ tầng.

**Nhược điểm:**
- API phản hồi chậm, không đáp ứng được yêu cầu bắt buộc của đề tài về sử dụng Message Queue.

### Tùy chọn 2: [Sử dụng Redis (Pub/Sub)]

**Ưu điểm:**
- Tốc độ nhanh, đơn giản triển khai.

**Nhược điểm:**
- Không đảm bảo được tin nhắn lưu trữ bền vững nếu subscriber offline (Khi thành viên trong nhóm không hoạt động).

### Tùy chọn 3: [RabbitMQ]

**Ưu điểm:**
- Hỗ trợ hàng đợi tin nhắn bền vững, đảm bảo tin nhắn không bị mất, hỗ trợ tốt cho việc mở rộng dự án.

**Nhược điểm:**
- Tăng độ phức tạp của hệ thống.

---

## Kết quả Quyết định

Chúng tôi chọn RabbitMQ vì đây là yêu cầu kỹ thuật trọng tâm.
Việc sử dụng RabbitMQ giúp hệ thống đạt được tính tách rời (decoupling),
tăng khả năng chịu tải và đảm bảo API luôn phản hồi nhanh chóng cho người dùng

---

## Hậu quả

**Những tác động tích cực và tiêu cực của quyết định này là gì?**

### Tích cực

- **Cải thiện hiệu suất**: Giảm thời gian phản hồi của các API chính (như tạo task, cập nhật board).
- **Tăng độ ổn định**: Nếu dịch vụ email gặp sự cố, tin nhắn vẫn được lưu trong hàng đợi RabbitMQ và sẽ được xử lý lại sau khi dịch vụ hồi phục.
- **Module hóa**: Tách biệt rõ ràng chức năng nghiệp vụ và chức năng thông báo.

### Tiêu cực

- **Phức tạp hóa hạ tầng**: Cần cấu hình thêm container RabbitMQ và quản lý kết nối trong file docker-compose.yml.
- **Tốn tài nguyên**: Chạy thêm một dịch vụ Message Broker trên máy local yêu cầu thêm bộ nhớ RAM.

### Trung tính / Ghi chú

- Cần triển khai cơ chế Retry cho Worker khi việc gửi email gặp lỗi.
- Cần bổ sung giao diện quản lý RabbitMQ (Management Plugin) để theo dõi trạng thái hàng đợi.

---

## Tham khảo

- [Liên kết đến ADR liên quan](#)
- [Tài nguyên hoặc tài liệu bên ngoài](https://github.com/liemqv/SAD-Solution-Architecture-Document-Best-Practices/edit/main/vi-VN/ADR-Template.md)
- [Quyết định hoặc pattern liên quan](#)

---

## Ghi chú

---

**Cập nhật lần cuối**: 2026-02-25  
**ADRs Liên quan**: [ADR-XXX](#), [ADR-YYY](#)
