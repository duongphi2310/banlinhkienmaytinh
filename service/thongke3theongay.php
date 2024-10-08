<?php
require 'KetNoiDatabase.php';

class LoaiLinhKien {
    public $mallinhkien;
    public $tenllinhkien;
    public $soluongban;
    public $phantram;

    function __construct($mallinhkien, $tenllinhkien, $soluongban, $phantram) {
        $this->mallinhkien = $mallinhkien;
        $this->tenllinhkien = $tenllinhkien;
        $this->soluongban = $soluongban;
        $this->phantram = $phantram;
    }
}
$ngay = $_GET['ngay'];


$query = "SELECT llk.mallinhkien, llk.tenllinhkien, COALESCE(SUM(chd.soluong), 0) AS soluongban
        FROM loailinhkien llk
        LEFT JOIN linhkien lk ON llk.mallinhkien = lk.mallinhkien
        LEFT JOIN chitiethoadon chd ON lk.malinhkien = chd.malinhkien
        LEFT JOIN hoadon hd ON chd.mahd = hd.mahd
        WHERE DATE(hd.ngaylaphoadon) = '$ngay'
        GROUP BY llk.mallinhkien, llk.tenllinhkien
    ";

// Thực thi truy vấn
$result = mysqli_query($connect, $query);

// Tính tổng số lượng của tất cả các linh kiện đã bán
$totalSold = 0;
while ($row = mysqli_fetch_assoc($result)) {
    $totalSold += $row["soluongban"];
}

// Truy vấn SQL lại để lấy ra số lượng đã bán của mỗi loại linh kiện và tính phần trăm
mysqli_data_seek($result, 0); // Đặt lại con trỏ kết quả về vị trí đầu tiên

$loaiLinhKienList = array();
while ($row = mysqli_fetch_assoc($result)) {
    $soluongban = $row["soluongban"];
    $phantram = ($totalSold > 0) ? round($soluongban / $totalSold * 100, 2) : 0;
    $loaiLinhKien = new LoaiLinhKien($row["mallinhkien"], $row["tenllinhkien"], $soluongban, $phantram);
    $loaiLinhKienList[] = $loaiLinhKien;
}

// Chuyển đổi mảng kết quả thành JSON và hiển thị
echo json_encode($loaiLinhKienList, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

// Đóng kết nối
mysqli_close($connect);
?>
