<?php
class FeesModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	/* Getting fee receipt from fee_receipt table */
	public function getFeeReceipt()
	{
		$this->db->select("*");
		$this->db->from('fee_receipt');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function getClass()
	{
		$result = $this->db->select('id, name')->get('class')->result_array();
		$classes = array();
		foreach ($result as $r) {
			$classes[$r['id']] = $r['name'];
		}
		return $classes;
	}

	public function addStudent($array)
	{
		$sql = 'SELECT id FROM student WHERE id = "' . $array['id'] . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows() > 0) {
			return FALSE;
		} else {
			$array['status'] = 1;
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
}
