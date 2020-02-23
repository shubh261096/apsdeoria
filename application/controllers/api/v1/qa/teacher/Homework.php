<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Homework extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v1/HomeworkModel');
    $this->load->helper('common');
  }

  public function index_post()
  {
    if (isTheseParametersAvailable(array('teacher_id'))) {
      $response = array();
      $teacher_id = $this->input->post('teacher_id');

      /** Checking teacher status */
      if (getTeacherStatus($teacher_id)) {
        $response = $this->get_classSubject($teacher_id); // getting class and subject by teacher_id
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

  private function get_classSubject($teacher_id)
  {
    $data = $this->HomeworkModel->get_Class($teacher_id);
    if ($data != NULL) {
      foreach ($data as $key => $field) {
        if ($field->class_id) {
          $data[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into data array
          /* Getting subject by class_id and teacher_id*/
          $data[$key]->class_id->subject_id = $this->HomeworkModel->get_Subject($data[$key]->class_id->id, $teacher_id);
          foreach ($data[$key]->class_id->subject_id as $new_key => $new_field) {
            $data[$key]->class_id->subject_id[$new_key] = getSubjectDetails($new_field->subject_id);
          }
        }
      }
      $response['error'] = false;
      $response['message'] = "Data fetched successfully";
      $response['class_subject'] = $data;
    } else {
      $response['error'] = true;
      $response['message'] = "No data found";
    }
    return $response;
  }

  /* Funtion to generate and save pdf file */
  public function add_post()
  {
    $value = json_decode(file_get_contents('php://input'), true);
    $date = $value['date'];
    $class_id = $value['class_id'];
    $subject_id = $value['subject_id'];
    $teacher_id = $value['teacher_id'];

    /** Checking teacher status */
    if (getTeacherStatus($teacher_id)) {
      if (!empty($value['title'])) {
        $title = $value['title'];
      }
      if (!empty($value['description'])) {
        $description = $value['description'];
      }
      if (!empty($value['remarks'])) {
        $remarks = $value['remarks'];
      } else {
        $remarks = "";
      }

      /* Getting class name  by id */
      $class = getClassDetails($class_id);
      $class_name = str_replace(' ', '_', $class->name);
      /* Getting subject name  by id */
      $subject = getSubjectDetails($subject_id);
      $subject_name = str_replace(' ', '_', $subject->name);
      $path =  base_url() . 'asset/pdf/homework/' . $class_name . '_' . $subject_name . '_' . $date . '.pdf';

      $check_array = array('date' => $date, 'class_id' => $class_id, 'teacher_id' => $teacher_id, 'subject_id' => $subject_id);

      if ($this->HomeworkModel->is_HomeworkAvailable($check_array) == FALSE) {
        if (empty($value['title']) || empty($value['description'])) {
          $response['error'] = true;
          $response['message'] = "Title and description cannot be empty";
          $data = array('params' => null);
          $response['data'] = $data;
          $httpStatus = REST_Controller::HTTP_OK;
        } else {
          $add_array = array('date' => $date, 'class_id' => $class_id, 'teacher_id' => $teacher_id, 'subject_id' => $subject_id, 'data' => $path, 'remarks' => $remarks);
          if ($this->HomeworkModel->add_Homework($add_array)) {
            generateTCPDF($title, $description, 'asset/pdf/homework/', $class_name . '_' . $subject_name . '_' . $date);
            sendPushNotification($subject_name . ' homework is added', 'Please check into the homework section', NULL, NULL, $class_id, NULL, 'topic');
            $response['error'] = false;
            $response['message'] = "Added successfully";
            $httpStatus = REST_Controller::HTTP_OK;
          } else {
            $response['error'] = true;
            $response['message'] = "An Error occured! Please try again.";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
          }
        }
      } else {
        $response['error'] = true;
        $response['message'] = "Homework is already added for the date.";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Status is not active. Please contact administration";
      $httpStatus = REST_Controller::HTTP_OK;
    }


    $this->response($response, $httpStatus);
  }
}
