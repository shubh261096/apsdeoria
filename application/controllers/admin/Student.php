<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Student extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/StudentModel', 'StudentModel');
    $this->load->helper('common');
  }

  public function index()
  {
    $query = $this->StudentModel->getStudent();
    $data['STUDENTS'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->class_id) {
          $query[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into query array
        }
      }
      $data['STUDENTS'] =  $query;
    }
    $this->load->view('admin/student/student', $data);
  }

  public function add()
  {
    $data['classes'] = $this->AdminCommonModel->getClassList();
    $data['gender'] = array(
      'Male' => 'Male', 'Female' => 'Female'
    );
    $this->load->view('admin/student/add_student', $data);
  }

  public function insert()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_student_rules') == FALSE) {
      $data['classes'] = $this->AdminCommonModel->getClassList();
      $data['gender'] = array(
        'Male' => 'Male', 'Female' => 'Female'
      );
      $this->load->view('admin/student/add_student', $data);
    } else {
      $student_id = 'APS' . rand(100, 999); //Generating random student_id
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->StudentModel->addStudent($post, $student_id)) {

        /** Getting first four char of fullname and dob month & date and adding to login */
        $pass1 = substr($post['fullname'], 0, 4);
        $pass2 = explode('-', $post['dob']);
        $login = array('id' => $student_id, 'password' => strtolower($pass1) . $pass2[2] . $pass2[1], 'type' => 'student');
        $this->StudentModel->addLogin($login);
        /** end login */

        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Student id already exists. Please try with different student id.');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('student');
    }
  }

  public function edit($student_id)
  {
    $student = $this->StudentModel->editStudent($student_id);
    $data['classes'] = $this->AdminCommonModel->getClassList();
    $gender = array(
      'Male' => 'Male', 'Female' => 'Female'
    );
    $this->load->view('admin/student/edit_student', ['student' => $student, 'classes' => $data, 'gender' => $gender]);
  }

  public function update($student_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_student_rules') == FALSE) {
      $student = $this->StudentModel->editStudent($student_id);
      $data['classes'] = $this->AdminCommonModel->getClassList();
      $gender = array(
        'Male' => 'Male', 'Female' => 'Female'
      );
      $this->load->view('admin/student/edit_student', ['student' => $student, 'classes' => $data, 'gender' => $gender]);
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
      return redirect('student');
    }
  }


  public function delete($student_id)
  {
    if ($this->StudentModel->deleteStudent($student_id) && $this->StudentModel->deleteLogin($student_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('student');
  }
}
