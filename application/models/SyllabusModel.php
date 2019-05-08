<?php

class SyllabusModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for getting syllabus by class*/
    public function get_syllabusByClass($class_id) {
        $query = $this->db->where(['class_id'=>$class_id])
    				  ->from('subject')	
                      ->get();
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }   

}


?>