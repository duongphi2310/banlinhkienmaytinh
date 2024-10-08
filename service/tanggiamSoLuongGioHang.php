<?php
require 'KetNoiDatabase.php';
$maLinhKien = $_POST["maLinhKien"];
$soLuongHienTai = $_POST["soLuongHienTai"]; // Sửa thành soLuongHienTai
$soLuongMoi = $_POST["soLuongMoi"]; // Lấy soLuongMoi từ $_POST

$query_update = "UPDATE giohang SET SOLUONG = '$soLuongMoi' WHERE MALINHKIEN = '$maLinhKien'";

if (mysqli_query($connect, $query_update)) {
    echo json_encode(array("success" => true, "message" => "Cập nhật số lượng thành công"));
} else {
    echo json_encode(array("success" => false, "message" => "Cập nhật số lượng thất bại"));
}
?>
