<?php
require 'KetNoiDatabase.php';

$username = $_POST["username"];
$password = $_POST["password"];

$query = "SELECT * FROM khachhang WHERE username='$username' AND matkhau='$password'";
$result = mysqli_query($connect, $query);

if (mysqli_num_rows($result) == 0) {
    echo "not_exist"; // Không tồn tại tài khoản trong CSDL
} else {
    $row = mysqli_fetch_assoc($result);
    $trangthai = $row['TRANGTHAI'];
    $idquyen = $row['IDQUYEN']; // Lấy idquyen của người dùng

    if ($trangthai == 1) {
        echo "active-" . $row['USERNAME'] . "-" . $idquyen; // Tài khoản đang hoạt động
    } elseif ($trangthai == -1) {
        echo "locked-" . $row['USERNAME'] . "-" . $idquyen; // Tài khoản đã bị khóa
    } else {
        echo "inactive-" . $row['USERNAME'] . "-" . $idquyen; // Tài khoản chưa kích hoạt
    }
}
?>
