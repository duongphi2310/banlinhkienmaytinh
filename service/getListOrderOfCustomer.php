<?php
require 'KetNoiDatabase.php';
class ORDER{
    function __construct($code, $username, $total, $create_date, $status)
    {
        $this->code=$code;
        $this->username=$username;
        $this->total=$total;
        $this->create_date=$create_date;
        $this->status=$status;
    }
}
$username=$_GET["username"];
if(isset($username))
{
    $query="select *from hoadon where MAKH='$username'";
  $data=mysqli_query($connect,$query);
  $arrayList=array();
  while($row=mysqli_fetch_assoc($data))
        array_push($arrayList, new ORDER($row["MAHD"], $row["MAKH"], $row["TONGTIEN"], $row["NGAYLAPHOADON"], $row["TRANGTHAI"]));
  echo json_encode($arrayList,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}


