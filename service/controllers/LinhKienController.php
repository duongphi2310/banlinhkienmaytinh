<?php
    require "./model/LOAILINHKIEN.php";
    require "./model/LINHKIEN.php";
class LinhKienController extends Controller{
public function Index(){
    $LinhKien=new LinhKien("","","","","","","","","");
    $data['dulieu']=$LinhKien->loadLinhKien();
    $LoaiLinhKien=new LoaiLinhKien("","","");
    $data['loailinhkien']=$LoaiLinhKien->loadLoaiSanPham();
    $this->View("LinhKien",$data);
}

public function Insert(){

$target_file="";
if(isset($_FILES['hinhanh'])){
    $file_name = $_FILES['hinhanh']['name'];
      $file_type=$_FILES['hinhanh']['type'];
      $file_tmp =$_FILES['hinhanh']['tmp_name'];

      $target_dir = "assets/hinhanh/";
    $target_file = $target_dir . basename($_FILES["hinhanh"]["name"]);
echo $target_file;
      $file_ext=strtolower(end(explode('.',$_FILES['hinhanh']['name'])));

      $expensions= array("jpeg","jpg","png");

      if(in_array($file_ext,$expensions)=== false){
        $errors="extension not allowed, please choose a JPEG or PNG file.";
     }else{
        move_uploaded_file($file_tmp,$target_file);
     }
}
echo $_POST['loailinhkien'];
$LinhKien=new LinhKien(1,$_POST['tenlinhkien'],$_POST['giatien'],$_POST['soluong'],$_POST['giatienkhuyenmai'],$_POST['mota'],$file_name,$_POST['loailinhkien']);
$result=$LinhKien->addLinhKien($LinhKien);
if($result==0){
    echo "Thêm Thất bại";
}else{
    header('Location: ./LinhKien');
}
}

    function Del(){
        $LinhKien=new LinhKien("","","","","","","","","");
        $malinhkien=$_POST['malinhkien'];
        $result=$LinhKien->delLinhKien($malinhkien);
        echo $result;
    }
}

?>