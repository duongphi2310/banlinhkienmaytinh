<?php
require 'KetNoiDatabase.php';

class LoaiLinhKien {
    public $mallinhkien;
    public $tenllinhkien;
    public $tongtienban;

    function __construct($mallinhkien, $tenllinhkien, $tongtienban) {
        $this->mallinhkien = $mallinhkien;
        $this->tenllinhkien = $tenllinhkien;
        $this->tongtienban = $tongtienban;
    }
}

$ngay = $_GET['ngay'];


$query = "
    SELECT llk.mallinhkien, llk.tenllinhkien, COALESCE(SUM(chd.dongia * chd.soluong), 0) AS tongtienban
    FROM loailinhkien llk
    LEFT JOIN linhkien lk ON llk.mallinhkien = lk.mallinhkien
    LEFT JOIN chitiethoadon chd ON lk.malinhkien = chd.malinhkien
    LEFT JOIN hoadon hd ON chd.mahd = hd.mahd
    WHERE DATE(hd.ngaylaphoadon) = '$ngay'
    GROUP BY llk.mallinhkien, llk.tenllinhkien
";

// Thực thi truy vấn
$result = mysqli_query($connect, $query);

// Mảng để lưu trữ kết quả
$loaiLinhKienList = array();

// Duyệt qua từng dòng kết quả và tạo đối tượng LoaiLinhKien
while ($row = mysqli_fetch_assoc($result)) {
    $loaiLinhKien = new LoaiLinhKien($row["mallinhkien"], $row["tenllinhkien"], $row["tongtienban"]);
    $loaiLinhKienList[] = $loaiLinhKien;
}

// Chuyển đổi mảng kết quả thành JSON và hiển thị
echo json_encode($loaiLinhKienList, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

mysqli_close($connect);
?>
