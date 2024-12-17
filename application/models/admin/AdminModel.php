<?php

class AdminModel extends CI_model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function validate_login($id, $password)
	{
		$this->db->select('l.id, l.password, l.type, 
			CASE 
				WHEN l.type = "admin" THEN "Admin" 
				WHEN l.type = "student" THEN s.fullname 
				WHEN l.type = "teacher" THEN t.fullname 
				ELSE NULL
			END as name', false) // 'false' ensures the raw SQL is used for the CASE statement
			->from('login l') // Alias for the login table
			->join('student s', 's.id = l.id AND l.type = "student"', 'left') // Join student table
			->join('teacher t', 't.id = l.id AND l.type = "teacher"', 'left') // Join teacher table
			->where(['l.id' => $id, 'l.password' => $password]);

		$q = $this->db->get();

		if ($q->num_rows() > 0) {
			return $q->row(); // Return the result as an object
		} else {
			return FALSE; // Return FALSE if no rows found
		}
	}
}
