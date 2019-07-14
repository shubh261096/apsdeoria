<?php  
defined('BASEPATH') OR exit('No direct script access allowed');

define('FPDF_FONTPATH', APPPATH .'third_party');

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

    function getFeesDeatils($fees_id){
        // get main CodeIgniter object
        $ci = get_instance();
        $ci->load->model('CommonModel');
        $data = $ci->CommonModel->get_feesDetails($fees_id);
        if($data){
            return $data;
        }
    }

    /* Generate and save PDF file */
    function generatePDF($title, $message, $path, $filename){
        // get main CodeIgniter object
        $ci = get_instance();
        ob_start();
        $pdf = new FPDF();
        $pdf->AddPage();
        $pdf->SetFont('Arial','B',10);
        $pdf->SetTitle($title);
        $pdf->MultiCell(190, 5, $message, 0);
        $pdf->Output($path.$filename.".pdf", 'F');
        ob_end_flush();
   }

   function getStudentDetails($student_id) {
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('CommonModel');
    $data = $ci->CommonModel->get_studentDetails($student_id);
        if($data){
            return $data;
        }
    }
