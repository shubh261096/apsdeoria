<?php
class AdmissionModel extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	/** function to insert admission form */
	public function addAdmission($array)
	{
		return $this->db->insert('admission', $array);
	}
}
