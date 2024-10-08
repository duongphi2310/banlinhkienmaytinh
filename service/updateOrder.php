<?php
require 'KetNoiDatabase.php';
$total=$_POST["total"];
$code_order=$_POST["code_order"];
$query="UPDATE `hoadon` SET `TONGTIEN`='$total' WHERE `MAHD`='$code_order'";
if(mysqli_query($connect,$query))
   echo 1;
else
    echo 0;