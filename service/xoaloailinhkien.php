<?php
require 'KetNoiDatabase.php';
$maLinhKien = $_POST["maLinhKien"];

$query = "DELETE FROM loailinhkien WHERE MALLINHKIEN = '$maLinhKien'";
if (mysqli_query($connect, $query)) {
    echo 1;
} else {
    echo 0;
}
?>
