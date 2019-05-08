<?php

class AttendanceModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for getting attendance by month */
    public function get_attendanceByMonth($student_id,$month, $year) {
        $sql = 'SELECT * FROM attendance WHERE student_id = "'.$student_id.'" AND MONTH(date) = '.$month.' AND YEAR(date) = '.$year.'';
        $query = $this->db->query($sql);
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }   

}


?>