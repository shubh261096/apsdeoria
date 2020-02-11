<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Download extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/prod/DownloadModel', 'DownloadModel');
    $this->load->model('api/v2/prod/CommonModel', 'CommonModel');
    $this->load->helper('commonprod');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('id'))) {
      $response = array();
      $id = $this->input->post('id');
      $user_type = $this->CommonModel->getUserType($id);
      $data = $this->DownloadModel->get_downloadByUserType($user_type); // getting download content by user_type
      if (!$data == false) {
        $response['error'] = false;
        $response['message'] = "Download Fetched Successfully";
        $response['download'] = $data;
      } else {
        $response['error'] = true;
        $response['message'] = "There is nothing to download now.";
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
