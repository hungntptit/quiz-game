- server:
	- mỗi 1 giây gửi thời gian còn lại, xếp hạng.
	- chỉ ghi file cho client 1 lần, kết thúc sớm hoặc ghi lúc hết giờ.
	- gửi nhận object Map<Interger, Question> thay bằng gửi nhận json
- client:
	- ...
- thêm web:
	- web server giao tiếp với tcp server
	- tcp client socket được quản lý tại web server. mỗi khi có request đến "/" tạo 1 socket và gán cho client id.
	- trình duyệt gửi request lấy các thông tin, gửi cập nhật điểm, thoát, đến web server. web server xử lí.

		
