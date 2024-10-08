<?php
require 'KetNoiDatabase.php';
$tenLinhKien = $_POST["tenLinhKien"];
$image = $_POST["image"];

$query = "INSERT INTO loailinhkien (TENLLINHKIEN, HINHANH) VALUES ('$tenLinhKien', '$image')";
if (mysqli_query($connect, $query)) {
    echo 1;
} else {
    echo -1;
}
?>
