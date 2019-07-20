<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Login extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/LoginModel', 'LoginModel');
  }

  public function index()
  {
    $query = $this->LoginModel->getLogin();
    $data['LOGIN'] = null;
    if ($query) {
      $data['LOGIN'] =  $query;
    }
    $this->load->view('admin/login/login', $data);
  }
}
