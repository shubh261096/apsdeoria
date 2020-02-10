<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Feedback extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/FeedbackModel', 'FeedbackModel');
    $this->load->helper('common');
  }

  public function index()
  {
    $query = $this->FeedbackModel->getFeedback();
    $data['FEEDBACK'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->student_id) {
          $query[$key]->student_id = getStudentDetails($field->student_id); // getting student details and adding it into query array
        }
        if ($field->teacher_id) {
          $query[$key]->teacher_id = getTeacherDetails($field->teacher_id); // getting teacher details and adding it into query array
        }
      }
      $data['FEEDBACK'] =  $query;
    }
    $this->load->view('admin/feedback/feedback', $data);
  }
}
