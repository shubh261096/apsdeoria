<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Fees extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
    $this->load->model('FeesModel');
  }

  public function index_post() {
    if(isTheseParametersAvailable(array('class_id','year','student_id'))){
      $response = array();
      $class_id = $this->input->post('class_id');
      $year = $this->input->post('year');
      $student_id = $this->input->post('student_id');
      $response = $this->get_feesYearly($class_id, $year, $student_id); // getting fees monthly
      $httpStatus = REST_Controller::HTTP_OK;
    }else{
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }
    
    
    $this->response($response, $httpStatus);
    } 

    private function get_feesYearly($class_id, $year, $student_id){
        $data = $this->FeesModel->get_feesYearly($student_id, $year);
        if(!$data==false) {
            foreach ($data as $key => $field) {
                if ($field->fees_id) {
                    $data[$key]->fees_id = getFeesDeatils($field->fees_id); // getting fee details and adding it into data array
                }
            } 
            
            $response['error'] = false;
            $response['message'] = "Fees fetched successfully";
            $response['fees'] = $data;
        } else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }   
        return $response;
    } 
}
