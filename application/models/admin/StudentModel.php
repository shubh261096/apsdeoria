<?php
class StudentModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getStudent()
	{
		$this->db->select("*");
		$this->db->from('student');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function addStudent($array, $student_id)
	{
		$sql = 'SELECT id FROM student WHERE id = "' . $student_id . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows() > 0) {
			return FALSE;
		} else {
			$array['status'] = 1;
			$array['id'] = $student_id;
			return $this->db->insert('student', $array);
		}
	}

	public function editStudent($student_id)
	{
		$q = $this->db->select("*")
			->where('id', $student_id)
			->get('student');
		return $q->row();
	}

	public function updateStudent($student_id, array $student)
	{
		return $this->db
			->where('id', $student_id)
			->update('student', $student);
	}


	public function deleteStudent($student_id)
	{
		return $this->db
			->where('id', $student_id)
			->delete('student');
	}

	public function addLogin($array)
	{
		return $this->db->insert('login', $array);
	}

	public function deleteLogin($student_id)
	{
		return $this->db
			->where('id', $student_id)
			->delete('login');
	}
}
