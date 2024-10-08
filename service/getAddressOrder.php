<?php
require 'KetNoiDatabase.php';

class ORDER {
    function __construct($ten, $diachi, $sodienthoai) {
        $this->ten = $ten;
        $this->diachi = $diachi;
        $this->sodienthoai = $sodienthoai;
    }
}

$mahd = $_GET["mahd"];
if (isset($mahd)) {
    $query = "SELECT TEN, DIACHI, SODIENTHOAI FROM hoadon WHERE MAHD = '$mahd'";
    $data = mysqli_query($connect, $query);
    $arrayList = array();
    while ($row = mysqli_fetch_assoc($data)) {
        array_push($arrayList, new ORDER(
            $row["TEN"], 
            $row["DIACHI"], 
            $row["SODIENTHOAI"]
        ));
    }
    echo json_encode($arrayList, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}
?>
