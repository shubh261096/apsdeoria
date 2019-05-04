<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Profile extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
  }

  public function index_post() {
    $response = array();   
    $this->response($response, REST_Controller::HTTP_OK);
    } 
}
