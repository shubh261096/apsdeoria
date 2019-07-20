<?php

class DashboardModel extends CI_model{

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

    public function getTotalStudent()
    {
        return $this->db->count_all('student'); 
    }

    public function getTotalTeacher()
    {
        return $this->db->count_all('teacher'); 
    }

}
