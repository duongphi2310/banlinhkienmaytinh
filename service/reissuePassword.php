<?php
require 'KetNoiDatabase.php';
$email=$_POST["email"];
$pass_new=$_POST["pass_new"];
$query="select * from khachhang where email='$email'";
$data=mysqli_query($connect,$query);
$count=mysqli_num_rows($data);
if($count!=0)
{
   $query="UPDATE `khachhang` SET `MATKHAU`='$pass_new' WHERE `EMAIL`='$email'";
   if(mysqli_query($connect,$query))
      echo 1;
   else
       echo 0;
   
}
else
   echo -1;