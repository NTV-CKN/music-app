- Ứng dụng nghe nhạc cho phép người dùng nghe và tạo playlist lưu bài hát, thêm bài hát yêu thích, ...
- Công nghệ sử dụng:
  + Phân trang bằng thư viện Paging
  + Thao tác luồng bằng Coroutines/Flow
  + BoundService cung cấp MediaController cho UI liên kết
  + Thư viện Hilt để tiêm các phụ thuộc
  + Thư viện Media3 dùng để phát nhạc và tạo playlist
  + Thư viện RoomDatabase hỗ trợ lưu bài hát từ remote, thao tác với Flow và thư viện Paging
  + Thư viện Retrofit/GsonConverter để kéo dữ liệu và chuyển đổi sang object
  + Thư viện Glide để load ảnh
  + Thư viện DataStore để xử lí thao tác lưu session trước đấy chơi nhạc khi quay lại ứng dụng
  + Thư viện Navigation hỗ trợ di chuyển và quản lí qua lại giữa các fragment và truyền nhận dữ liệu an toàn (Safe Args)
- Kiến trúc sử dụng: MVVM kết hợp Clean Architecture, viewBinding
