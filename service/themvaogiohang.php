<?php
require 'KetNoiDatabase.php';
$maLinhKien = $_POST["maLinhKien"];
$username   = $_POST["username"];
$soLuong    = $_POST["soLuong"];

$query_check = "SELECT * FROM giohang WHERE MALINHKIEN = '$maLinhKien' AND USERNAME = '$username'";
$result_check = mysqli_query($connect, $query_check);

if (mysqli_num_rows($result_check) > 0) {
    $row = mysqli_fetch_assoc($result_check);
    $soLuongMoi = $row['SOLUONG'] + $soLuong;
    $query_update = "UPDATE giohang SET SOLUONG = '$soLuongMoi' WHERE MALINHKIEN = '$maLinhKien' AND USERNAME = '$username'";
    if (mysqli_query($connect, $query_update)) {
        echo 1;
    } else {
        echo -1;
    }
} else {
    $query_insert = "INSERT INTO giohang (MALINHKIEN, USERNAME, SOLUONG) VALUES ('$maLinhKien', '$username', '$soLuong')";
    if (mysqli_query($connect, $query_insert)) {
        echo 1; 
    } else {
        echo -1; 
    }
}
?>
