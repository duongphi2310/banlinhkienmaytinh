<?php
require 'KetNoiDatabase.php';
$username=$_POST["username"];
$password=$_POST["password"];
$query="select username from khachhang where username='$username' and matkhau='$password'";
$result=mysqli_query($connect,$query);
$row = mysqli_fetch_assoc($result);
echo !isset($row['username'])?"fail":$row['username'];