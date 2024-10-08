<?php
require 'KetNoiDatabase.php';
// class CUSTOMER
// {
//     function __construct($username, $name, $password,$address,$phone,$image)
//     {
//         $this->username = $username;
//         $this->name = $name;
//         $this->password = $password;
//         $this->address = $address;
//         $this->phone = $phone;
//         $this->image = $image;
//     }
// }
$username=$_POST["username"];
$name=$_POST["name"];
$password=$_POST["password"];
$address=$_POST["address"];
$phone=$_POST["phone"];
// $image=$_POST["image"];

$query="select count(*) sl from khachhang where username='$username'";
$result=mysqli_query($connect,$query);
$row = mysqli_fetch_assoc($result);
if($row['sl']>0)
{
    echo 0;
}
else
{
    $query="INSERT INTO khachhang VALUES('$username','$name','$password','$address','$phone','staff.png')";
    if(mysqli_query($connect,$query))
    {
        echo 1;
    }
    else
        echo -1;

}

