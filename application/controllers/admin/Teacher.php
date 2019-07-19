<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Teacher extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin/admin');

    $this->load->model('admin/TeacherModel', 'TeacherModel');
  }

  public function index()
  {
    $query = $this->TeacherModel->getTeacher();
    $data['TEACHERS'] = null;
    if ($query) {
      $data['TEACHERS'] =  $query;
    }
    $this->load->view('admin/teacher/teacher', $data);
  }

  public function all_teacher()
  {
    $query = $this->TeacherModel->getTeacher();
    $data['TEACHERS'] = null;
    if ($query) {
      $data['TEACHERS'] =  $query;
    }
    $this->load->view('admin/teacher/teacher', $data);
  }

  public function add_teacher()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_teacher_rules') == FALSE) {
      $this->load->view('admin/teacher/add_teacher');
    } else {
      $teacher_id = 'APST' . rand(100, 999); //Generating random teacher_id
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->TeacherModel->addTeacher($post, $teacher_id)) {

        /** Getting first four char of email and dob month & date and adding to login */
        $pass1 = substr($post['fullname'], 0, 4);
        $pass2 = explode('-', $post['dob']);
        $login = array('id' => $teacher_id, 'password' => strtolower($pass1) . $pass2[2] . $pass2[1], 'type' => 'teacher');
        $this->TeacherModel->addLogin($login);
        /** end login */

        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Teacher ID Already Exists. Please try with different teacher id.');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/teacher/all_teacher');
    }
  }

  public function edit_teacher($teacher_id)
  {

    $teacher = $this->TeacherModel->editTeacher($teacher_id);
    $this->load->view('admin/teacher/edit_teacher', ['teacher' => $teacher]);
  }

  public function update_teacher($teacher_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_teacher_rules') == FALSE) {
      $teacher = $this->TeacherModel->editTeacher($teacher_id);
      $this->load->view('admin/teacher/edit_teacher', ['teacher' => $teacher]);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->TeacherModel->updateTeacher($teacher_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/teacher/all_teacher');
    }
  }


  public function delete_teacher($teacher_id)
  {
    if ($this->TeacherModel->deleteTeacher($teacher_id) && $this->TeacherModel->deleteLogin($teacher_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('admin/teacher/all_teacher');
  }
}
