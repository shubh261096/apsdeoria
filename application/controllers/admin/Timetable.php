<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Timetable extends CI_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->view('admin/includes/header');
    $this->load->view('admin/includes/footer');
    if (!$this->session->userdata('user_id'))
      return redirect('admin/admin');

    $this->load->model('admin/TimetableModel', 'TimetableModel');
  }

  public function index()
  {
    $query = $this->TimetableModel->getTimetable();
    $data['TIMETABLE'] = null;
    if ($query) {
      foreach ($query as $key => $field) {
        if ($field->class_id) {
          $query[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into query array
        }
        if ($field->subject_id) {
          $query[$key]->subject_id = getSubjectDetails($field->subject_id); // getting subject details and adding it into query array
        }
        if ($field->teacher_id) {
          $query[$key]->teacher_id = getTeacherDetails($field->teacher_id); // getting teacher details and adding it into query array
        }
      }
      $data['TIMETABLE'] =  $query;
    }
    $this->load->view('admin/timetable/timetable', $data);
  }

  public function add()
  {
    $data['classes'] = $this->AdminCommonModel->getClassList();
    $data['subjects'] = $this->AdminCommonModel->getSubjectList();
    $data['teachers'] = $this->AdminCommonModel->getTeacherList();
    $data['days'] = array(
      'Monday' => 'Monday', 'Tuesday' => 'Tuesday', 'Wednesday' => 'Wednesday',
      'Thursday' => 'Thursday', 'Friday' => 'Friday', 'Saturday' => 'Saturday'
    );
    $this->load->view('admin/timetable/add_timetable', $data);
  }

  public function insert()
  {
    $post = $this->input->post();
    unset($post['submit']);
    if ($this->TimetableModel->addTimetable($post)) {
      $this->session->set_flashdata('feedback', 'Added Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not Added');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('admin/timetable');
  }

  public function edit($timetable_id)
  {
    $data['timetable'] = $this->TimetableModel->editTimetable($timetable_id);
    $data['classes'] = $this->AdminCommonModel->getClassList();
    $data['subjects'] = $this->AdminCommonModel->getSubjectList();
    $data['teachers'] = $this->AdminCommonModel->getTeacherList();
    $data['days'] = array(
      'Monday' => 'Monday', 'Tuesday' => 'Tuesday', 'Wednesday' => 'Wednesday',
      'Thursday' => 'Thursday', 'Friday' => 'Friday', 'Saturday' => 'Saturday'
    );
    $this->load->view('admin/timetable/edit_timetable', $data);
  }

  public function update($timetable_id)
  {
    $post = $this->input->post();
    unset($post['submit']);
    $post['id'] = $timetable_id;
    if ($this->TimetableModel->updateTimetable($timetable_id, $post)) {
      $this->session->set_flashdata('feedback', 'Updated Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not Updated');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('admin/timetable');
  }


  public function delete($timetable_id)
  {
    if ($this->TimetableModel->deleteTimetable($timetable_id)) {
      $this->session->set_flashdata('feedback', 'Deleted Succefully');
      $this->session->set_flashdata('feedback_class', 'alert-success');
    } else {
      $this->session->set_flashdata('feedback', 'Not deleted');
      $this->session->set_flashdata('feedback_class', 'alert-danger');
    }
    return redirect('admin/timetable');
  }
}
