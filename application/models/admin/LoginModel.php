<?php
class LoginModel extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getLogin()
	{
		$this->db->select("*");
		$this->db->from('login');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}
}
