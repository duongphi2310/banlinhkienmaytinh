<?php
require 'KetNoiDatabase.php';
$maLinhKien = $_POST["maLinhKien"];

$query = "DELETE FROM giohang WHERE MALINHKIEN = '$maLinhKien'";
$result = mysqli_query($connect, $query);
$response = array();

if ($result) {
    $response['success'] = true;
    $response['message'] = "XÓA LINH KIỆN THÀNH CÔNG";
} else {
    $response['success'] = false;
    $response['message'] = "XÓA LINH KIỆN THẤT BẠI";
}

echo json_encode($response);
mysqli_close($connect);
?>