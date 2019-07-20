<?php
class ParentModel extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getParent()
	{
		$this->db->select("*");
		$this->db->from('parent');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function addParent($array, $parent_id)
	{
		$sql = 'SELECT id FROM parent WHERE id = "' . $parent_id . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows() > 0) {
			return FALSE;
		} else {
			$array['status'] = 1;
			$array['id'] = $parent_id;
			return $this->db->insert('parent', $array);
		}
	}

	public function editParent($parent_id)
	{
		$q = $this->db->select("*")
			->where('id', $parent_id)
			->get('parent');
		return $q->row();
	}

	public function updateParent($parent_id, array $parent)
	{
		return $this->db
			->where('id', $parent_id)
			->update('parent', $parent);
	}


	public function deleteParent($parent_id)
	{
		return $this->db
			->where('id', $parent_id)
			->delete('parent');
	}
}
