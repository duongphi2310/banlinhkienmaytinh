<?php
require 'KetNoiDatabase.php';
class PRODUCT
{
    function __construct($code, $name, $price,$quantity,$price_discounted,$description,$image,$type_code)
    {
        $this->code = $code;
        $this->name = $name;
        $this->price = $price;
        $this->quantity = $quantity;
        $this->price_discounted = $price_discounted;
        $this->description = $description;
        $this->image = $image;
        
        $this->type_code = $type_code;
    }
}
$query = "select * from linhkien where 	GIATIENKHUYENMAI>0";
$data = mysqli_query($connect, $query);
$arraylist = array();
while ($row = mysqli_fetch_assoc($data)) {
    array_push($arraylist, new PRODUCT($row['MALINHKIEN'], $row['TENLINHKIEN'], $row['GIATIEN'], $row['SOLUONG'], $row['GIATIENKHUYENMAI'], $row['MOTA'], $row['HINHANH'], $row['MALLINHKIEN']));
}
echo json_encode($arraylist,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>