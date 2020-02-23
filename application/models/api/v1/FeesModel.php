<?php

class FeesModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for getting fees by period and class */
    public function get_feesYearly($student_id, $year) {
        $sql = 'SELECT fees_id, student_id, due_amount, fees_paid, year, period, date_paid, status
                    FROM fee_receipt FR JOIN fees F ON FR.fees_id = F.id 
                        WHERE FR.student_id="'.$student_id.'" AND FR.year="'.$year.'" ORDER BY date_paid';
        $query = $this->db->query($sql);
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }   

}
