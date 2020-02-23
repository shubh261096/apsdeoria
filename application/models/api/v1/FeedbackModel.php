<?php

class FeedbackModel extends CI_model
{
    public function __construct()
    {
        parent::__construct();
        $this->load->database();
    }

    /* Query to check if feedback is already added by student_id*/
    public function isFeedbackFilled($student_id, $start_date, $end_date)
    {
        $sql = 'SELECT id FROM feedback WHERE student_id = "' . $student_id . '"  AND date BETWEEN "' . $start_date . '" AND "' . $end_date . '"';
        $query = $this->db->query($sql);
        if ($query->num_rows() > 0) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /* Query for add feedback */
    public function add_feedback($student_id, $teacher_id, $rating, $feedback)
    {
        // set the timezone first
        if (function_exists('date_default_timezone_set')) {
            date_default_timezone_set("Asia/Kolkata");
        }
        $data = array(
            'student_id' => $student_id,
            'teacher_id' => $teacher_id,
            'rating' => $rating,
            'feedback' => $feedback,
            'date' =>  date("Y-m-d H:i:s")
        );
        return $this->db->insert('feedback', $data);
    }
}
