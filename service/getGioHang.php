<?php
require 'KetNoiDatabase.php';
$username = $_POST["username"];

$query = "SELECT giohang.ID, giohang.MALINHKIEN, giohang.SOLUONG, linhkien.TENLINHKIEN, linhkien.HINHANH, 
            CASE 
                WHEN linhkien.GIATIENKHUYENMAI > 0 THEN linhkien.GIATIENKHUYENMAI
                ELSE linhkien.GIATIEN
            END AS GIATIEN, linhkien.HINHANH AS HINHANH1
            FROM giohang 
            INNER JOIN linhkien ON giohang.MALINHKIEN = linhkien.MALINHKIEN 
            WHERE giohang.USERNAME = '$username'";
$result = mysqli_query($connect, $query);
$response = array();

if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $item = array(
            'ID' => $row['ID'],
            'maLinhKien' => $row['MALINHKIEN'],
            'soLuong' => $row['SOLUONG'],
            'tenLinhKien' => $row['TENLINHKIEN'],
            'hinhAnh' => $row['HINHANH1'],
            'gia' => $row['GIATIEN']
        );
        array_push($response, $item);
    }
    echo json_encode($response);
} else {
    echo json_encode($response);
}

mysqli_close($connect);
?>