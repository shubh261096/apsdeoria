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

function sendPushNotification($title, $message, $imageUrl, $action, $topic, $actionDestination, $sendTo)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('NotificationModel');
    $ci->load->library('NotificationPOJO');

    $notification = new NotificationPOJO();
    if ($actionDestination == '') {
        $action = '';
    }

    $notification->setTitle($title);
    $notification->setMessage($message);
    $notification->setImage($imageUrl);
    $notification->setAction($action);
    $notification->setActionDestination($actionDestination);

    $firebase_api_prod = 'AAAALviGhbM:APA91bEg3inOYZLskeCtRZZfitG11bU92RAOUkR_i6Fm_FJmIW1xw9ZJvNtsQ24XQYgGnqevvo2S4q7zTA7SZxPWlw8-LM5efpgTajDnKCDUzkk7Qz6OsI_Jm8XfR3XOhFPgc_VbTl2L';
    $firebase_api_qa = 'AAAAsv5UsBc:APA91bEQ1uR4ufsRv4FtFTWjw7hMuCW0o9DtswmEy0_awtYJOhHOulu4dv8gcX55ir_b_iF92Teo1DMtb3PXUZ2nsAl7CxrrkxNgrNX7LQvDf_QaBulFIcbwwNCOD2dKjwuO7s31wH5B';

    $requestData = $notification->getNotificatin();

    $data['fields'] = array(
        'to' => '/topics/' . $topic,
        'data' => $requestData,
    );

    // Set POST variables
    $url = 'https://fcm.googleapis.com/fcm/send';

    $headers = array(
        'Authorization: key=' . $firebase_api_prod,
        'Content-Type: application/json'
    );

    // Open connection
    $ch = curl_init();

    // Set the url, number of POST vars, POST data
    curl_setopt($ch, CURLOPT_URL, $url);

    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Disabling SSL Certificate support temporarily
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data['fields']));

    // Execute post
    $data['result'] = curl_exec($ch);
    if ($data['result'] === FALSE) {
        die('Curl failed: ' . curl_error($ch));
    } else {
        if (!empty($title)) {
            $post['title'] = $title;
            $post['message'] = $message;
            $post['image_url'] = $imageUrl;
            $post['action'] = $action;
            $post['action_destination'] = $actionDestination;
            $post['send_to'] = $sendTo;
            $post['topic'] = $topic;
            $ci->NotificationModel->add_notification($post);
        }
    }

    // Close connection
    curl_close($ch);
}
