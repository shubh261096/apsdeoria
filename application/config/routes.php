<?php
defined('BASEPATH') or exit('No direct script access allowed');

/*
| -------------------------------------------------------------------------
| URI ROUTING
| -------------------------------------------------------------------------
| This file lets you re-map URI requests to specific controller functions.
|
| Typically there is a one-to-one relationship between a URL string
| and its corresponding controller class/method. The segments in a
| URL normally follow this pattern:
|
|	example.com/class/method/id/
|
| In some instances, however, you may want to remap this relationship
| so that a different class/function is called than the one
| corresponding to the URL.
|
| Please see the user guide for complete details:
|
|	https://codeigniter.com/user_guide/general/routing.html
|
| -------------------------------------------------------------------------
| RESERVED ROUTES
| -------------------------------------------------------------------------
|
| There are three reserved routes:
|
|	$route['default_controller'] = 'welcome';
|
| This route indicates which controller class should be loaded if the
| URI contains no data. In the above example, the "welcome" class
| would be loaded.
|
|	$route['404_override'] = 'errors/page_missing';
|
| This route will tell the Router which controller/method to use if those
| provided in the URL cannot be matched to a valid route.
|
|	$route['translate_uri_dashes'] = FALSE;
|
| This is not exactly a route, but allows you to automatically route
| controller and method names that contain dashes. '-' isn't a valid
| class or method name character, so it requires translation.
| When you set this option to TRUE, it will replace ALL dashes in the
| controller and method URI segments.
|
| Examples:	my-controller/index	-> my_controller/index
|		my-controller/my-method	-> my_controller/my_method
*/
$route['default_controller'] = 'api/Login';

/** Called from header.php */
$route['dashboard'] = "admin/dashboard"; // Dashboard controller
$route['teacher'] = "admin/teacher"; // teacher controller
$route['student'] = "admin/student"; // student controller
$route['classes'] = "admin/classes"; // classes controller
$route['subject'] = "admin/subject"; // subject controller
$route['fees'] = "admin/fees"; // fees controller
$route['timetable'] = "admin/timetable"; // timetabale controller
$route['login'] = "admin/login"; // login controller
$route['parents'] = "admin/parents"; // parents controller
$route['homework'] = "admin/homework"; // homework controller
$route['notification'] = "admin/notification"; // notification controller
$route['expense'] = "admin/expense"; // Expenses controller
$route['feedback'] = "admin/feedback"; // Feedback controller
$route['learn'] = "admin/learn"; // Feedback controller
$route['lazyclick'] = "admin/lazyclick"; // Lazyclick controller

/** called from views */
$route['teacher/add'] = "admin/teacher/add"; // teacher controller add mthod
$route['teacher/edit/(:any)'] = "admin/teacher/edit/$1"; // teacher controller edit mthod
$route['teacher/update/(:any)'] = "admin/teacher/update/$1"; // teacher controller update mthod
$route['teacher/delete/(:any)'] = "admin/teacher/delete/$1"; // teacher controller delete mthod

$route['student/add'] = "admin/student/add"; // student controller add method
$route['student/insert'] = "admin/student/insert"; // student controller insert method
$route['student/edit/(:any)'] = "admin/student/edit/$1"; // student controller edit method
$route['student/update/(:any)'] = "admin/student/update/$1"; // student controller update method
$route['student/delete/(:any)'] = "admin/student/delete/$1"; // student controller delete method

$route['classes/add'] = "admin/classes/add"; // classes controller add method
$route['classes/edit/(:any)'] = "admin/classes/edit/$1"; // classes controller edit method
$route['classes/update/(:any)'] = "admin/classes/update/$1"; // classes controller update method
$route['classes/delete/(:any)'] = "admin/classes/delete/$1"; // classes controller delete method

$route['subject/add'] = "admin/subject/add"; // subject controller add method
$route['subject/insert'] = "admin/subject/insert"; // subject controller insert method
$route['subject/edit/(:any)'] = "admin/subject/edit/$1"; // subject controller edit method
$route['subject/update/(:any)'] = "admin/subject/update/$1"; // subject controller update method
$route['subject/delete/(:any)'] = "admin/subject/delete/$1"; // subject controller delete method
$route['subject/download/(:any)'] = "admin/subject/download/$1"; // subject controller download method

$route['fees/add'] = "admin/fees/add"; // fees controller add method
$route['fees/insert'] = "admin/fees/insert"; // fees controller insert method
$route['fees/edit/(:any)'] = "admin/fees/edit/$1"; // fees controller edit method
$route['fees/update/(:any)'] = "admin/fees/update/$1"; // fees controller update method
$route['fees/delete/(:any)'] = "admin/fees/delete/$1"; // fees controller delete method

$route['timetable/add'] = "admin/timetable/add"; // timetabale controller add method
$route['timetable/getSubjectFromClass'] = "admin/timetable/getSubjectFromClass"; // timetable controller add method
$route['timetable/insert'] = "admin/timetable/insert"; // timetabale controller insert method
$route['timetable/edit/(:any)'] = "admin/timetable/edit/$1"; // timetabale controller edit method
$route['timetable/update/(:any)'] = "admin/timetable/update/$1"; // timetabale controller update method
$route['timetable/delete/(:any)'] = "admin/timetable/delete/$1"; // timetabale controller delete method

$route['parents/add'] = "admin/parents/add"; // parents controller add method
$route['parents/insert'] = "admin/parents/insert"; // parents controller insert method
$route['parents/edit/(:any)'] = "admin/parents/edit/$1"; // parents controller
$route['parents/update/(:any)'] = "admin/parents/update/$1"; // parents controller
$route['parents/delete/(:any)'] = "admin/parents/delete/$1"; // parents controller

$route['homework/add'] = "admin/homework/add"; // homework controller add method
$route['homework/insert'] = "admin/homework/insert"; // homework controller adinsertd method
$route['homework/edit/(:any)'] = "admin/homework/edit/$1"; // homework controller edit method
$route['homework/update/(:any)'] = "admin/homework/update/$1"; // homework controller  update method
$route['homework/delete/(:any)'] = "admin/homework/delete/$1"; // homework controller delete method
$route['homework/download/(:any)'] = "admin/homework/download/$1"; // homework controller download method

$route['expense/add'] = "admin/expense/add"; // expense controller add mthod
$route['expense/insert'] = "admin/expense/insert"; // expense controller adinsertd method
$route['expense/delete/(:any)'] = "admin/expense/delete/$1"; // expense controller delete method

$route['learn/add'] = "admin/learn/add"; // learn controller add method
$route['learn/getSubjectFromClass'] = "admin/learn/getSubjectFromClass"; // learn controller add method
$route['learn/insert'] = "admin/learn/insert"; // learn controller adinsertd method
$route['learn/edit/(:any)'] = "admin/learn/edit/$1"; // homework controller edit method
$route['learn/update/(:any)'] = "admin/learn/update/$1"; // homework controller  update method
$route['learn/delete/(:any)'] = "admin/learn/delete/$1"; // learn controller delete method

$route['lazyclick/delete/(:any)'] = "admin/lazyclick/delete/$1"; // lazyclick controller delete method

$route['admin/logout'] = "admin/admin/logout"; // admin controller logout method
$route['admin'] = "admin/admin"; // admin controller logout method

$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;
