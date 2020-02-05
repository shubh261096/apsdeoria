<?php

class LoginModel extends CI_model
{

	var $db;

	public function __construct()
	{
		parent::__construct();
		$this->db = $this->load->database('default', TRUE);
	}

	/* Query for validation */
	public function validate_login($id, $password)
	{
		$q = $this->db->where(['id' => $id, 'password' => $password])
			->from('login')
			->get();

		if ($q->num_rows()) {
			return $q->row();
		} else {
			return FALSE;
		}
	}

	/* Query for return true if status is 1 */
	public function get_userStatus($id, $type)
	{
		$sql = 'SELECT status FROM ' . $type . ' WHERE id="' . $id . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows()) {
			if ($query->row()->status == 1) {
				return TRUE;
			} else {
				return FALSE;
			}
		} else {
			return FALSE;
		}
	}

	/** Query to validate user by id and DOB */
	public function validate_user($id, $dob)
	{
		$sql = 'SELECT id FROM `student` WHERE id="' . $id . '" AND  dob="' . $dob . '" AND status=1';
		$query = $this->db->query($sql);
		if ($query->num_rows()) {
			return TRUE;
		} else {
			return FALSE;
		}
	}

	/** Query to reset password */
	public function reset_password($id, $password)
	{
		$this->db->set('password', $password);
		$this->db->where('id', $id);
		$this->db->update('login');
		return ($this->db->affected_rows() > 0) ? TRUE : FALSE;
	}
}
