<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Login extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
    $this->load->model('LoginModel');
  }

  public function index_post() {
    $response = array();     
    $email = $this->input->post('email');
    $password = $this->input->post('password');
    $data['row'] = $this->LoginModel->validate_login($email, $password); // validating login
    if (!$data['row']==false) {
      $id = $this->LoginModel->get_uniqueId($email, $data['row']->type); // getting userId if status is active or 1
      if(!$id==false) {
        $user = array(
          'id'=>$id,
          'email'=>$email, 
          'type'=>$data['row']->type
          );
          $response['error'] = false;
          $response['message'] = "Login Successfull";
          $response['user'] = $user;
          $httpStatus = REST_Controller::HTTP_OK;
      }else{
        $response['error'] = true;
        $response['message'] = "Status is not active. Please contact administration";
        $httpStatus = REST_Controller::HTTP_UNAUTHORIZED;
      }  
    } else {
      $response['error'] = true;
      $response['message'] = "Login unscuccesfull. Please try again!";
      $httpStatus = REST_Controller::HTTP_UNAUTHORIZED;
    }
    
    $this->response($response, $httpStatus);
    } 
}
