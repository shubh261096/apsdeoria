<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Homework extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('HomeworkModel');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('teacher_id'))) {
      $response = array();
      $teacher_id = $this->input->post('teacher_id');
      $response = $this->get_classSubject($teacher_id); // getting class and subject by teacher_id
      $httpStatus = REST_Controller::HTTP_OK;
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }


    $this->response($response, $httpStatus);
  }

  private function get_classSubject($teacher_id)
  {
    $data = $this->HomeworkModel->get_Class($teacher_id);
    if ($data != NULL) {
      foreach ($data as $key => $field) {
        if ($field->class_id) {
          $data[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into data array
          /* Getting subject by class_id and teacher_id*/
          $data[$key]->class_id->subject_id = $this->HomeworkModel->get_Subject($data[$key]->class_id->id, $teacher_id);
          foreach ($data[$key]->class_id->subject_id as $new_key => $new_field) {
            $data[$key]->class_id->subject_id[$new_key] = getSubjectDetails($new_field->subject_id);
          }
        }
      }
      $response['error'] = false;
      $response['message'] = "Data fetched successfully";
      $response['class_subject'] = $data;
    } else {
      $response['error'] = true;
      $response['message'] = "No data found";
    }
    return $response;
  }
}
