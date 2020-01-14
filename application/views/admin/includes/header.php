<?php
defined('BASEPATH') or exit('No direct script access allowed'); ?>
<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>APSZONE | Dashboard</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->

  <link rel="stylesheet" href="<?php echo base_url(); ?>bootstrap/css/bootstrap.min.css">

  <link rel="shortcut icon" href="<?php echo base_url(); ?>dist/img/only_logo.png" />
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>dist/css/AdminLTE.min.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>dist/css/skins/_all-skins.min.css">
  <!-- iCheck -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/iCheck/flat/blue.css">
  <!-- Morris chart -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/morris/morris.css">
  <!-- jvectormap -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/jvectormap/jquery-jvectormap-1.2.2.css">
  <!-- Date Picker -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/datepicker/datepicker3.css">
  <!-- Time picker -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/timepicker/bootstrap-timepicker.min.css">
  <!-- Daterange picker -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/daterangepicker/daterangepicker.css">
  <!-- bootstrap wysihtml5 - text editor -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">

  <!-- DataTables -->
  <link rel="stylesheet" href="<?php echo base_url(); ?>plugins/datatables/dataTables.bootstrap.css">

  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>

  <script type="text/javascript" src="//code.jquery.com/jquery-1.10.2.min.js"></script>


</head>

<body class="hold-transition skin-blue sidebar-mini">
  <div class="wrapper">

    <header class="main-header">
      <!-- Logo -->
      <a href="#" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><b>A</b>PS</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><b>APS</b>Zone</span>
      </a>
      <!-- Header Navbar: style can be found in header.less -->
      <nav class="navbar navbar-static-top">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
          <span class="sr-only">Toggle navigation</span>
        </a>

        <div class="navbar-custom-menu">
          <ul class="nav navbar-nav">

            <li class="dropdown user user-menu">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                <img src="<?php echo base_url(); ?>dist/img/user2-160x160.jpg" class="user-image" alt="User Image">
                <span class="hidden-xs">Shubham Agrawal</span>
              </a>
              <ul class="dropdown-menu">
                <!-- User image -->
                <li class="user-header">
                  <img src="<?php echo base_url(); ?>dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                  <p>
                    Shubham Agrawal- Developer
                  </p>
                </li>
                <li class="user-footer">
                  <div class="pull-left">
                    <a href="#" class="btn btn-default btn-flat">Profile</a>
                  </div>
                  <div class="pull-right">
                    <a href="<?php echo base_url('admin/logout'); ?>" class="btn btn-default btn-flat">Sign out</a>
                  </div>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </nav>
    </header>
    <!-- Left side column. contains the logo and sidebar -->

    <aside class="main-sidebar">
      <!-- sidebar: style can be found in sidebar.less -->
      <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
          <div class="pull-left image">
            <img src="<?php echo base_url(); ?>dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
          </div>
          <div class="pull-left info">
            <p>Shubham Agrawal</p>
            <a href="#"><i class="fa fa-circle text-success"></i>Online</a>
          </div>
        </div>
        <!-- search form -->
        <form action="#" method="get" class="sidebar-form">
          <div class="input-group">
            <input type="text" name="q" class="form-control" placeholder="Search...">
            <span class="input-group-btn">
              <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
              </button>
            </span>
          </div>
        </form>
        <ul class="sidebar-menu">
          <li class="header">APPLICATION CONTENT</li>

          <li><a href="<?php echo base_url('dashboard'); ?>"><i class="fa fa-dashboard"></i> <span>Dashboard</span></a></li>
          <li><a href="<?php echo base_url('teacher'); ?>"><i class="fa fa-user"></i> <span>Teachers</span></a></li>
          <li><a href="<?php echo base_url('student'); ?>"><i class="fa fa-user"></i> <span>Students</span></a></li>
          <li><a href="<?php echo base_url('classes'); ?>"><i class="fa fa-users"></i> <span>Classes</span></a></li>
          <li><a href="<?php echo base_url('subject'); ?>"><i class="fa fa-book"></i> <span>Subject</span></a></li>
          <li><a href="<?php echo base_url('fees'); ?>"><i class="fa fa-credit-card"></i> <span>Fees</span></a></li>
          <li><a href="<?php echo base_url('timetable'); ?>"><i class="fa fa-calendar"></i> <span>Timetable</span></a></li>
          <li><a href="<?php echo base_url('login'); ?>"><i class="fa fa-sign-in"></i> <span>Login Credentials</span></a></li>
          <li><a href="<?php echo base_url('parents'); ?>"><i class="fa fa-users"></i> <span>Parents</span></a></li>
          <li><a href="<?php echo base_url('homework'); ?>"><i class="fa fa-book"></i> <span>Homework</span></a></li>
          <li><a href="<?php echo base_url('notification'); ?>"><i class="fa fa-bell"></i> <span>Notification</span></a></li>
          <li><a href="<?php echo base_url('feedback'); ?>"><i class="fa fa-comments"></i> <span>Feedback</span></a></li>
          <li><a href="<?php echo base_url('learn'); ?>"><i class="fa fa-youtube-play"></i> <span>Learning Videos</span></a></li>

          <li class="header">SCHOOL CONTENT</li>
          <li><a href="<?php echo base_url('expense'); ?>"><i class="fa fa-credit-card"></i> <span>Expenses & Accounts</span></a></li>
      </section>
      <!-- /.sidebar -->
    </aside>