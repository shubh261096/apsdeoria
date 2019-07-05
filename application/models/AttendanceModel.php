<?php

class AttendanceModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for getting attendance by month */
    public function get_attendanceByMonth($student_id,$month, $year) {
        $sql = 'SELECT * FROM attendance WHERE student_id = "'.$student_id.'" AND MONTH(date) = '.$month.' AND YEAR(date) = '.$year.' ORDER BY date';
        $query = $this->db->query($sql);
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }   

    /* Query for getting classes a teacher teachers by teacher_id */
    public function get_classByTeacher($teacher_id, $day) {
        $sql = 'SELECT id as timetable_id, class_id FROM timetable WHERE teacher_id = "'.$teacher_id.'" AND day = "'.$day.'" GROUP BY class_id';
        $query = $this->db->query($sql);
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }

    /* Query for getting student list by class_id */
    public function get_studentByClass($class_id) {
        $sql = 'SELECT id, fullname FROM student WHERE class_id = "'.$class_id.'" AND status=1';
        $query = $this->db->query($sql);
        if($query->num_rows()){
            return $query->result();
        }else{
            return NULL;
        }
    }

    /* Query for getting attendance from attendance table by student_id and date */
    public function get_attendanceByStudent($student_id, $date) {
        $sql = 'SELECT date, id, timetable_id, student_id, status, remarks FROM attendance WHERE student_id = "'.$student_id.'" AND date = "'.$date.'" GROUP BY date';
        $query = $this->db->query($sql);
        if($query->num_rows()){
            return $query->row  ();
        }else{
            return NULL;
        }
    }

    /* Query for add attendance */
    public function add_attendance($date, $student_id, $status, $remarks, $timetable_id) {
        $data = array(
            'date'=>$date,
            'student_id'=>$student_id,
            'status'=>$status,
            'remarks'=>$remarks,
            'timetable_id'=>$timetable_id
        );
        return $this->db->insert('attendance',$data);
    }

     /* Query for add attendance */
     public function edit_attendance($id, $date, $student_id, $status, $remarks, $timetable_id) {
        $data = array(
            'date'=>$date,
            'student_id'=>$student_id,
            'status'=>$status,
            'remarks'=>$remarks,
            'timetable_id'=>$timetable_id
        );
        $this->db->where('id', $id);
        return $this->db->update('attendance',$data);
    }
}
