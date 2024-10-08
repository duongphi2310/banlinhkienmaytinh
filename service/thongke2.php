<?php
require 'KetNoiDatabase.php';

class LoaiLinhKien {
    public $mallinhkien;
    public $tenllinhkien;
    public $tongtienban;

    function __construct($mallinhkien, $tenllinhkien, $tongtienban) {
        $this->mallinhkien = $mallinhkien;
        $this->tenllinhkien = $tenllinhkien;
        $this->tongtienban = $tongtienban;
    }
}

$query = "
    SELECT llk.mallinhkien, llk.tenllinhkien, COALESCE(SUM(chd.dongia * chd.soluong), 0) AS tongtienban
    FROM loailinhkien llk
    LEFT JOIN linhkien lk ON llk.mallinhkien = lk.mallinhkien
    LEFT JOIN chitiethoadon chd ON lk.malinhkien = chd.malinhkien
    GROUP BY llk.mallinhkien, llk.tenllinhkien
";

$result = mysqli_query($connect, $query);

$loaiLinhKienList = array();

while ($row = mysqli_fetch_assoc($result)) {
    $loaiLinhKien = new LoaiLinhKien($row["mallinhkien"], $row["tenllinhkien"], $row["tongtienban"]);
    $loaiLinhKienList[] = $loaiLinhKien;
}

echo json_encode($loaiLinhKienList, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

mysqli_close($connect);
?>
