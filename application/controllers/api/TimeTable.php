<?php
defined('BASEPATH') OR exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class TimeTable extends REST_Controller {

    public function __construct() {
        parent::__construct();
        $this->load->database();
        $this->load->model('TimeTableModel');
    }

    public function index_post() {
        $response = array();
        if(isTheseParametersAvailable(array('class_id','today'))) {
            $class_id = $this->input->post('class_id');
            $today = $this->input->post('today');
            $response = $this->get_timeTableByDay($class_id, $today);
            if($response['error']){
                $httpStatus = REST_Controller::HTTP_OK;
            } else {
                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
            }
        } elseif(isTheseParametersAvailable(array('class_id'))) {
            $class_id = $this->input->post('class_id');
            $response = $this->get_timeTableByWeek($class_id);
            if($response['error']){
                $httpStatus = REST_Controller::HTTP_OK;
            } else {
                $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
            }  
        } else {
            $response['error'] = true;
            $response['message'] = "Parameters not found";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }
        
        $this->response($response, $httpStatus);
        }

    public function get_timeTableByDay($class_id, $today) {
        $data = $this->TimeTableModel->get_timeTableByDay($class_id, $today); // getting Timetable by day
        if(!$data==false) {
            foreach ($data as $key => $field) {
                if ($field->class_id) {
                    $data[$key]->class_id = getClassDetails($class_id); // getting class details and adding it into data array
                } 
                if ($field->subject_id) {
                    $data[$key]->subject_id = getSubjectDetails($field->subject_id); // getting subject details and adding it into data array
                }
                if ($field->teacher_id) {
                    $data[$key]->teacher_id = getTeacherDetails($field->teacher_id); // getting teacher details and adding it into data array
                }
            }
            $response['error'] = false;
            $response['message'] = "Timetable fetched successfully";
            $response['timetable'] = $data;
        } else{
            $response['error'] = true;
            $response['message'] = "No data found";
        }   
        return $response;
    }

    public function get_timeTableByWeek($class_id){
        $data = $this->TimeTableModel->get_timeTableByWeek($class_id); // getting Timetable by week
        if(!$data==false) {
            foreach ($data as $key => $field) {
                if ($field->class_id) {
                    $data[$key]->class_id = getClassDetails($class_id); // getting class details and adding it into data array
                } 
                if ($field->subject_id) {
                    $data[$key]->subject_id = getSubjectDetails($field->subject_id); // getting subject details and adding it into data array
                }
                if ($field->teacher_id) {
                    $data[$key]->teacher_id = getTeacherDetails($field->teacher_id); // getting teacher details and adding it into data array
                }
            }
            $response['error'] = false;
            $response['message'] = "Timetable fetched successfully";
            $response['timetable'] = $data;
        } else {
            $response['error'] = true;
            $response['message'] = "No data found";
        }
        return $response;
    }

}
