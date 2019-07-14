<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Subject extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin/admin');

    $this->load->model('admin/SubjectModel', 'SubjectModel');
  }

  public function index()
  {
    $query = $this->SubjectModel->getSubject();
    $data['SUBJECTS'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->class_id) {
          $query[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into query array
        }
      }
      $data['SUBJECTS'] =  $query;
    }
    $this->load->view('admin/subject/subject', $data);
  }

  /* function to download syllabus from admin panel */
  public function download($subject_id)
  {
    $query = $this->SubjectModel->getSyllabus($subject_id);
    $path = $query->syllabus;
    $pth = file_get_contents($path);
    $nme = "syllabus.pdf";
    force_download($nme, $pth);
  }

  public function add_subject()
  {
    $data['classes'] = $this->SubjectModel->getClass();
    $this->load->view('admin/subject/add_subject', $data);
  }

  public function insert_subject()
  {
    $config = [
      'upload_path' => 'asset/pdf/syllabus/',
      'allowed_types' => 'pdf|doc|docx',
    ];

    $this->load->library('upload', $config);
    $this->upload->initialize($config);

    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');

    if ($this->form_validation->run('add_subject_rules') && $this->upload->do_upload('syllabus')) {
      $post = $this->input->post();
      unset($post['submit']);
      $data = $this->upload->data();
      $image_path = base_url($config['upload_path'] . $data['raw_name'] . $data['file_ext']);
      $post['syllabus'] = $image_path;
      if ($this->SubjectModel->addSubject($post)) {
        $this->session->set_flashdata('feedback', 'Added Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Subject id already exists. Please try with different subject id.');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/subject');
    } else {
      $data['classes'] = $this->SubjectModel->getClass();
      $data['upload_error'] = $this->upload->display_errors();
      $this->load->view('admin/subject/add_subject', $data);
    }
  }

  public function edit_subject($subject_id)
  {
    $subject = $this->SubjectModel->editSubject($subject_id);
    $data = $this->SubjectModel->getClass();
    $this->load->view('admin/subject/edit_subject', ['subject' => $subject, 'classes' => $data]);
  }

  public function update_subject($subject_id)
  {
    $this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
    if ($this->form_validation->run('add_subject_rules') == FALSE) {
      $subject = $this->SubjectModel->editSubject($subject_id);
      $data = $this->SubjectModel->getClass();
      $this->load->view('admin/subject/edit_subject', ['subject' => $subject, 'classes' => $data]);
    } else {
      $post = $this->input->post();
      unset($post['submit']);
      if ($this->SubjectModel->updateSubject($subject_id, $post)) {
        $this->session->set_flashdata('feedback', 'Updated Succefully');
        $this->session->set_flashdata('feedback_class', 'alert-success');
      } else {
        $this->session->set_flashdata('feedback', 'Not Updated');
        $this->session->set_flashdata('feedback_class', 'alert-danger');
      }
      return redirect('admin/subject');
    }
  }


  public function delete_subject($subject_id)
  {
    if ($this->SubjectModel->deleteSubject($subject_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('admin/subject');
  }
}
