<?php
require 'KetNoiDatabase.php';
$tenLinhKien = $_POST["tenLinhKien"];
$giaTien = $_POST["giaTien"];
$giaTienKhuyenMai = $_POST["giaTienKhuyenMai"];
$soLuong = $_POST["soLuong"];
$moTa = $_POST["moTa"];
$image = $_POST["image"];
$maLoaiLinhKien = $_POST["maLoaiLinhKien"];

$query = "INSERT INTO linhkien (TENLINHKIEN, GIATIEN, SOLUONG, GIATIENKHUYENMAI, MOTA, HINHANH, MALLINHKIEN) VALUES ('$tenLinhKien', '$giaTien', '$soLuong', '$giaTienKhuyenMai', '$moTa', '$image', '$maLoaiLinhKien')";
if (mysqli_query($connect, $query)) {
    echo 1;
} else {
    echo -1;
}
?>
