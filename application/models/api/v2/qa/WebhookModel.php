<?php

class WebhookModel extends CI_model
{
    var $db;

    public function __construct()
    {
        parent::__construct();
        $this->db = $this->load->database('qa_db', TRUE);
    }

    // -------------------------------------------------------------------- //
    // -------------------------Common Method------------------------------ //
    // -------------------------------------------------------------------- //

    /* Query for adding whatsapp webhook response */
    public function addMeesageReceivedFromWhatsapp($array)
    {
        return $this->db->insert('whatsapp', $array);
    }





    /** Quesry to get call back URL to send back the payload data received from WhatsApp */
    public function get_appId($message_id, $message_number)
    {
        $sql = ' SELECT vtb.vendor_app_id FROM vendor AS vtb INNER JOIN whatsapp AS wtb ON vtb.vendor_app_id = wtb.app_id WHERE wtb.message_id = "' . $message_id . '" AND wtb.message_number ="' . $message_number . '" ';
        $query = $this->db->query($sql);
        if ($query->num_rows() > 0) {
            return $query->row()->vendor_app_id;
        } else {
            return NULL;
        }
    }

    /* Query for getting class etails by class_id */
    public function isVendorAvailable($app_id)
    {
        $query = $this->db->select('vendor_app_id')
            ->where(['vendor_app_id' => $app_id])
            ->from('vendor')
            ->get();
        if ($query->num_rows()) {
            return $query->row();
        } else {
            return FALSE;
        }
    }

    /** Quesry to get call back URL to send back the payload data received from WhatsApp */
    public function get_Status($transaction_id)
    {
        $val = 0;
        $sql = ' SELECT vtb.vendor_return_url, vtb.vendor_name, vtb.vendor_app_id, wtb.transaction_id  FROM vendor AS vtb INNER JOIN whatsapp AS wtb ON vtb.vendor_app_id = wtb.app_id WHERE wtb.transaction_id = "' . $transaction_id . '" AND wtb.status ="' . $val . '" ';
        $query = $this->db->query($sql);
        if ($query->num_rows() > 0) {
            return $query->row();
        } else {
            return NULL;
        }
    }

    /** Query to update status once a response has been sent */
    public function update_status_txnId($transaction_id)
    {
        $update_rows = array('status' => 1);
        $this->db->where('transaction_id', $transaction_id);
        $this->db->update('whatsapp', $update_rows);
    }

    /* Query for adding click button otpless response */
    public function add_click_button($array)
    {
        return $this->db->insert('otpless_whatsapp', $array);
    }

    /* Query for adding vendor if not exists */
    public function add_vendor($array)
    {
        return $this->db->insert('vendor', $array);
    }


    

    public function deleteTransactionId($transaction_id)
    {
        return $this->db
            ->where('transaction_id', $transaction_id)
            ->delete('otpless_whatsapp');
    }




    // -------------------------------------------------------------------- //
    // --------This code is for Pro Version Yes Button Deeplink------------ //
    // -------------------------------------------------------------------- //

    /**  
     * Query for adding whastsapp webhook resppnse with transaction id so that later
     * when the click on the link we can verify with the transaction id and send the data to a webhook/js
     * real-time and redirect user to its website
     * */
    public function addMessageSentOnWhatsappProVendor($array)
    {
        return $this->db->insert('whatsapp', $array);
    }

    /**  
     * Query to get redirect url to send back to the website stored in whatsapp db
     * Also send the data to the webhook url stored in vendor db
     * */

    public function verifyTranIdWhatsappProVendor($transaction_id)
    {
        $val = 0;
        $sql = ' SELECT vtb.vendor_app_id, vtb.vendor_webhook_url, wtb.message_number, wtb.transaction_id, wtb.redirect_url FROM vendor AS vtb INNER JOIN whatsapp AS wtb ON vtb.vendor_app_id = wtb.app_id WHERE wtb.transaction_id = "' . $transaction_id . '" AND wtb.status ="' . $val . '" ';
        $query = $this->db->query($sql);
        if ($query->num_rows() > 0) {
            return $query->row();
        } else {
            return NULL;
        }
    }

    /** Query to update status once a response has been sent */
    public function updateTranIdWhatsappProVendor($transaction_id)
    {
        $update_rows = array('status' => 1);
        $this->db->where('transaction_id', $transaction_id);
        $this->db->update('whatsapp', $update_rows);
    }





    // -------------------------------------------------------------------- //
    // --------This code is for Free Version Yes Button Deeplink----------- //
    // -------------------------------------------------------------------- //

    /** Query to get call back url to send back to received from WhatsApp */
    public function verifyTranIdWhatsappFreeVendor($transaction_id)
    {
        $val = 0;
        $sql = ' SELECT app_id, redirect_url, transaction_id, wa_name, wa_number, platform  FROM otpless_whatsapp  WHERE transaction_id = "' . $transaction_id . '" AND status ="' . $val . '" ';
        $query = $this->db->query($sql);
        if ($query->num_rows() > 0) {
            return $query->row();
        } else {
            return NULL;
        }
    }

    /** Query to update status once a response has been sent */
    public function updateTranIdWhatsappFreeVendor($transaction_id)
    {
        $update_rows = array('status' => 1);
        $this->db->where('transaction_id', $transaction_id);
        $this->db->update('otpless_whatsapp', $update_rows);
    }

    /** Query to get transaction id to send message to a link to user as soon as we get a response */
    public function getTransactionIdWhatsappFreeVendor($unicode_char, $number, $name)
    {
        $sql = ' SELECT transaction_id  FROM otpless_whatsapp WHERE unicode_char = "' . $unicode_char . '" ';
        $query = $this->db->query($sql);


        $data = array(
            'wa_number' => $number,
            'wa_name' => $name
        );
        $this->db->where('unicode_char', $unicode_char);
        $this->db->update('otpless_whatsapp', $data);


        if ($query->num_rows() > 0) {
            return $query->row();
        } else {
            return NULL;
        }
    }




    // -------------------------------------------------------------------- //
    // --------This code is for Pro Version Yes/No Reply------------------- //
    // -------------------------------------------------------------------- //

    /** Query to get webhook url to send back the payload data received from WhatsApp */
    public function getWebhookUrlWhatsappProV2($message_id, $message_number)
    {
        $val = 0;
        $sql = ' SELECT vtb.vendor_webhook_url, vtb.vendor_app_id, wtb.transaction_id  FROM vendor AS vtb INNER JOIN whatsapp AS wtb ON vtb.vendor_app_id = wtb.app_id WHERE wtb.message_id = "' . $message_id . '" AND wtb.message_number ="' . $message_number . '" AND wtb.status ="' . $val . '" ';
        $query = $this->db->query($sql);
        if ($query->num_rows() > 0) {
            return $query->row();
        } else {
            return NULL;
        }
    }

    /** Query to update status once a response has been sent */
    public function updateStatusFromMessageId($message_id)
    {
        $update_rows = array('status' => 1);
        $this->db->where('message_id', $message_id);
        $this->db->update('whatsapp', $update_rows);
    }
}
