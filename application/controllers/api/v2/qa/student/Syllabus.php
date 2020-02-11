<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Syllabus extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/qa/SyllabusModel', 'SyllabusModel');
    $this->load->model('api/v2/qa/CommonModel', 'CommonModel');
    $this->load->helper('commonqa');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('student_id'))) {
      $response = array();

      $student_id = $this->input->post('student_id');
      $class_id = $this->CommonModel->getClassIdFromStudentId($student_id);

      $response = $this->get_syllabusByClass($class_id); // getting syllabus by class
      $httpStatus = REST_Controller::HTTP_OK;
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }

    $this->response($response, $httpStatus);
  }

  private function get_syllabusByClass($class_id)
  {
    $data = $this->SyllabusModel->get_syllabusByClass($class_id);
    if (!$data == false) {
      $response['error'] = false;
      $response['message'] = "Syllabus fetched successfully";
      $response['syllabus'] = $data;
    } else {
      $response['error'] = true;
      $response['message'] = "Syllabus not found.";
    }
    return $response;
  }
}
