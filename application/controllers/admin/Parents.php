<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Parents extends CI_Controller
{
  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin/admin');

    $this->load->model('admin/ParentModel', 'ParentModel');
  }

  public function index()
  {
    $query = $this->ParentModel->getParent();
    $data['PARENTS'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->student_id) {
          $query[$key]->student_id = getStudentDetails($field->student_id); // getting student details and adding it into query array
        }
      }
      $data['PARENTS'] =  $query;
    }
    $this->load->view('admin/parent/parent', $data);
  }

  public function add()
  {
    $data['students'] = $this->ParentModel->getStudent();
    $data['types'] = array(
      'Father' => 'Father', 'Mother' => 'Mother', 'Guardian' => 'Guardian'
    );
    $this->load->view('admin/parent/add_parent', $data);
  }

  public function insert()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_parent_rules') == FALSE) {
      $data['students'] = $this->ParentModel->getStudent();
      $data['types'] = array(
        'Father' => 'Father', 'Mother' => 'Mother', 'Guardian' => 'Guardian'
      );
      $this->load->view('admin/parent/add_parent', $data);
    } else {
      $parent_id = 'APSP' . rand(100, 999); //Generating random parent_id
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->ParentModel->addParent($post, $parent_id)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Parent id already exists. Please try with different Parent id.');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/parents');
    }
  }

  public function edit($parent_id)
  {
    $data['parent'] = $this->ParentModel->editParent($parent_id);
    $data['students'] = $this->ParentModel->getStudent();
    $data['types'] = array(
      'Father' => 'Father', 'Mother' => 'Mother', 'Guardian' => 'Guardian'
    );
    $this->load->view('admin/parent/edit_parent', $data);
  }

  public function update($parent_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_parent_rules') == FALSE) {
      $data['parent'] = $this->ParentModel->editParent($parent_id);
      $data['students'] = $this->ParentModel->getStudent();
      $data['types'] = array(
        'Father' => 'Father', 'Mother' => 'Mother', 'Guardian' => 'Guardian'
      );
      $this->load->view('admin/parent/edit_parent', $data);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      $post['id'] = $parent_id;
      if ($this->ParentModel->updateParent($parent_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/parents');
    }
  }

  public function delete($parent_id)
  {
    if ($this->ParentModel->deleteParent($parent_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('admin/parents');
  }
}
