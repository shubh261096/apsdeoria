<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class TimeTable extends REST_Controller
{

    public function __construct()
    {
        parent::__construct();
        $this->load->database();
        $this->load->model('api/v2/qa/TimeTableModel', 'TimeTableModel');
        $this->load->model('api/v2/qa/CommonModel', 'CommonModel');
    }

    public function index_post()
    {
        $response = array();
        if (isTheseParametersAvailable(array('student_id', 'today'))) {
            $student_id = $this->input->post('student_id');
            $class_id = $this->CommonModel->getClassIdFromStudentId($student_id);

            $today = $this->input->post('today');
            $response = $this->get_timeTableByDay($class_id, $today);
            $httpStatus = REST_Controller::HTTP_OK;
        } elseif (isTheseParametersAvailable(array('student_id'))) {
            $student_id = $this->input->post('student_id');
            $class_id = $this->CommonModel->getClassIdFromStudentId($student_id);

            $response = $this->get_timeTableByWeek($class_id);
            $httpStatus = REST_Controller::HTTP_OK;
        } elseif (isTheseParametersAvailable(array('teacher_id', 'today'))) {
            $teacher_id = $this->input->post('teacher_id');
            /** Checking teacher status */
            if (getTeacherStatus($teacher_id)) {
                $today = $this->input->post('today');
                $response = $this->get_TeacherTimeTableByDay($teacher_id, $today);
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

    public function get_timeTableByDay($class_id, $today)
    {
        $data = $this->TimeTableModel->get_timeTableByDay($class_id, $today); // getting Timetable by day
        if (!$data == false) {
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

    /* get teacher's timetable function @param teacher_id and today*/
    public function get_TeacherTimeTableByDay($teacher_id, $today)
    {
        $data = $this->TimeTableModel->get_TeacherTimeTableByDay($teacher_id, $today); // getting Timetable by day
        if (!$data == false) {
            foreach ($data as $key => $field) {
                if ($field->class_id) {
                    $data[$key]->class_id = getClassDetails($field->class_id); // getting class details and adding it into data array
                }
                if ($field->subject_id) {
                    $data[$key]->subject_id = getSubjectDetails($field->subject_id); // getting subject details and adding it into data array
                }
                if ($field->teacher_id) {
                    $data[$key]->teacher_id = getTeacherDetails($field->teacher_id); // getting teacher details and adding it into data array
                }
            }
            $response['error'] = false;
            $response['message'] = "Teacher timetable fetched successfully";
            $response['timetable'] = $data;
        } else {
            $response['error'] = true;
            $response['message'] = "No data found";
        }
        return $response;
    }

    public function get_timeTableByWeek($class_id)
    {
        $data = $this->TimeTableModel->get_timeTableByWeek($class_id); // getting Timetable by week
        if (!$data == false) {
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
