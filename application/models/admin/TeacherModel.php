<?php
class TeacherModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getTeacher()
	{
		$this->db->select("*");
		$this->db->from('teacher');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function addTeacher($array, $teacher_id)
	{
		$sql = 'SELECT id FROM teacher WHERE id = "' . $teacher_id . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows() > 0) {
			return FALSE;
		} else {
			$array['status'] = 1;
			$array['id'] = $teacher_id;
			return $this->db->insert('teacher', $array);
		}
	}

	public function editTeacher($teacher_id)
	{
		$q = $this->db->select("*")
			->where('id', $teacher_id)
			->get('teacher');
		return $q->row();
	}

	public function updateTeacher($teacher_id, array $teacher)
	{
		return $this->db
			->where('id', $teacher_id)
			->update('teacher', $teacher);
	}


	public function deleteTeacher($teacher_id)
	{
		return $this->db
			->where('id', $teacher_id)
			->delete('teacher');
	}

	public function addLogin($array)
	{
		return $this->db->insert('login', $array);
	}

	public function deleteLogin($teacher_id)
	{
		return $this->db
			->where('id', $teacher_id)
			->delete('login');
	}
}