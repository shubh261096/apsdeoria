<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Home extends CI_Controller
{
	public function __construct()
	{
		parent::__construct();
	}

	public function index()
	{
		$this->load->template('home');
	}

	public function about()
	{
		$this->load->template('about');
	}

	public function events()
	{
		$this->load->template('events');
	}

	public function gallery()
	{
		$this->load->template('gallery');
	}

	public function privacy()
	{
		$this->load->template('privacy-policy');
	}
}
