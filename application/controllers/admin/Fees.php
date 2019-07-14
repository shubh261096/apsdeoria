<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Fees extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin/admin');

    $this->load->model('admin/FeesModel', 'FeesModel');
  }

  public function index()
  {
    $query = $this->FeesModel->getFeeReceipt();
    $data['FEES'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->student_id) {
          $query[$key]->student_id = getStudentDetails($field->student_id); // getting student details and adding it into query array
        }
        if($field->fees_id){
          $query[$key]->fees_id = getFeesDeatils($field->fees_id); // getting fee details and adding it into query array
        }
      }
      $data['FEES'] =  $query;
    }
    $this->load->view('admin/fees/fees', $data);
  }

  public function add_student()
  {
    $data['classes'] = $this->StudentModel->getClass();
    $this->load->view('admin/student/add_student', $data);
  }

  public function insert_student()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_student_rules') == FALSE) {
      $data['classes'] = $this->StudentModel->getClass();
      $this->load->view('admin/student/add_student', $data);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->StudentModel->addStudent($post)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Student id already exists. Please try with different student id.');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/student/all_student');
    }
  }

  public function edit_student($student_id)
  {
    $student = $this->StudentModel->editStudent($student_id);
    $data = $this->StudentModel->getClass();
    $this->load->view('admin/student/edit_student', ['student' => $student, 'classes' => $data]);
  }

  public function update_student($student_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_student_rules') == FALSE) {
      $student = $this->StudentModel->editStudent($student_id);
      $data = $this->StudentModel->getClass();
      $this->load->view('admin/student/edit_student', ['student' => $student, 'classes' => $data]);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->StudentModel->updateStudent($student_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/student/all_student');
    }
  }


  public function delete_student($student_id)
  {
    if ($this->StudentModel->deleteStudent($student_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('admin/student/all_student');
  }
}
