<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Profile extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
    $this->load->model('ProfileModel');
  }

  public function index_post() {
    if(isTheseParametersAvailable(array('id','type'))){
      $response = array();
      $id = $this->input->post('id');
      $type = $this->input->post('type'); 
      $data = $this->ProfileModel->get_profile($id, $type); // getting Profile Info
      if(!$data==false) {
        if (strcasecmp($type, PARENTS) == 0) {
          $data->parent = $this->ProfileModel->get_parentInfo($data->id); // Adding new key value in profile object for parent
        }
        $response['error'] = false;
        $response['message'] = "Profile Fetched Successfully";
        $response['profile'] = $data;
        $httpStatus = REST_Controller::HTTP_OK;
      } else {
        $response['error'] = true;
        $response['message'] = "Status is not active. Please contact administration";
        $httpStatus = REST_Controller::HTTP_UNAUTHORIZED;
      }
    }else{
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }
    
    
    $this->response($response, $httpStatus);
    } 
}
