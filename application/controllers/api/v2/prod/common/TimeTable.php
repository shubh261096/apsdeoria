<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';

class TimeTable extends REST_Controller
{

    public function __construct()
    {
        parent::__construct();
        $this->load->database();
        $this->load->model('api/v2/prod/TimeTableModel', 'TimeTableModel');
        $this->load->model('api/v2/prod/CommonModel', 'CommonModel');
        $this->load->helper('commonprod');
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
            $response['message'] = "Timetable is not available.";
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

    // Student API
    public function events_get() 
    {
        $response = array();
        $student_id = $this->session->userdata('user_id');
        $class_id = $this->CommonModel->getClassIdFromStudentId($student_id);
    
        // Fetch timetable data for one week
        $timetable = $this->get_timeTableByWeek($class_id);
        $data = $timetable['timetable'];
    
        // Check if data is not empty
        if ($data !== false) {
            $events = [];
    
            // Loop through the timetable and repeat for every week
            for ($week = 1; $week <= 52; $week++) {  // Loop for 52 weeks of the year
                foreach ($data as $key => $field) {
                    $start_time = $field->start_time;
                    $end_time = $field->end_time;
                    $day = $field->day;
                    
                    // Ensure valid start and end times for conversion
                    $start_datetime = $this->convertToDateTime($day, $start_time);
                    $end_datetime = $this->convertToDateTime($day, $end_time);

                    // Generate random backgroundColor
                    $randomColor = $this->getRandomColor();
                    
                    // Add week number to the event
                    // Convert the start and end datetime to the correct week of the year
                    $start_datetime = $this->adjustForWeek($start_datetime, $week);
                    $end_datetime = $this->adjustForWeek($end_datetime, $week);
    
                    // Create event for FullCalendar
                    $events[] = [
                        'id' => $field->id . '-' . $week,  // Ensure unique ID by appending week number
                        'title' => $field->subject_id ? $field->subject_id->name . ' (' . $field->teacher_id->fullname . ')' : 'No Subject', // Subject name as title
                        'start' => $start_datetime, // Start time in YYYY-MM-DD HH:MM:SS format
                        'end' => $end_datetime, // End time in YYYY-MM-DD HH:MM:SS format
                        'backgroundColor' => $randomColor, // Customize the color if needed
                        'borderColor' => $randomColor // Same color for border
                    ];
                }
            }
    
            // Send the response with all events for the year
            echo json_encode($events);
        } else {
            // Handle the case when no timetable data is available
            echo json_encode(['error' => 'No timetable data found']);
        }
    }

    // Function to generate random color
    private function getRandomColor() {
        $letters = '0123456789ABCDEF';
        $color = '#';
        for ($i = 0; $i < 6; $i++) {
            $color .= $letters[rand(0, 15)]; // Generate a random hexadecimal digit
        }
        return $color;
    }
        
    /**
     * Adjust the given datetime for the specific week of the year.
     * This function adds the required number of days to shift the date for the correct week.
     */
    private function adjustForWeek($datetime, $week) {
        // Convert datetime to a timestamp
        $timestamp = strtotime($datetime);
        
        // Adjust the date by adding the appropriate number of weeks (7 days per week)
        $new_timestamp = strtotime("+".($week - 1)." week", $timestamp);
        
        // Return the new datetime in 'YYYY-MM-DD HH:MM:SS' format
        return date('Y-m-d H:i:s', $new_timestamp);
    }

    
    private function convertToDateTime($day, $time)
    {
        // Get the current week day (e.g., 'Monday', 'Tuesday')
        $week_days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
        $current_day_index = date('w'); // Get current day of the week (0 for Sunday, 6 for Saturday)
        
        // Find the difference between today and the provided day of the week
        $day_diff = array_search($day, $week_days) - $current_day_index;
    
        // If the day is in the past for the current week, move it to next week
        if ($day_diff < 0) {
            $day_diff += 7;
        }
    
        // Get today's date and add the number of days to get the correct weekday
        $event_date = strtotime("+$day_diff days");
    
        // Combine event date with the provided time to create a full datetime
        $formatted_datetime = date('Y-m-d', $event_date) . " " . $time;
    
        return $formatted_datetime;    
    }
}
