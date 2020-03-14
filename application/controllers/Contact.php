<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Contact extends CI_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model('ContactModel');
	}

	public function index()
	{
		$this->load->template('contact');
	}

	public function add()
	{
		$this->form_validation->set_error_delimiters('<p class="text-danger">', '</p>');
		if ($this->form_validation->run('add_contact_rules') == FALSE) {
			$this->load->template('contact');
		} else {
			$post = $this->input->post();
			unset($post['submit']);
			if ($this->ContactModel->addContact($post)) {
				$this->session->set_flashdata('feedback', 'Thank-you for contacting us!');
				$this->session->set_flashdata('feedback_class', 'alert-success');
			} else {
				$this->session->set_flashdata('feedback', 'Oops! There is a problem. Please Try Again!');
				$this->session->set_flashdata('feedback_class', 'alert-danger');
			}
			return redirect('contact');
		}
	}
}
