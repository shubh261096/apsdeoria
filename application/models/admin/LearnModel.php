<?php
class LearnModel extends CI_Model
{
	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	/* Getting all the videos from learn table */
	public function getAllVideos()
	{
		$this->db->select("*");
		$this->db->from('learn');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	/* Getting class details from subject_id */
	public function getClassDetails($subject_id)
	{
		$sql = 'SELECT cls.id, cls.name
                    FROM class cls JOIN subject sub ON cls.id = sub.class_id
                        WHERE sub.id="' . $subject_id . '"';
		$query = $this->db->query($sql);
		if ($query->num_rows() > 0) {
			return $query->row();
		} else {
			return FALSE;
		}
	}

	/** Getting class details */
	function getClassList()
	{
		$query = $this->db->get('class');
		return $query;
	}

	/** Getting Subjects filter from class_id*/
	public function getSubjectFromClass($class_id)
	{
		$query = $this->db->get_where('subject', array('class_id' => $class_id));
		return $query->result();
	}

	/** function to insert video receipt */
	public function addVideo($array)
	{
		$array['status'] = '1';
		return $this->db->insert('learn', $array);
	}

	/** function to get fee detail by fee_id */
	public function editVideo($learn_id)
	{
		$q = $this->db->select("*")
			->where('id', $learn_id)
			->get('learn');
		return $q->row();
	}

	/** function to update fee detail by learn_id and array */
	public function updateVideo($learn_id, array $learn)
	{
		return $this->db
			->where('id', $learn_id)
			->update('learn', $learn);
	}


	public function deleteVideo($learn_id)
	{
		return $this->db
			->where('id', $learn_id)
			->delete('learn');
	}
}
