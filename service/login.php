<?php
require 'KetNoiDatabase.php';
$username=$_POST["username"];
$query="select * from khachhang where username='$username'";
$result=mysqli_query($connect,$query);
$row = mysqli_fetch_assoc($result);
echo $row['TRANGTHAI'];