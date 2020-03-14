<?php
class ContactModel extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	/** function to insert contact form */
	public function addContact($array)
	{
		return $this->db->insert('contact_us', $array);
	}
}
