<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Admin extends CI_Controller
{

	public function __construct()
	{
		parent::__construct();
		$this->load->model('admin/AdminModel', 'AdminModel');
	}

	public function index()
	{
		if ($this->session->userdata('user_id'))
			return redirect('admin/dashboard');
		$this->load->view('admin/login');
	}

	public function login()
	{
		$id = $this->input->post('id');
		$password = $this->input->post('password');
		$login_id = $this->AdminModel->validate_login($id, $password);
		if ($login_id) {
			$this->session->set_userdata('user_id', $login_id);
			return redirect('admin/dashboard');
		} else {
			$this->session->set_flashdata('login_failed', 'Invalid Username or Password Or You are not an admin');
			return redirect('admin/admin');
		}
	}

	public function logout()
	{
		$this->session->unset_userdata('user_id');
		return redirect('admin/admin');
	}
}