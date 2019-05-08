<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Homework extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
    $this->load->model('HomeworkModel');
  }

  public function index_post() {
    if(isTheseParametersAvailable(array('class_id','date'))){
      $response = array();
      $class_id = $this->input->post('class_id');
      $date = $this->input->post('date');
      $response = $this->get_homeworkByDate($class_id, $date); // getting homework by date
      if(!$response['error']){
          $httpStatus = REST_Controller::HTTP_OK;
      } else {
          $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
      }
    }else{
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }
    
    
    $this->response($response, $httpStatus);
    } 

    private function get_homeworkByDate($class_id, $date){
        $data = $this->HomeworkModel->get_homeworkByDate($class_id, $date);
        if(!$data==false) {
            foreach ($data as $key => $field) {
                if ($field->class_id) {
                    $data[$key]->class_id = getClassDetails($class_id); // getting class details and adding it into data array
                } 
                if ($field->subject_id) {
                    $data[$key]->subject_id = getSubjectDetails($field->subject_id); // getting subject details and adding it into data array
                }
                if ($field->teacher_id) {
                    $data[$key]->teacher_id = getTeacherDetails($field->teacher_id); // getting teacher details and adding it into data array
                }
            } 
            
            $response['error'] = false;
            $response['message'] = "Homework fetched successfully";
            $response['timetable'] = $data;
        } else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }   
        return $response;
    } 
}
