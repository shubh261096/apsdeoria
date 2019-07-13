<?php
class ClassModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getClass()
	{
		$this->db->select("*");
		$this->db->from('class');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function addClass($array)
	{
		$sql = 'SELECT id FROM class WHERE id = "' . $array['id'] . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows() > 0) {
			return FALSE;
		} else {
			$array['status'] = 1;
			return $this->db->insert('class', $array);
		}
	}

	public function editClass($class_id)
	{
		$q = $this->db->select("*")
			->where('id', $class_id)
			->get('class');
		return $q->row();
	}

	public function updateClass($class_id, array $classes)
	{
		return $this->db
			->where('id', $class_id)
			->update('class', $classes);
	}


	public function deleteClass($class_id)
	{
		return $this->db
			->where('id', $class_id)
			->delete('class');
	}
}
