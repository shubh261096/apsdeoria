<?php

class HomeworkModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for getting homework by date and class*/
    public function get_homeworkByDate($class_id, $date) {
        $query = $this->db->where(['class_id'=>$class_id, 'date'=>$date])
    				  ->from('homework')	
                      ->get();
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }   

}


?>