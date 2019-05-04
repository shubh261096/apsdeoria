<?php

class ProfileModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for getting profile info */
    public function get_profile($id,$type) {
        if(strcasecmp($type, TEACHER) == 0) {
            $sql = 'SELECT * FROM teacher WHERE id="'.$id.'" AND status=1';
        } elseif(strcasecmp($type, PARENTS) == 0) {
            $raw_query = 'SELECT student_id FROM parent WHERE id= "'.$id.'"';
            $q = $this->db->query($raw_query);
            if($q->num_rows()){
                $student_id =  $q->row()->student_id;
                $sql = 'SELECT * FROM student WHERE id="'.$student_id.'" AND status=1';
            }else{
                return FALSE;
           }
        }    	
		$query = $this->db->query($sql);
    	if($query->num_rows()){
    		return $query->row();
       	}else{
       		return FALSE;
       	}
    }
    

    /* Query for getting parent info*/
    public function get_parentInfo($student_id){
        $query = $this->db->where('student_id', $student_id)
    				  ->from('parent')	
                      ->get();
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }

}


?>