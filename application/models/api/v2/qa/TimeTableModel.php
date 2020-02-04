<?php

class TimeTableModel extends CI_model {

	var $db;
    
    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('qa_db', TRUE);
    }

	/* Query for getting timetable by day for students */
    public function get_timeTableByDay($class_id,$today) {
        $query = $this->db->where(['class_id'=>$class_id, 'day'=>$today])
    				  ->from('timetable')	
                      ->get();
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }

    /* Query for getting timetable by day for teachers */
    public function get_TeacherTimeTableByDay($teacher_id, $today) {
        $query = $this->db->where(['teacher_id'=>$teacher_id, 'day'=>$today])
    				  ->from('timetable')
                      ->get();
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }

    /* Query for getting timetable by week */
    public function get_timeTableByWeek($class_id) {
        $query = $this->db->where(['class_id'=>$class_id])
    				  ->from('timetable')	
                      ->get();
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }
    

}
