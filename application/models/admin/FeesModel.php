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

	/** Getting Student Class Id  from student_id*/
	public function getClassId($student_id)
	{
		$query = $this->db->select("class_id")
			->where('id', $student_id)
			->get('student');
		if ($query->num_rows()) {
			return $query->row();
		}
	}

	/** Getting Fees Id from class_id */
	public function getFeesId($class_id)
	{
		$query = $this->db->select("id")
			->where('class_id', $class_id)
			->get('fees');
		if ($query->num_rows()) {
			return $query->row();
		}
	}

	/** function to insert fees receipt */
	public function addFees($array)
	{
		$array['status'] = 'paid';
		$array['date_paid'] = date("Y-m-d");
		return $this->db->insert('fee_receipt', $array);
	}

	/** function to get fee detail by fee_id */
	public function editFees($fees_id)
	{
		$q = $this->db->select("*")
			->where('id', $fees_id)
			->get('fee_receipt');
		return $q->row();
	}

	/** function to update fee detail by fee_id and array */
	public function updateFees($fees_id, array $fees)
	{
		return $this->db
			->where('id', $fees_id)
			->update('fee_receipt', $fees);
	}


	public function deleteStudent($fees_id)
	{
		return $this->db
			->where('id', $fees_id)
			->delete('fee_receipt');
	}
}
