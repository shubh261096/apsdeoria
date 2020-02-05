<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Feedback extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/qa/FeedbackModel', 'FeedbackModel');
    $this->load->helper('commonqa');
  }

  /* Check if feedback is already filled */
  public function index_post()
  {
    if (isTheseParametersAvailable(array('student_id'))) {
      $dateTime = get_dates_of_quarter();
      $start_date = $dateTime['start'];
      $end_date =  $dateTime['end'];

      $student_id = $this->input->post('student_id');
      $data = $this->FeedbackModel->isFeedbackFilled($student_id, $start_date, $end_date);
      if (!$data) {
        $response['error'] = false;
        $response['message'] = "You can add feedback";
        $httpStatus = REST_Controller::HTTP_OK;
      } else {
        $response['error'] = true;
        $response['message'] = "You have already added the feedback. We will let you know when its open again.";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }

    $this->response($response, $httpStatus);
  }

  /* Add Feedback */
  public function add_post()
  {
    $jsonArray = json_decode(file_get_contents('php://input'), true);
    foreach ($jsonArray['feedback'] as $value) {
      $student_id = $value['student_id'];
      $teacher_id = $value['teacher_id'];
      $rating = $value['rating'];
      $feedback = $value['feedback'];
      $response['error'] = $this->FeedbackModel->add_feedback($student_id, $teacher_id, $rating, $feedback);
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
}
