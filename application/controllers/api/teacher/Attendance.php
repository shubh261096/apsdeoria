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
    if (isTheseParametersAvailable(array('teacher_id'))) {
      $response = array();
      $teacher_id = $this->input->post('teacher_id');
      $response = $this->get_classByTeacher($teacher_id); // getting classes which a teacher teaches
      $httpStatus = REST_Controller::HTTP_OK;
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }


    $this->response($response, $httpStatus);
  }

  private function get_classByTeacher($teacher_id)
  {
    $data = $this->AttendanceModel->get_classByTeacher($teacher_id);
    if (!$data == false) {
      foreach ($data as $key => $field) {
        if ($field->class_id) {
          $data[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into data array
          $data[$key]->class_id->students = $this->AttendanceModel->get_studentByClass($data[$key]->class_id->id); // getting students list and adding it as extra parameter to data->class->students array
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
