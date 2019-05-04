<?php

class LoginModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for validation */
    public function validate_login($email,$password) {
    	$q = $this->db->where(['email'=>$email, 'password'=>$password])
    				  ->from('login')	
    				  ->get();

    	if($q->num_rows()){
    		return $q->row();
       	}else{
       		return FALSE;
       	}
	}
	
	/* Query for getting userId if status is 1 */
	public function get_uniqueId($email, $type) {
		$sql = 'SELECT id FROM '.$type.' WHERE email="'.$email.'" AND status=1';
		$query = $this->db->query($sql);
		if($query->num_rows()){
    		return $query->row()->id;
       	}else{
       		return FALSE;
       	}
	}
}


?>