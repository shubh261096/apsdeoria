<?php
class SubjectModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getSubject()
	{
		$this->db->select("*");
		$this->db->from('subject');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function getSyllabus($subject_id)
	{
		$this->db->select("syllabus");
		$this->db->from('subject');
		$this->db->where('id', $subject_id);
		$query = $this->db->get();
		if ($query->num_rows()) {
			return $query->row();
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

	public function addSubject($array)
	{
		$sql = 'SELECT id FROM subject WHERE id = "' . $array['id'] . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows() > 0) {
			return FALSE;
		} else {
			return $this->db->insert('subject', $array);
		}
	}

	public function editSubject($subject_id)
	{
		$q = $this->db->select("*")
			->where('id', $subject_id)
			->get('subject');
		return $q->row();
	}

	public function updateSubject($subject_id, array $subject)
	{
		return $this->db
			->where('id', $subject_id)
			->update('subject', $subject);
	}


	public function deleteSubject($subject_id)
	{
		return $this->db
			->where('id', $subject_id)
			->delete('subject');
	}

}