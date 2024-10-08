<?php
require "./api/KetNoiDatabase.php";
   class LoaiLinhKien{

    function __construct($code,$tenlinhkien,$img){
        $this->code=$code;
        $this->tenlinhkien=$tenlinhkien;
        $this->img=$img;
    }

    function loadLoaiSanPham(){
        $array=array();
        $db=new Database();
        $connect=$db->getDB();
        $query="select * from loailinhkien";
            $data=mysqli_query($connect,$query);
    
            $array=array();
        while($row = mysqli_fetch_assoc($data)){
          array_push($array,new LoaiLinhKien($row['MALLINHKIEN'],$row['TENLLINHKIEN'],$row['HINHANH']));
        }
        return $array;

    }

    function addLoaiLinhKien($loailinhkiens){
        $db=new Database();
        $connect=$db->getDB();
$query="Insert into loailinhkien (`TENLLINHKIEN`, `HINHANH`) values ('$loailinhkiens->tenlinhkien','$loailinhkiens->img')";
    $result=mysqli_query($connect,$query);
    if($result)
    return 1;
    else
    return 0;
    }

    function delLoaiLinhKien($maloai){
        $db=new Database();
        $connect=$db->getDB();
        $query="delete from loailinhkien where MALLINHKIEN=$maloai";
        $result=mysqli_query($connect,$query);
        if($result)
        return 1;
        return 0;
    }

}
?>