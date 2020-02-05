<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Syllabus extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('SyllabusModel');
    $this->load->helper('common');
  }

  /* Check if syllabus already exists */
  public function index_post()
  {
    $subject_id = $this->input->post('subject_id');
    $data = $this->SyllabusModel->isSyllabusAvailable($subject_id);
    if (empty(trim($data->syllabus))) {
      $response['error'] = false;
      $response['message'] = "You can add syllabus";
      $this->response($response, REST_Controller::HTTP_OK);
    } else {
      $response['error'] = true;
      $response['message'] = "You have already added the syllabus for this subject.";
      $this->response($response, REST_Controller::HTTP_OK);
    }
  }

  /* Update syllabus */
  public function update_post()
  {
    $subject_id = $this->input->post('subject_id');
    $subject_description = $this->input->post('subject_description');
    $config = [
      'upload_path' => 'asset/pdf/syllabus/',
      'allowed_types' => 'pdf|doc|docx',
    ];

    $this->load->library('upload', $config);

    if ($this->upload->do_upload('pdfData')) {
      $data = $this->upload->data();
      $path = base_url($config['upload_path'] . $data['raw_name'] . $data['file_ext']);
      if (!empty($path)) {
        if ($this->SyllabusModel->update_Syllabus($subject_id, $subject_description, $path)) {
          $response['error'] = false;
          $response['message'] = "Added Successfully";
          $this->response($response, REST_Controller::HTTP_OK);
        } else {
          $response['error'] = true;
          $response['message'] = "An Error occured. Please try again!";
          $this->response($response, REST_Controller::HTTP_OK);
        }
      }
    }
  }
}
