<?php
require 'KetNoiDatabase.php';
$maLinhKien = $_POST["maLinhKien"];
$tenLinhKien = $_POST["tenLinhKien"];
$giaTien = $_POST["giaTien"];
$giaTienKhuyenMai = $_POST["giaTienKhuyenMai"];
$soLuong = $_POST["soLuong"];
$moTa = $_POST["moTa"];
$image = $_POST["image"];
$maLoaiLinhKien = $_POST["maLoaiLinhKien"];

$query = "UPDATE linhkien SET TENLINHKIEN = '$tenLinhKien', GIATIEN = '$giaTien', SOLUONG = '$soLuong', GIATIENKHUYENMAI = '$giaTienKhuyenMai', MOTA = '$moTa', HINHANH = '$image', MALLINHKIEN = '$maLoaiLinhKien' WHERE MALINHKIEN = '$maLinhKien'";
if (mysqli_query($connect, $query)) {
    echo 1;
} else {
    echo -1;
}
?>
