<?php
require 'KetNoiDatabase.php';
$username=$_POST["username"];
$pass_old=$_POST["pass_old"];
$pass_new=$_POST["pass_new"];
$query="select * from khachhang where username='$username' and matkhau='$pass_old'";
$data=mysqli_query($connect,$query);
$count=mysqli_num_rows($data);
if($count!=0)
{
   $query="UPDATE `khachhang` SET `MATKHAU`='$pass_new' WHERE `USERNAME`='$username'";
   if(mysqli_query($connect,$query))
      echo 1;
   else
       echo 0;
   
}
else
   echo -1;

