<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Homework extends CI_Controller
{
  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/HomeworkModel', 'HomeworkModel');
    $this->load->helper('commonprod');
  }

  public function index()
  {
    $query = $this->HomeworkModel->getHomework();
    $data['HOMEWORKS'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->class_id) {
          $query[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into query array
        }
        if ($field->teacher_id) {
          $query[$key]->teacher_id = getTeacherDetails($field->teacher_id); // getting teacher details and adding it into query array
        }
        if ($field->subject_id) {
          $query[$key]->subject_id = getSubjectDetails($field->subject_id); // getting subject details and adding it into query array
        }
      }
      $data['HOMEWORKS'] =  $query;
    }
    $this->load->view('admin/homework/homework', $data);
  }

  /* function to download homework from admin panel */
  public function download($homework_id)
  {
    $query = $this->HomeworkModel->getHomeworkData($homework_id);
    $path = $query->data;
    $pth = file_get_contents($path);
    $nme = "homework.pdf";
    force_download($nme, $pth);
  }

  public function add()
  {
    $data['classes'] = $this->AdminCommonModel->getClassList();
    $data['subjects'] = $this->AdminCommonModel->getSubjectList();
    $data['teachers'] = $this->AdminCommonModel->getTeacherList();
    $this->load->view('admin/homework/add_homework', $data);
  }

  public function insert()
  {
    $config = [
      'upload_path' => 'asset/pdf/homework/',
      'allowed_types' => 'pdf|doc|docx',
    ];

    $this->load->library('upload', $config);
    $this->upload->initialize($config);

    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');

    if ($this->upload->do_upload('data')) {
      $post = $this->input->post();
      unset($post['submit']);
      $data = $this->upload->data();
      $image_path = base_url($config['upload_path'] . $data['raw_name'] . $data['file_ext']);
      $post['data'] = $image_path;
      if ($this->HomeworkModel->addHomework($post)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'An Error Occured');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('homework');
    } else {
      $data['classes'] = $this->AdminCommonModel->getClassList();
      $data['subjects'] = $this->AdminCommonModel->getSubjectList();
      $data['teachers'] = $this->AdminCommonModel->getTeacherList();
      $data['upload_error'] = $this->upload->display_errors();
      $this->load->view('admin/homework/add_homework', $data);
    }
  }

  public function edit($homework_id)
  {
    $data['homework'] = $this->HomeworkModel->editHomework($homework_id);
    $data['classes'] = $this->AdminCommonModel->getClassList();
    $data['subjects'] = $this->AdminCommonModel->getSubjectList();
    $data['teachers'] = $this->AdminCommonModel->getTeacherList();
    $this->load->view('admin/homework/edit_homework', $data);
  }

  public function update($homework_id)
  {
    $config = [
      'upload_path' => 'asset/pdf/homework/',
      'allowed_types' => 'pdf|doc|docx',
    ];

    $this->load->library('upload', $config);
    $this->upload->initialize($config);

    $post = $this->input->post();
    unset($post['submit']);
    if (empty($_FILES['data']['name'])) {
      $query = $this->HomeworkModel->getHomeworkData($homework_id);
      $path = $query->data;
      $post['data'] = $path;
      if ($this->HomeworkModel->updateHomework($homework_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('homework');
    } else {
      if ($this->upload->do_upload('data')) {
        $data = $this->upload->data();
        $image_path = base_url($config['upload_path'] . $data['raw_name'] . $data['file_ext']);
        $post['data'] = $image_path;
        if ($this->HomeworkModel->updateHomework($homework_id, $post)) {
          $this->session->set_flashdata('feedback', 'Updated Succefully');
          $this->session->set_flashdata('feedback_class', 'alert-success');
        } else {
          $this->session->set_flashdata('feedback', 'Not Updated');
          $this->session->set_flashdata('feedback_class', 'alert-danger');
        }
        return redirect('homework');
      } else {
        $data['homework'] = $this->HomeworkModel->editHomework($homework_id);
        $data['classes'] = $this->AdminCommonModel->getClassList();
        $data['subjects'] = $this->AdminCommonModel->getSubjectList();
        $data['teachers'] = $this->AdminCommonModel->getTeacherList();
        $data['upload_error'] = $this->upload->display_errors();
        $this->load->view('admin/homework/edit_homework', $data);
      }
    }
  }

  public function delete($homework_id)
  {
    if ($this->HomeworkModel->deleteHomework($homework_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('homework');
  }
}
