<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Classes extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/ClassModel', 'ClassModel');
  }

  public function index()
  {
    $query = $this->ClassModel->getClass();
    $data['CLASSES'] = null;
    if ($query) {
      $data['CLASSES'] =  $query;
    }
    $this->load->view('admin/class/class', $data);
  }

  public function add()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_class_rules') == FALSE) {
      $this->load->view('admin/class/add_class');
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->ClassModel->addClass($post)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Class ID already exists. Please try with different class id.');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('classes');
    }
  }

  public function edit($class_id)
  {

    $classes = $this->ClassModel->editClass($class_id);
    $this->load->view('admin/class/edit_class', ['classes' => $classes]);
  }

  public function update($class_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_class_rules') == FALSE) {
      $classes = $this->ClassModel->editClass($class_id);
      $this->load->view('admin/class/edit_class', ['classes' => $classes]);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->ClassModel->updateClass($class_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('classes');
    }
  }


  public function delete($class_id)
  {
    if ($this->ClassModel->deleteClass($class_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('classes');
  }
}
