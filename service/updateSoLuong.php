<?php
require 'KetNoiDatabase.php';

$code_order = $_GET["code_order"];

if(isset($code_order)) {
    $query = "SELECT malinhkien, soluong FROM chitiethoadon WHERE mahd = '$code_order'";
    $result = mysqli_query($connect, $query);
    if($result) {
        while($row = mysqli_fetch_assoc($result)) {
            $malinhkien = $row["malinhkien"];
            $soluong = $row["soluong"];
            
            $updateQuery = "UPDATE linhkien SET soluong = soluong + $soluong WHERE malinhkien = '$malinhkien'";
            mysqli_query($connect, $updateQuery);
        }
        echo json_encode(array("message" => "Cập nhật kho thành công."), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    } else {
        echo json_encode(array("message" => "Có lỗi xảy ra, không thể cập nhật kho."), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    }
} else {
    echo json_encode(array("message" => "Không tìm thấy mã đơn hàng."), JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>