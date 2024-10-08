<?php
require 'KetNoiDatabase.php';
$maLinhKien = $_POST["maLinhKien"];
$tenLinhKien = $_POST["tenLinhKien"];
$image = $_POST["image"];

$query = "UPDATE loailinhkien SET TENLLINHKIEN = '$tenLinhKien', HINHANH = '$image' WHERE MALLINHKIEN = '$maLinhKien'";
if (mysqli_query($connect, $query)) {
    echo 1;
} else {
    echo -1;
}
?>
