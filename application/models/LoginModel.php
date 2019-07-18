<?php

class LoginModel extends CI_model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
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
		if ($type == 'parent') {
			$type = 'student'; // TODO remove this login type in future 
		}
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
}
