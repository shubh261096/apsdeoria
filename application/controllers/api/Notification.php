<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Notification extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
    $this->load->model('NotificationModel');
  }

  public function index_get() {
    $response = array();
    $data = $this->NotificationModel->get_notification();
        if(!$data==false) {            
            $response['error'] = false;
            $response['message'] = "Notification Fetched Successfully";
            $response['notification'] = $data;
        } else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }     
    $this->response($response, REST_Controller::HTTP_OK);
    }
}
