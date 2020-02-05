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
    $ci->load->model('api/v2/qa/CommonModel');
    $data = $ci->CommonModel->get_classDetails($class_id);
    if ($data) {
        return $data;
    }
}

function getTeacherDetails($teacher_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('api/v2/qa/CommonModel');
    $data = $ci->CommonModel->get_teacherDetails($teacher_id);
    if ($data) {
        return $data;
    }
}

function getTeacherStatus($teacher_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('api/v2/qa/CommonModel');
    $data = $ci->CommonModel->get_teacherStatus($teacher_id);
    if ($data) {
        return $data;
    }
}

function getSubjectDetails($subject_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('api/v2/qa/CommonModel');
    $data = $ci->CommonModel->get_subjectDetails($subject_id);
    if ($data) {
        return $data;
    }
}

function getFeesDeatils($fees_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('api/v2/qa/CommonModel');
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
    $ci->load->model('api/v2/qa/CommonModel');
    $data = $ci->CommonModel->get_studentDetails($student_id);
    if ($data) {
        return $data;
    }
}

function getAllSubjects($class_id)
{
    // get main CodeIgniter object
    $ci = get_instance();
    $ci->load->model('api/v2/qa/CommonModel');
    $data = $ci->CommonModel->get_AllSubjectsByClassId($class_id);
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
    $ci->load->model('api/v2/qa/NotificationModel');
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

    $firebase_api_qa = 'AAAAsv5UsBc:APA91bEQ1uR4ufsRv4FtFTWjw7hMuCW0o9DtswmEy0_awtYJOhHOulu4dv8gcX55ir_b_iF92Teo1DMtb3PXUZ2nsAl7CxrrkxNgrNX7LQvDf_QaBulFIcbwwNCOD2dKjwuO7s31wH5B';

    $requestData = $notification->getNotificatin();

    $data['fields'] = array(
        'to' => '/topics/' . $topic,
        'data' => $requestData,
    );

    // Set POST variables
    $url = 'https://fcm.googleapis.com/fcm/send';

    $headers = array(
        'Authorization: key=' . $firebase_api_qa,
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

/**
 * Compute the start and end date of some fixed o relative quarter in a specific year.
 * @param mixed $quarter  Integer from 1 to 4 or relative string value:
 *                        'this', 'current', 'previous', 'first' or 'last'.
 *                        'this' is equivalent to 'current'. Any other value
 *                        will be ignored and instead current quarter will be used.
 *                        Default value 'current'. Particulary, 'previous' value
 *                        only make sense with current year so if you use it with
 *                        other year like: get_dates_of_quarter('previous', 1990)
 *                        the year will be ignored and instead the current year
 *                        will be used.
 * @param int $year       Year of the quarter. Any wrong value will be ignored and
 *                        instead the current year will be used.
 *                        Default value null (current year).
 * @param string $format  String to format returned dates
 * @return array          Array with two elements (keys): start and end date.
 */
function get_dates_of_quarter($quarter = 'current', $year = null, $format = 'Y-m-d')
{
    if (!is_int($year)) {
        $year = (new DateTime)->format('Y');
    }
    $current_quarter = ceil((new DateTime)->format('n') / 3);
    switch (strtolower($quarter)) {
        case 'this':
        case 'current':
            $quarter = ceil((new DateTime)->format('n') / 3);
            break;

        case 'previous':
            $year = (new DateTime)->format('Y');
            if ($current_quarter == 1) {
                $quarter = 4;
                $year--;
            } else {
                $quarter =  $current_quarter - 1;
            }
            break;

        case 'first':
            $quarter = 1;
            break;

        case 'last':
            $quarter = 4;
            break;

        default:
            $quarter = (!is_int($quarter) || $quarter < 1 || $quarter > 4) ? $current_quarter : $quarter;
            break;
    }
    if ($quarter === 'this') {
        $quarter = ceil((new DateTime)->format('n') / 3);
    }
    $start = new DateTime($year . '-' . (3 * $quarter - 2) . '-1 00:00:00');
    $end = new DateTime($year . '-' . (3 * $quarter) . '-' . ($quarter == 1 || $quarter == 4 ? 31 : 30) . ' 23:59:59');

    return array(
        'start' => $format ? $start->format('Y-m-d') : $start,
        'end' => $format ? $end->format('Y-m-d') : $end,
    );
}
