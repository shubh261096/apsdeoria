<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Attendance extends REST_Controller {

  public function __construct() {
    parent::__construct();
    $this->load->database();
    $this->load->model('AttendanceModel');
  }

  public function index_post() {
    if(isTheseParametersAvailable(array('student_id','month','year'))){
      $response = array();
      $student_id = $this->input->post('student_id');
      $month = $this->input->post('month');
      $year = $this->input->post('year'); 
      $response = $this->get_attendanceByMonth($student_id, $month, $year); // getting attendance by month & year
      if(!$response['error']){
          $httpStatus = REST_Controller::HTTP_OK;
      } else {
          $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
      }
    }else{
      $response['error'] = true;
      $response['message'] = "Parameters not found";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }
    
    
    $this->response($response, $httpStatus);
    } 

    private function get_attendanceByMonth($student_id, $month, $year){
        $data = $this->AttendanceModel->get_attendanceByMonth($student_id, $month, $year);
        if(!$data==false) {
            /* foreach ($data as $key => $field) {
             *    if ($field->timetable_id) {
             *    // TODO get timtable details if in future needed
             *   }   
             * } 
            */
            $response['error'] = false;
            $response['message'] = "Attendance fetched successfully";
            $response['attendance'] = $data;
        } else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }   
        return $response;
    } 
}
