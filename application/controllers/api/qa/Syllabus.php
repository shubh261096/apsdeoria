<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Syllabus extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('SyllabusModel');
    $this->load->helper('common');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('class_id'))) {
      $response = array();
      $class_id = $this->input->post('class_id');
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
      $response['message'] = "No data found";
    }
    return $response;
  }
}
