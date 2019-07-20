<?php
class TimetableModel extends CI_Model
{

	public function __construct()
	{
		parent::__construct();
		$this->load->database();
	}

	public function getTimetable()
	{
		$this->db->select("*");
		$this->db->from('timetable');
		$query = $this->db->get();
		if ($query->num_rows() > 0) {
			return $query->result();
		}
	}

	public function addTimetable($array)
	{
		$array['status'] = 1;
		return $this->db->insert('timetable', $array);
	}

	public function editTimetable($timetable_id)
	{
		$q = $this->db->select("*")
			->where('id', $timetable_id)
			->get('timetable');
		return $q->row();
	}

	public function updateTimetable($timetable_id, array $timetable)
	{
		return $this->db
			->where('id', $timetable_id)
			->update('timetable', $timetable);
	}


	public function deleteTimetable($timetable_id)
	{
		return $this->db
			->where('id', $timetable_id)
			->delete('timetable');
	}
}