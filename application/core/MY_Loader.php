<?php
defined('BASEPATH') or exit('No direct script access allowed');

class MY_Loader extends CI_Loader
{
    public function template($template_name, $vars = array(), $return = FALSE)
    {
        if ($return) :
            $content  = $this->view('includes/header', $vars, $return);
            $content .= $this->view($template_name, $vars, $return);
            $content .= $this->view('includes/footer', $vars, $return);
            return $content;
        else :
            $this->view('includes/header', $vars);
            $this->view($template_name, $vars);
            $this->view('includes/footer', $vars);
        endif;
    }
}
