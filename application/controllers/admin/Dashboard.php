<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Dashboard extends CI_Controller
{
  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/DashboardModel');
    $this->load->model('api/v2/qa/WebhookModel');
  }

  public function index()
  {
    $data['student_count'] = $this->DashboardModel->getTotalStudent();
    $data['teacher_count'] = $this->DashboardModel->getTotalTeacher();
    $data['lazyclick_count'] = $this->WebhookModel->getTotalUsers();
    $this->load->view('admin/dashboard/dashboard', $data);
  }
}
