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
    $trangthai = $row['trangthai'];
    $idquyen = $row['idquyen'];
    if ($trangthai == 1) {
        // Tài khoản đang hoạt động
        echo "active-" . $row['username'] . "-role-" . $idquyen;
    } elseif ($trangthai == -1) {
        // Tài khoản đã bị khóa
        echo "locked-" . $row['username'] . "-role-" . $idquyen;
    } else {
        // Tài khoản chưa kích hoạt
        echo "inactive-" . $row['username'] . "-role-" . $idquyen;
    }
}

mysqli_close($connect);
?>
