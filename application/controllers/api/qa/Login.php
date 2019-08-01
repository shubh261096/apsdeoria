<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Login extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('LoginModel');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('id', 'password'))) {
      $response = array();
      $id = $this->input->post('id');
      $password = $this->input->post('password');
      $data['row'] = $this->LoginModel->validate_login($id, $password); // validating login
      if (!$data['row'] == false) {
        $status = $this->LoginModel->get_userStatus($id, $data['row']->type); // getting if status is active or not
        if (!$status == false) {
          $user = array(
            'id' => $id,
            'type' => $data['row']->type
          );
          $response['error'] = false;
          $response['message'] = "Login Successfull";
          $response['user'] = $user;
          $httpStatus = REST_Controller::HTTP_OK;
        } else {
          $response['error'] = true;
          $response['message'] = "Status is not active. Please contact administration";
          $httpStatus = REST_Controller::HTTP_OK;
        }
      } else {
        $response['error'] = true;
        $response['message'] = "Login unscuccesfull. Please try again!";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }

    $this->response($response, $httpStatus);
  }

  public function validate_post()
  {
    if (isTheseParametersAvailable(array('id', 'dob'))) {
      $response = array();
      $id = $this->input->post('id');
      $dob = $this->input->post('dob');
      if ($this->LoginModel->validate_user($id, $dob)) {
        $response['error'] = false;
        $response['message'] = "Reset your password now";
        $httpStatus = REST_Controller::HTTP_OK;
      } else {
        $response['error'] = true;
        $response['message'] = "School ID or DOB is incorrect. Please try again.";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }
    $this->response($response, $httpStatus);
  }

  public function reset_post()
  {
    if (isTheseParametersAvailable(array('id', 'password'))) {
      $response = array();
      $id = $this->input->post('id');
      $password = $this->input->post('password');
      if ($this->LoginModel->reset_password($id, $password)) {
        $response['error'] = false;
        $response['message'] = "Password Reset succesfully.";
        $httpStatus = REST_Controller::HTTP_OK;
      } else {
        $response['error'] = true;
        $response['message'] = "Some Error Occured. Please try again.";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }
    $this->response($response, $httpStatus);
  }
}
