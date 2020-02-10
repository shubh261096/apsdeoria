<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Learn extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin');

    $this->load->model('admin/LearnModel', 'LearnModel');
    $this->load->helper('common');
  }

  public function index()
  {
    $query = $this->LearnModel->getAllVideos();
    $data['VIDEOS'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->subject_id) {
          $query[$key]->subject_id = getSubjectDetails($field->subject_id); // getting subject details and adding it into query array
          $query[$key]->class_id = $this->LearnModel->getClassDetails($field->subject_id->id); // getting class name from subject_id and adding it into data array
        }
      }
      $data['VIDEOS'] =  $query;
    }
    $this->load->view('admin/learn/learn', $data);
  }

  public function add()
  {
    $data['class'] = $this->LearnModel->getClassList()->result();
    $this->load->view('admin/learn/add_learn', $data);
  }

  public function getSubjectFromClass()
  {
    $class_id = $this->input->post('id', TRUE);
    $data = $this->LearnModel->getSubjectFromClass($class_id);
    $this->output->set_content_type('application/json')->set_output(json_encode($data));
  }

  public function insert()
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_video_rules') == FALSE) {
      $data['class'] = $this->LearnModel->getClassList()->result();
      $this->load->view('admin/learn/add_learn', $data);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      unset($post['class']);
      if ($this->LearnModel->addVideo($post)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Added');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('learn');
    }
  }

  public function edit($learn_id)
  {
    $data['video'] = $this->LearnModel->editVideo($learn_id);
    $data['class'] = $this->LearnModel->getClassList()->result();
    $data['class_id'] = $this->LearnModel->getClassDetails($data['video']->subject_id);
    $data['subject_id'] = getSubjectDetails($data['video']->subject_id);
    $this->load->view('admin/learn/edit_learn', $data);
  }

  public function update($learn_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_video_rules') == FALSE) {
      $data['video'] = $this->LearnModel->editVideo($learn_id);
      $data['class'] = $this->LearnModel->getClassList()->result();
      $data['class_id'] = $this->LearnModel->getClassDetails($data['video']->subject_id);
      $data['subject_id'] = getSubjectDetails($data['video']->subject_id);
      $this->load->view('admin/learn/edit_learn', $data);
    } else {
      $post = $this->input->post();
      if (empty($post['subject_id'])) {
        $data['video'] = $this->LearnModel->editVideo($learn_id);
        $post['subject_id'] = $data['video']->subject_id;
      }
      unset($post['submit']);
      unset($post['class']);
      if ($this->LearnModel->updateVideo($learn_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('learn');
    }
  }


  public function delete($learn_id)
  {
    if ($this->LearnModel->deleteVideo($learn_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('learn');
  }
}
