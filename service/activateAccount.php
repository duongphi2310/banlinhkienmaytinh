<?php
require 'KetNoiDatabase.php';
if (isset($_GET['email'])) 
    $email = $_GET['email'];
else 
    return 0;

$query="UPDATE `khachhang` SET TRANGTHAI = 1 WHERE email = '$email'";
if(mysqli_query($connect,$query))
      echo "Thanks for your register! Your account is already activated!";
else
       echo 2;

