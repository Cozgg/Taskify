# ADR-006: Chiến lược CI/CD với GitHub Actions và GHCR
## Trạng thái
Accepted

## Bối cảnh
Với việc đã container hóa ứng dụng (ADR-005), chúng tôi cần một quy trình tự động để kiểm tra mã nguồn (Continuous Integration) và đóng gói/phát hành ứng dụng (Continuous Deployment). 

Mục tiêu là giảm thiểu lỗi con người khi build thủ công và đảm bảo rằng mọi phiên bản code được đẩy lên nhánh chính đều sẵn sàng để triển khai.

## Quyết định
Sử dụng **GitHub Actions** làm nền tảng CI/CD và **GitHub Container Registry (GHCR)** làm nơi lưu trữ Docker Images:

1.  **Pipeline Đa tầng (Multi-job):**
    -   **Job 1 (Backend):** Sử dụng Maven để build và kiểm tra lỗi logic.
    -   **Job 2 (Frontend):** Sử dụng Node.js để build code React.
    -   **Job 3 (Docker Check):** Chỉ chạy khi 2 job trên thành công, thực hiện build thử image để kiểm tra tính toàn vẹn của Dockerfile.
2.  **Tự động hóa Deployment (CD):**
    -   Mỗi khi có commit được đẩy vào nhánh `main` hoặc `cozg`, hệ thống sẽ tự động build image chính thức và gắn tag `latest`.
    -   Đẩy image lên **ghcr.io** (GitHub Container Registry).
3.  **Bảo mật:** Sử dụng `GITHUB_TOKEN` có sẵn của GitHub Actions để xác thực với Registry, không cần cấu hình thêm secret thủ công cho việc push image nội bộ.

## Lý do
1.  **Tích hợp tốt nhất:** Vì mã nguồn đã nằm trên GitHub, việc dùng Actions giúp quản lý mọi thứ tập trung tại một nơi.
2.  **Chi phí:** Miễn phí cho các dự án mã nguồn mở và repo công khai.
3.  **Tốc độ:** GitHub cung cấp các runner cấu hình mạnh, giúp việc build Docker image diễn ra nhanh chóng.
4.  **Tính sẵn sàng:** GHCR giúp việc quản lý phiên bản image trở nên chuyên nghiệp, hỗ trợ tốt cho việc kéo (pull) image về server sau này.

## Hệ quả
**Tích cực:** 
- Tự động hóa hoàn toàn quy trình kiểm tra và đóng gói.
- Luôn có sẵn bản build "sạch" mới nhất trên Registry.
- Dễ dàng mở rộng để deploy tự động lên VPS/Cloud trong tương lai.

**Tiêu cực:** 
- Phụ thuộc vào hạ tầng của GitHub (nếu GitHub Actions gặp sự cố, quy trình build sẽ bị gián đoạn).
- Cần quản lý dung lượng lưu trữ của Packages nếu project phát triển quá lớn.

## Các lựa chọn khác
- **Jenkins:**
    1. Ưu điểm: Khả năng tùy biến cực cao, tự host (không phụ thuộc cloud).
    2. Nhược điểm: Tốn tài nguyên duy trì server; cấu hình phức tạp hơn GitHub Actions.

- **Triển khai thủ công (Manual):**
    1. Ưu điểm: Không tốn thời gian viết script CI/CD ban đầu.
    2. Nhược điểm: Dễ sai sót; tốn thời gian lặp lại; không kiểm tra được code liên tục.

## Ngày quyết định
2026-04-20
