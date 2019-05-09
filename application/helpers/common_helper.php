<?php  
defined('BASEPATH') OR exit('No direct script access allowed');

    function isTheseParametersAvailable($params){
        if(count($_POST) > sizeof($params)){ // checking here the count of post data and parameter required.
            return false;
        }
        foreach($params as $param){
            if(!isset($_POST[$param])){
            return false; 
        }
    }
    return true;
    }

    function getClassDetails($class_id){
        // get main CodeIgniter object
        $ci = get_instance();
        $ci->load->model('CommonModel');
        $data = $ci->CommonModel->get_classDetails($class_id);
        if($data){
            return $data;
        }
    }

    function getTeacherDetails($teacher_id){
        // get main CodeIgniter object
        $ci = get_instance();
        $ci->load->model('CommonModel');
        $data = $ci->CommonModel->get_teacherDetails($teacher_id);
        if($data){
            return $data;
        }
    }

    function getSubjectDetails($subject_id){
        // get main CodeIgniter object
        $ci = get_instance();
        $ci->load->model('CommonModel');
        $data = $ci->CommonModel->get_subjectDetails($subject_id);
        if($data){
            return $data;
        }
    }
?>