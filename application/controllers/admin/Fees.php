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
      return redirect('admin');

    $this->load->model('admin/FeesModel', 'FeesModel');
    $this->load->helper('commonprod');
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
        if ($field->fees_id) {
          $query[$key]->fees_id = getFeesDeatils($field->fees_id); // getting fee details and adding it into query array
        }
      }
      $data['FEES'] =  $query;
    }
    $this->load->view('admin/fees/fees', $data);
  }

  public function add()
  {
    $data['students'] = $this->AdminCommonModel->getStudentList();
    $data['months'] = array(
      'January' => 'January', 'February' => 'February', 'March' => 'March', 'April' => 'April', 'May' => 'May', 'June' => 'June', 'July' => 'July',
      'August' => 'August', 'September' => 'September', 'October' => 'October', 'November' => 'November', 'December' => 'December'
    );
    $this->load->view('admin/fees/add_fees', $data);
  }

  public function insert()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_fees_rules') == FALSE) {
      $data['students'] = $this->AdminCommonModel->getStudentList();
      $data['months'] = array(
        'January' => 'January', 'February' => 'February', 'March' => 'March', 'April' => 'April', 'May' => 'May', 'June' => 'June', 'July' => 'July',
        'August' => 'August', 'September' => 'September', 'October' => 'October', 'November' => 'November', 'December' => 'December'
      );
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
      return redirect('fees');
    }
  }

  public function edit($fees_id)
  {
    $fees = $this->FeesModel->editFees($fees_id);
    $student = getStudentDetails($fees->student_id);
    $months = array(
      'January' => 'January', 'February' => 'February', 'March' => 'March', 'April' => 'April', 'May' => 'May', 'June' => 'June', 'July' => 'July',
      'August' => 'August', 'September' => 'September', 'October' => 'October', 'November' => 'November', 'December' => 'December'
    );
    $this->load->view('admin/fees/edit_fees', ['fees' => $fees, 'student' => $student, 'months' => $months]);
  }

  public function update($fees_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_fees_rules') == FALSE) {
      $fees = $this->FeesModel->editFees($fees_id);
      $student = getStudentDetails($fees->student_id);
      $months = array(
        'January' => 'January', 'February' => 'February', 'March' => 'March', 'April' => 'April', 'May' => 'May', 'June' => 'June', 'July' => 'July',
        'August' => 'August', 'September' => 'September', 'October' => 'October', 'November' => 'November', 'December' => 'December'
      );
      $this->load->view('admin/fees/edit_fees', ['fees' => $fees, 'student' => $student, 'months' => $months]);
    } else {
      $post = $this->input->post();
      $fees = $this->FeesModel->editFees($fees_id);
      $post['id'] = $fees_id;
      $post['fees_id'] = $fees->fees_id;
      $post['status'] = 'paid';
      $post['date_paid'] = $fees->date_paid;
      unset($post['submit']);
      if ($this->FeesModel->updateFees($fees_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('fees');
    }
  }


  public function delete($fees_id)
  {
    if ($this->FeesModel->deleteFees($fees_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('fees');
  }
}
