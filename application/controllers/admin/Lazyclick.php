<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Lazyclick extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('api/v2/qa/WebhookModel', 'WebhookModel');
    $this->load->helper('commonprod');
  }

  public function index()
  {
    $query = $this->WebhookModel->getAllData();
    $data['DATA'] = $query;
    $this->load->view('admin/lazyclick/lazyclick', $data);
  }


  public function delete($row_id)
  {
    if ($this->WebhookModel->deleteRow($row_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('lazyclick');
  }
}
