<?php

class LoginModel extends CI_model{

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

    public function validate_login($email,$password)
    {
    	$q = $this->db->where(['email'=>$email, 'password'=>$password])
    				  ->from('login')	
    				  ->get();

    	if($q->num_rows()){
    		return $q->row();
       	} else{
       		return FALSE;
       	}
    }
}


?>