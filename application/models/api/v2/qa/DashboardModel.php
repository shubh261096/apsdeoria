<?php

class DashboardModel extends CI_model {

	public function __construct() 
    {
        parent::__construct(); 
        $this->load->database();
    }

	/* Query for getting android dashboard ui elements*/
    public function get_dashboardUi() {
        $query = $this->db->get('dashboard_ui');
        if($query->num_rows()){
            return $query->result();
        }else{
            return FALSE;
        }
    }
}


?>