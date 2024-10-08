<?php
require 'KetNoiDatabase.php';

$username = $_POST["username"];
$productCodes = json_decode($_POST["productCodes"]);
$productCodesString = implode(",", $productCodes);

$query = "DELETE FROM giohang WHERE USERNAME = '$username' AND MALINHKIEN IN ($productCodesString)";
$result = mysqli_query($connect, $query);
$response = array();

if ($result) {
    $response['success'] = true;
} else {
    $response['success'] = false;
}

echo json_encode($response);
mysqli_close($connect);
?>