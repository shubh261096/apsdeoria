<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Attendance extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/prod/AttendanceModel', 'AttendanceModel');
    $this->load->helper('commonprod');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('teacher_id', 'date'))) {
      $response = array();
      $teacher_id = $this->input->post('teacher_id');
      $date = $this->input->post('date');

      /** Checking teacher status */
      if (getTeacherStatus($teacher_id)) {
        $response = $this->get_classByTeacher($teacher_id, $date); // getting classes which a teacher teaches
        $httpStatus = REST_Controller::HTTP_OK;
      } else {
        $response['error'] = true;
        $response['message'] = "Status is not active. Please contact administration";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }


    $this->response($response, $httpStatus);
  }

  private function get_classByTeacher($teacher_id, $date)
  {
    $timestamp = strtotime($date);
    $day = date('l', $timestamp);
    $data = $this->AttendanceModel->get_classByTeacher($teacher_id, $day);
    if (!$data == false) {
      foreach ($data as $key => $field) {
        if ($field->class_id) {
          $data[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into data array
          $data[$key]->class_id->students = $this->AttendanceModel->get_studentByClass($data[$key]->class_id->id); // getting students list and adding it as extra parameter to data->class->students array
          foreach ($data[$key]->class_id->students as $new_key => $new_field) {
            if ($new_field->id) {
              $data[$key]->class_id->students[$new_key]->attendance = $this->AttendanceModel->get_attendanceByStudent($new_field->id, $date);
            } else {
              $data[$key]->class_id->students[$new_key]->attendance = null;
            }
          }
        }
      }

      $response['error'] = false;
      $response['message'] = "Details Fetched successfully";
      $response['class_detail'] = $data;
    } else {
      $response['error'] = true;
      $response['message'] = "No data found";
    }
    return $response;
  }

  public function add_post()
  {
    $jsonArray = json_decode(file_get_contents('php://input'), true);
    foreach ($jsonArray['attendance'] as $value) {
      $date = $value['date'];
      $student_id = $value['student_id'];
      $status = $value['status'];
      $remarks = $value['remarks'];
      $timetable_id = $value['timetable_id'];
      $response['error'] = $this->AttendanceModel->add_attendance($date, $student_id, $status, $remarks, $timetable_id);
    }
    if ($response['error']) {
      $response['error'] = false;
      $response['message'] = "Added successfully";
      $this->response($response, REST_Controller::HTTP_OK);
    } else {
      $response['error'] = true;
      $response['message'] = "Error occured while adding";
      $this->response($response, REST_Controller::HTTP_OK);
    }
  }

  public function edit_post()
  {
    $jsonArray = json_decode(file_get_contents('php://input'), true);
    foreach ($jsonArray['attendance'] as $value) {
      $id = $value['id'];
      $date = $value['date'];
      $student_id = $value['student_id'];
      $status = $value['status'];
      $remarks = $value['remarks'];
      $timetable_id = $value['timetable_id'];
      $response['error'] = $this->AttendanceModel->edit_attendance($id, $date, $student_id, $status, $remarks, $timetable_id);
    }
    if ($response['error']) {
      $response['error'] = false;
      $response['message'] = "Edited Attendance successfully";
      $this->response($response, REST_Controller::HTTP_OK);
    } else {
      $response['error'] = true;
      $response['message'] = "Error occured while editing";
      $this->response($response, REST_Controller::HTTP_OK);
    }
  }
}
