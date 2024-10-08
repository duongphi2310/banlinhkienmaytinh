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
$keyword=$_GET['keyword'];
if(isset($keyword))
{
    $query = "select linhkien.* from linhkien,loailinhkien where linhkien.MALLINHKIEN=loailinhkien.MALLINHKIEN AND (linhkien.TENLINHKIEN LIKE '%$keyword%' or loailinhkien.TENLLINHKIEN like '%$keyword%' or linhkien.MOTA like '%$keyword%')";
    $data = mysqli_query($connect, $query);
    $arraylist = array();
    while ($row = mysqli_fetch_assoc($data)) {
        array_push($arraylist, new PRODUCT($row['MALINHKIEN'], $row['TENLINHKIEN'], $row['GIATIEN'], $row['SOLUONG'], $row['GIATIENKHUYENMAI'], $row['MOTA'], $row['HINHANH'], $row['MALLINHKIEN']));
}
echo json_encode($arraylist,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

}
?>