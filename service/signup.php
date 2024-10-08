<?php
require 'KetNoiDatabase.php';

$username=$_POST["username"];
$name=$_POST["name"];
$password=$_POST["password"];
$address=$_POST["address"];
$phone=(int)($_POST["phone"]);
$image=$_POST["image"];
$email=$_POST["email"];
$idquyen=$_POST["idquyen"];
$status=$_POST["status"];

$query_username = "SELECT COUNT(*) AS username_count FROM khachhang WHERE username='$username'";
$query_email = "SELECT COUNT(*) AS email_count FROM khachhang WHERE email='$email'";

$result_username=mysqli_query($connect,$query_username);
$result_email=mysqli_query($connect,$query_email);

$row_username = mysqli_fetch_assoc($result_username);
$row_email = mysqli_fetch_assoc($result_email);

if($row_username['username_count'] > 0) {
    echo -1;
} elseif ($row_email['email_count'] > 0) {
    echo -2;
} else {
    $query = "INSERT INTO khachhang VALUE('$username','$name','$password','$address','$phone','$image','$email', '$idquyen', '$status');";
    if(mysqli_query($connect,$query)) {
        echo "1";
    } else {
        echo 0;
    }
}
?>
