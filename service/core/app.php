<?php
class App {

    protected $controller="HomeController";
    protected $action="Index";
    protected $params=[];
    function __construct(){
       $arr=$this->UrlProcess();
       //Xử lý controller
       if(isset($arr[0])){
           
        $this->controller=$arr[0]."Controller";
       }else{
           $arr[0]=$this->controller;
       }
       if(file_exists("./controllers/".$arr[0]."Controller.php"))
            {
                unset($arr[0]);
            }
            require_once "./controllers/".$this->controller.".php";
            $this->controller=new $this->controller;

            //Xử lí action
            if(isset($arr[1])){
               if(method_exists($this->controller,$arr[1])){
                   $this->action=$arr[1];
                   unset($arr[1]);
                   
               }
            }

            //Xử lí Params
            $this->params=$arr?array_values($arr):[];
            //Gọi phương thức thực thi
         call_user_func_array([$this->controller,$this->action],$this->params);
}

    function UrlProcess(){


        if(isset($_GET["url"]))
        {
            $arr=explode("/",filter_var(trim($_GET["url"],"/")));
          
            return $arr;
            
        }
    }

}
?>