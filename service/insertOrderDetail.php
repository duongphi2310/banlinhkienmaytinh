<?php
require 'KetNoiDatabase.php';
$code_order= $_POST["code_order"];
$code_product=$_POST["code_product"];
$price=$_POST["price"];
$quantity=$_POST["quantity"];
$total=$_POST["total"];
$query="INSERT INTO `chitiethoadon`(`MAHD`, `MALINHKIEN`, `DONGIA`, `SOLUONG`, `TONGITEN`) 
        VALUES ('$code_order','$code_product','$price','$quantity','$total')";
if(mysqli_query($connect,$query))
    echo 1;
else
    echo 0;