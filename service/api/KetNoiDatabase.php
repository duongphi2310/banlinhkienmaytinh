<?php
class Database{

    public function getDB(){
$connect=mysqli_connect("localhost","root","","dblinhkien");
mysqli_query($connect,"SET NAMES 'utf8' ");
return $connect;
    }
}
?>