<?php
require 'KetNoiDatabase.php';
$username = $_POST["username"];
$ten = $_POST["ten"];
$sodienthoai = $_POST["sodienthoai"];
$diachi = $_POST["diachi"];

// Lưu vào bảng hoadon
$query = "INSERT INTO `hoadon`(`MAKH`, `TONGTIEN`, `NGAYLAPHOADON`, `ten`, `diachi`, `sodienthoai`) VALUES ('$username', 0, CURRENT_DATE, '$ten', '$diachi', '$sodienthoai')";

if (mysqli_query($connect, $query)) {
    $query = "SELECT MAHD FROM hoadon ORDER BY MAHD DESC LIMIT 1";
    $data = mysqli_query($connect, $query);
    $count = mysqli_num_rows($data);
    if ($count != 0) {
        $row = mysqli_fetch_assoc($data);
        echo $row["MAHD"];
    } else {
        echo 0;
    }
} else {
    echo 0;
}
?>
