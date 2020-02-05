<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Dashboard extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/prod/DashboardModel', 'DashboardModel');
    $this->load->helper('commonprod');
  }

  public function index_get()
  {
    $response = array();
    $data = $this->DashboardModel->get_dashboardUi();
    if (!$data == false) {
      $response['error'] = false;
      $response['message'] = "Fetched Successfully";
      $response['dashboard'] = $data;
    } else {
      $response['error'] = true;
      $response['message'] = "No data found";
    }
    $this->response($response, REST_Controller::HTTP_OK);
  }
}
