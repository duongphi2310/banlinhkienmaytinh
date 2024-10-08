<?php
require 'KetNoiDatabase.php';
    
    $arraylist = array();
    array_push($arraylist, "banner1.jpg");
    array_push($arraylist, "banner2.jpg");
    array_push($arraylist, "banner3.jpg");
    array_push($arraylist, "banner4.jpg");
 
echo json_encode($arraylist,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>