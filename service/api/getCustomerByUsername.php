<?php
require 'KetNoiDatabase.php';
class CUSTOMER
{
    function __construct($username, $name,$address,$phone,$image)
    {
        $this->username = $username;
        $this->name = $name;
        $this->address = $address;
        $this->phone = $phone;
        $this->image = $image;
    }
}
$username=$_GET['username'];
if(isset($username))
{
    $query="select * from khachhang where username='$username'";
    $result=mysqli_query($connect,$query);
    $row = mysqli_fetch_assoc($result);
    $customer=new CUSTOMER($row["USERNAME"],$row["TENKH"],$row["DIACHI"],$row["SODT"],$row["HINHANH"]);
    echo json_encode($customer,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}