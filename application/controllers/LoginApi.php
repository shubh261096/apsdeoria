<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class LoginApi extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
    $this->load->model('LoginModel');
  }

  public function index_post() {
    $response = array();     
    $email = $this->input->post('email');
    $password = $this->input->post('password');
    $data['row'] = $this->LoginModel->validate_login($email, $password);
    if (!$data['row']==false) {
      $user = array(
          'email'=>$email, 
          'type'=>$data['row']->type
          );
      if(strcasecmp($data['row']->type, TEACHER) == 0) {  
        $response['error'] = false;
        $response['message'] = "Login Successfull";
        $response['user'] = $user;
      }elseif (strcasecmp($data['row']->type, PARENTS) == 0) {
        $response['error'] = false;
        $response['message'] = "Login Successfull";
        $response['user'] = $user;
      }
      $httpStatus = REST_Controller::HTTP_OK;
    } else {
      $response['error'] = true;
      $response['message'] = "Login unscuccesfull. Please try again!";
      $httpStatus = REST_Controller::HTTP_UNAUTHORIZED;
    }
    
    $this->response($response, $httpStatus);
    } 
}
