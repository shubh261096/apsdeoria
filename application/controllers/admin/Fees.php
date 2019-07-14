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

  public function add_fees()
  {
    $data['students'] = $this->FeesModel->getStudents();
    $data['months'] = array('January'=>'January', 'February'=>'February', 'March'=>'March', 'April'=>'April', 'May'=>'May', 'June'=>'June', 'July'=>'July',
                            'August'=>'August', 'September'=>'September', 'October'=>'October', 'November'=>'November', 'December'=>'December');
    $this->load->view('admin/fees/add_fees', $data);
  }

  public function insert_fees()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_fees_rules') == FALSE) {
      $data['students'] = $this->FeesModel->getStudents();
      $data['months'] = array('January'=>'January', 'February'=>'February', 'March'=>'March', 'April'=>'April', 'May'=>'May', 'June'=>'June', 'July'=>'July',
                            'August'=>'August', 'September'=>'September', 'October'=>'October', 'November'=>'November', 'December'=>'December');
      $this->load->view('admin/fees/add_fees', $data);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      $class = $this->FeesModel->getClassId($post['student_id']);
      $fees = $this->FeesModel->getFeesId($class->class_id);
      $post['fees_id'] = $fees->id;
      if ($this->FeesModel->addFees($post)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Added');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/fees');
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
