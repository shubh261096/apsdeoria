<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Inbox extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/prod/NotificationModel', 'NotificationModel');
    $this->load->model('api/v2/prod/CommonModel', 'CommonModel');
    $this->load->helper('commonprod');
  }

  /** This function is not in use */
  public function index_get()
  {
    $response = array();
    $data = $this->NotificationModel->get_notification();
    if (!$data == false) {
      $response['error'] = false;
      $response['message'] = "Inbox Fetched Successfully";
      $response['inbox'] = $data;
    } else {
      $response['error'] = true;
      $response['message'] = "No data found";
    }
    $this->response($response, REST_Controller::HTTP_OK);
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('id'))) {
      $response = array();
      $id = $this->input->post('id');
      $user_type = $this->CommonModel->getUserType($id);
      $data = $this->NotificationModel->get_notificationByUserType($user_type); // getting inbox message by user_type
      if (!$data == false) {
        $response['error'] = false;
        $response['message'] = "Inbox Fetched Successfully";
        $response['inbox'] = $data;
      } else {
        $response['error'] = true;
        $response['message'] = "No data found";
      }
      $httpStatus = REST_Controller::HTTP_OK;
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }

    $this->response($response, $httpStatus);
  }
}
