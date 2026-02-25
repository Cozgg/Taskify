```mermaid
C4Context
    title Sơ đồ Kiến trúc tổng quan - Trello Management
    
    Person(member, "Thành viên Team", "Quản lý thẻ, danh sách và cập nhật tiến độ công việc.")
    Person(manager, "Quản lý dự án", "Theo dõi trạng thái board và quản lý thành viên.")
    
    System(trello_system, "Hệ thống Trello", "Ứng dụng quản lý Kanban chạy trong môi trường Docker local.")
    
    System_Ext(auth_service, "Dịch vụ Xác thực", "Xác thực người dùng (có thể là Mock Auth hoặc OAuth local).")
    System_Ext(local_storage, "Hệ thống Lưu trữ Tệp", "Lưu trữ tệp đính kèm và tài liệu trực tiếp trên máy chủ local.")
    System_Ext(notification, "Dịch vụ Thông báo", "Gửi thông báo qua email hoặc log hệ thống.")
    
    Rel(member, trello_system, "Tương tác với task và board", "HTTPS/Web App")
    Rel(manager, trello_system, "Quản lý team và xem báo cáo", "HTTPS/Web App")
    
    Rel(trello_system, auth_service, "Xác thực danh tính qua", "API/OAuth2")
    Rel(trello_system, local_storage, "Lưu và tải tệp tin tại", "File System API")
    Rel(trello_system, notification, "Gửi thông báo cập nhật qua", "SMTP/API")
```
