<?php
class Controller{

public function Model($model){
    require_once "./model/".$model.".php";
    return new $model;
}

public function View($view, $data=[]){
    require_once "./views/".$view.".php";
}

}
?>