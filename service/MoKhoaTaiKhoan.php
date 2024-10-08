<?php
require 'KetNoiDatabase.php';

$username = $_POST['username'];
$query = "UPDATE khachhang SET TRANGTHAI = 1 WHERE USERNAME = '$username'";
$result = mysqli_query($connect, $query);
if ($result) {
    $response = array("success" => true);
} else {
    $response = array("success" => false);
}
echo json_encode($response);
mysqli_close($connect);
?>
