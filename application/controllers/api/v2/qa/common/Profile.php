<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Profile extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/qa/ProfileModel', 'ProfileModel');
    $this->load->model('api/v2/qa/CommonModel', 'CommonModel');
    $this->load->helper('commonqa');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('id'))) {
      $response = array();
      $id = $this->input->post('id');
      $type = $this->CommonModel->getUserType($id);
      
      $data = $this->ProfileModel->get_profile($id, $type); // getting Profile Info
      if (!$data == false) {
        if (strcasecmp($type, STUDENT) == 0) {
          $data->parent = $this->ProfileModel->get_parentInfo($data->id); // Adding new key value in profile object for parent
          $data->class_id = getClassDetails($data->class_id); // Changed class_id key with its details in the same data array.
        }
        $response['error'] = false;
        $response['message'] = "Profile Fetched Successfully";
        $response['profile'] = $data;
        $httpStatus = REST_Controller::HTTP_OK;
      } else {
        $response['error'] = true;
        $response['message'] = "No Data Found or Status is not active. Please contact administration";
        $httpStatus = REST_Controller::HTTP_UNAUTHORIZED;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }


    $this->response($response, $httpStatus);
  }
}
