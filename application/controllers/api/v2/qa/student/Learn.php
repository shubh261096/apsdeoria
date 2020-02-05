<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class Learn extends REST_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
        $this->load->model('api/v2/qa/LearnModel', 'LearnModel');
        $this->load->model('api/v2/qa/CommonModel', 'CommonModel');
        $this->load->helper('commonqa');
    }

    public function index_post()
    {
        $response = array();
        if (isTheseParametersAvailable(array('student_id'))) {
            $student_id = $this->input->post('student_id');

            $class_id = $this->CommonModel->getClassIdFromStudentId($student_id);
            $subject_details = getAllSubjects($class_id);

            foreach ($subject_details as $key => $field) {
                $subject_details[$key]->video = $this->LearnModel->getVideoUrl($field->id);
            }
            $response['error'] = false;
            $response['message'] = "Videos fetched successfully";
            $response['learn'] = $subject_details;
            $httpStatus = REST_Controller::HTTP_OK;
        } else {
            $response['error'] = true;
            $response['message'] = "Parameters not found";
            $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
        }

        $this->response($response, $httpStatus);
    }
}
