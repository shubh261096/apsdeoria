<?php
class HomeworkModel extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getHomework()
	{
		$this->db->select("*");
		$this->db->from('homework');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function getHomeworkData($homework_id)
	{
		$this->db->select("data");
		$this->db->from('homework');
		$this->db->where('id', $homework_id);
		$query = $this->db->get();
		if ($query->num_rows()) {
			return $query->row();
		}
	}

	public function addHomework($array)
	{
		return $this->db->insert('homework', $array);
	}

	public function editHomework($homework_id)
	{
		$q = $this->db->select("*")
			->where('id', $homework_id)
			->get('homework');
		return $q->row();
	}

	public function updateHomework($homework_id, array $homework)
	{
		return $this->db
			->where('id', $homework_id)
			->update('homework', $homework);
	}


	public function deleteHomework($homework_id)
	{
		return $this->db
			->where('id', $homework_id)
			->delete('homework');
	}
}
