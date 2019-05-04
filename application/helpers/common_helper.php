<?php  
defined('BASEPATH') OR exit('No direct script access allowed');

    function isTheseParametersAvailable($params){
        foreach($params as $param){
            if(!isset($_POST[$param])){
            return false; 
        }
    }
    return true;
    }
?>