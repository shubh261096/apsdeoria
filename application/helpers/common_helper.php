<?php
defined('BASEPATH') or exit('No direct script access allowed');

define('FPDF_FONTPATH', APPPATH . 'third_party');

function isTheseParametersAvailable($params)
{
    if (count($_POST) > sizeof($params)) { // checking here the count of post data and parameter required.
        return false;
    }
    foreach ($params as $param) {
        if (!isset($_POST[$param])) {
            return false;
        }
    }
    return true;
}

function getClassDetails($class_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('CommonModel');
    $data = $ci->CommonModel->get_classDetails($class_id);
    if ($data) {
        return $data;
    }
}

function getTeacherDetails($teacher_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('CommonModel');
    $data = $ci->CommonModel->get_teacherDetails($teacher_id);
    if ($data) {
        return $data;
    }
}

function getSubjectDetails($subject_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('CommonModel');
    $data = $ci->CommonModel->get_subjectDetails($subject_id);
    if ($data) {
        return $data;
    }
}

function getFeesDeatils($fees_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('CommonModel');
    $data = $ci->CommonModel->get_feesDetails($fees_id);
    if ($data) {
        return $data;
    }
}

/* Generate and save PDF file usinf FDF Library*/
function generatePDF($title, $message, $path, $filename)
{
    // get main CodeIgniter object
    $ci = get_instance();
    ob_start();
    $pdf = new FPDF();
    $pdf->AddPage();
    $pdf->SetFont('Arial', 'B', 10);
    $pdf->SetTitle($title);
    $pdf->MultiCell(190, 5, $message, 0);
    $pdf->Output($path . $filename . ".pdf", 'F');
    ob_end_flush();
}

function getStudentDetails($student_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('CommonModel');
    $data = $ci->CommonModel->get_studentDetails($student_id);
    if ($data) {
        return $data;
    }
}

/* Generate and save PDF file using TCPDF Library*/
function generateTCPDF($title, $message, $path, $filename)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->library('Pdf');
    ob_start();
    $pdf = new Pdf('P', 'mm', 'A4', true, 'UTF-8', false);
    $pdf->SetTitle($title);
    $pdf->SetHeaderMargin(30);
    $pdf->SetTopMargin(20);
    $pdf->setFooterMargin(20);
    $pdf->SetAutoPageBreak(true);
    $pdf->SetAuthor('Agrawal Public School');
    $pdf->SetDisplayMode('real', 'default');
    // set some language dependent data:
    $lg = array();
    $l['a_meta_charset'] = 'UTF-8';
    $l['a_meta_dir'] = 'ltr';
    $l['a_meta_language'] = 'hi';
    $lg['w_page'] = 'page';

    // set some language-dependent strings (optional)
    $pdf->setLanguageArray($lg);
    $pdf->SetFont('freesans', '', 12);
    $pdf->AddPage();
    $pdf->WriteHTML($title, true, 0, true, 0, 'C');
    $pdf->Ln();
    $pdf->WriteHTML($message, true, 0, true, 0);
    $pdf->Output($path . $filename . ".pdf", 'F');

    ob_end_flush();
}
