<?php
require 'KetNoiDatabase.php';

$code_order = $_GET["code_order"];

if(isset($code_order)) {
    $query = "UPDATE hoadon SET trangthai = 6 WHERE MAHD = '$code_order'";

    $result = mysqli_query($connect, $query);
    if($result) {
        echo json_encode(array("message" => "CHUYỂN ĐƠN SANG ĐÃ HỦY."), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    } else {
        echo json_encode(array("message" => "Có lỗi xảy ra."), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    }
} else {
    echo json_encode(array("message" => "Không tìm thấy mã đơn hàng."), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>