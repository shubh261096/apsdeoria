<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Admission extends CI_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model('AdmissionModel');
	}

	public function index()
	{
		$data['class'] = array(
			'Play Group' => 'Play Group', 'Nursery' => 'Nursery', 'LKG' => 'LKG', 'UKG' => 'UKG', 'Class 1' => 'Class 1', 'Class 2' => 'Class 2', 'Class 3' => 'Class 3',
			'Class 4' => 'Class 4', 'Class 5' => 'Class 5', 'Class 6' => 'Class 6', 'Class 7' => 'Class 7', 'Class 8' => 'Class 8'
		);
		$this->load->template('admission', $data);
	}

	public function add()
	{
		$this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
		if ($this->form_validation->run('add_admission_rules') == FALSE) {
			$data['class'] = array(
				'Play Group' => 'Play Group', 'Nursery' => 'Nursery', 'LKG' => 'LKG', 'UKG' => 'UKG', 'Class 1' => 'Class 1', 'Class 2' => 'Class 2', 'Class 3' => 'Class 3',
				'Class 4' => 'Class 4', 'Class 5' => 'Class 5', 'Class 6' => 'Class 6', 'Class 7' => 'Class 7', 'Class 8' => 'Class 8'
			);
			$this->load->template('admission', $data);
		} else {
			$post = $this->input->post();
			$class = $post['class'];
			if (array_key_exists('pay_online', $post)) {
				$post['payment_method'] = "pay_online";
				unset($post['pay_online']);
				if ($class == 'Play Group' || $class == 'Nursery' || $class == 'LKG' || $class == 'UKG') {
					if (!$this->AdmissionModel->addAdmission($post)) {
						$this->session->set_flashdata('feedback', 'Oops! There is a problem. Please Try Again!');
						$this->session->set_flashdata('feedback_class', 'alert-danger');
						return redirect('admission');
					} else {
						redirect('https://imjo.in/JQ7UME');
					}
				} else {
					if (!$this->AdmissionModel->addAdmission($post)) {
						$this->session->set_flashdata('feedback', 'Oops! There is a problem. Please Try Again!');
						$this->session->set_flashdata('feedback_class', 'alert-danger');
						return redirect('admission');
					} else {
						redirect('https://imjo.in/U6qKSf');
					}
				}
			}
			if (array_key_exists('pay_upi', $post)) {
				$post['payment_method'] = "pay_upi";
				unset($post['pay_upi']);
				if (!$this->AdmissionModel->addAdmission($post)) {
					$this->session->set_flashdata('feedback', 'Oops! There is a problem. Please Try Again!');
					$this->session->set_flashdata('feedback_class', 'alert-danger');
					return redirect('admission');
				} else {
					if ($class == 'Play Group' || $class == 'Nursery' || $class == 'LKG' || $class == 'UKG') {
						$data['payment'] = "5700";
					} else {
						$data['payment'] = "6800";
					}
					$this->load->view('includes/header');
					$this->load->view('pay_upi', $data);
				}
			}
		}
	}
}
