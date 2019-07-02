<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Attendance extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('AttendanceModel');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('teacher_id', 'date'))) {
      $response = array();
      $teacher_id = $this->input->post('teacher_id');
      $date = $this->input->post('date');
      $response = $this->get_classByTeacher($teacher_id, $date); // getting classes which a teacher teaches
      $httpStatus = REST_Controller::HTTP_OK;
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }


    $this->response($response, $httpStatus);
  }

  private function get_classByTeacher($teacher_id, $date)
  {
    $timestamp = strtotime($date);
    $day = date('l', $timestamp);
    $data = $this->AttendanceModel->get_classByTeacher($teacher_id, $day);
    if (!$data == false) {
      foreach ($data as $key => $field) {
        if ($field->class_id) {
          $data[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into data array
          $data[$key]->class_id->students = $this->AttendanceModel->get_studentByClass($data[$key]->class_id->id); // getting students list and adding it as extra parameter to data->class->students array
          foreach ($data[$key]->class_id->students as $new_key => $new_field) {
            if ($new_field->id) {
              $data[$key]->class_id->students[$new_key]->attendance = $this->AttendanceModel->get_attendanceByStudent($new_field->id, $date);
            } else {
              $data[$key]->class_id->students[$new_key]->attendance = null;
            }
          }
        }
      }

      $response['error'] = false;
      $response['message'] = "Details Fetched successfully";
      $response['class_detail'] = $data;
    } else {
      $response['error'] = true;
      $response['message'] = "No data found";
    }
    return $response;
  }
}
