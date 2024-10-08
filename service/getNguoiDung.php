<?php
require 'KetNoiDatabase.php';
class TYPE
{
    function __construct($username, $name, $password, $diachi, $sodienthoai, $hinhanh, $idquyen, $trangthai)
    {
        $this->username = $username;
        $this->name = $name;
        $this->password = $password;
        $this->diachi = $diachi;
        $this->sodienthoai = $sodienthoai;
        $this->hinhanh = $hinhanh;
        $this->idquyen = $idquyen;
        $this->trangthai = $trangthai;
    }
}
$query = "select * from khachhang";
$data = mysqli_query($connect, $query);
$arraylist = array();
while ($row = mysqli_fetch_assoc($data)) {
    array_push($arraylist, new TYPE($row['USERNAME'], $row['TENKH'], $row['MATKHAU'], $row['DIACHI'], $row['SODT'], $row['HINHANH'], $row['IDQUYEN'], $row['TRANGTHAI']));
}echo json_encode($arraylist,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>