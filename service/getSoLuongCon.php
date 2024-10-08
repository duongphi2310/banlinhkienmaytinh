<?php
require 'KetNoiDatabase.php';

if (isset($_GET['maLinhKien'])) {
    $maLinhKien = $_GET['maLinhKien'];

    $query = "SELECT SOLUONG FROM linhkien WHERE MALINHKIEN = ?";
    $stmt = $connect->prepare($query);
    $stmt->bind_param("i", $maLinhKien); // i là kiểu dữ liệu của MALINHKIEN (integer)
    $stmt->execute();
    $stmt->bind_result($soluong);

    $response = array();

    if ($stmt->fetch()) {
        $response['SOLUONG'] = $soluong;
    } else {
        $response['message'] = 'Không tìm thấy sản phẩm với MALINHKIEN đã cung cấp.';
    }

    echo json_encode($response, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
} else {
    echo json_encode(array('message' => 'Không có MALINHKIEN được cung cấp'), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>
