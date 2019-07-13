<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Dashboard extends CI_Controller
{
  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('id'))
      return redirect('admin/admin');

    $this->load->model('admin/DashboardModel');
  }

  public function index()
  {
    $this->load->view('admin/dashboard/dashboard');
  }
}
