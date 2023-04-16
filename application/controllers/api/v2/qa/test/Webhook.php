<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries/REST_Controller.php';
require APPPATH . 'libraries/FirebaseLib.php';


class Webhook extends REST_Controller
{

  public function __construct()
  {
    parent::__construct();
    $this->load->database();
    $this->load->model('api/v2/qa/WebhookModel', 'WebhookModel');
    $this->load->helper('commonqa');
  }

  // -------------------------------------------------------------------- //
  // ------------GET method when Facebook sends to verify---------------- //
  // -------------------------------------------------------------------- //
  public function index_get()
  {
    $arg = $this->input->server('QUERY_STRING');
    $argument = array();
    parse_str($arg, $argument);
    $mode = $argument["hub_mode"];
    $challenge = (int) $argument["hub_challenge"];
    $token = $argument["hub_verify_token"];

    if ($mode && $token) {
      if ($mode == "subscribe" && $token == "test") {
        $this->response($challenge, REST_Controller::HTTP_OK);
      } else {
        $this->response("Forbidden", REST_Controller::HTTP_BAD_REQUEST);
      }
    }
  }

  // -------------------------------------------------------------------- //
  // ------------POST method when user messages on whatsapp-------------- //
  // -------------------------------------------------------------------- //
  public function index_post()
  {
    $jsonDecodeData = json_decode(file_get_contents('php://input'), true);
    $rawData = file_get_contents('php://input');
    // $this->addMessageToDbReceivedFromWhatsapp($jsonDecodeData);
    $this->bypassProVersionMessageYesNo($jsonDecodeData);
    $this->bypassfreeVersion($rawData);
    http_response_code(REST_Controller::HTTP_OK);
  }

  // -------------------------------------------------------------------- //
  // ---Add all messages coming from whatsapp to db in json formart------ //
  // -------------------------------------------------------------------- //
  private function addMessageToDbReceivedFromWhatsapp($jsonDecodeData)
  {
    $add_array = array('message_id' => json_encode($jsonDecodeData));
    $this->WebhookModel->addMeesageReceivedFromWhatsapp($add_array);
  }

  // -------------------------------------------------------------------- //
  // --------Business Initaied Response for Yes/No Reply----------------- //
  // -------------------------------------------------------------------- //

  private function bypassProVersionMessageYesNo($data)
  {

    // Dummy Success Response WhatsApp send us when user responds

    /**{
    "object": "whatsapp_business_account",
    "entry": [
    {
    "id": "103797195987793",
    "changes": [
    {
    "value": {
    "messaging_product": "whatsapp",
    "metadata": {
    "display_phone_number": "15550020663",
    "phone_number_id": "101399809564251"
    },
    "contacts": [
    {
    "profile": {
    "name": "Shubham Agrawal"
    },
    "wa_id": "918447050052"
    }
    ],
    "messages": [
    {
    "context": {
    "from": "15550020663",
    "id": "wamid.HBgMOTE4NDQ3MDUwMDUyFQIAERgSNTgyNzIzMEI0QTUxRENBMzA4AA=="
    },
    "from": "918447050052",
    "id": "wamid.HBgMOTE4NDQ3MDUwMDUyFQIAEhgUM0E1NjJGMENFNUFGNEI4Q0MyRDMA",
    "timestamp": "1678046820",
    "type": "button",
    "button": {
    "payload": "No",
    "text": "No"
    }
    }
    ]
    },
    "field": "messages"
    }
    ]
    }
    ]
    }*/

    $response = array();
    if (
      (array_key_exists('entry', $data) && isset($data['entry'])) &&
      (array_key_exists('changes', $data['entry'][0]) && isset($data['entry'][0])) &&
      (array_key_exists('value', $data['entry'][0]['changes'][0]) && isset($data['entry'][0]['changes'][0])) &&
      (array_key_exists('messages', $data['entry'][0]['changes'][0]['value']) && isset($data['entry'][0]['changes'][0]['value']))
    ) {
      if (
        (array_key_exists('context', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0])) &&
        (array_key_exists('id', $data['entry'][0]['changes'][0]['value']['messages'][0]['context']) && isset($data['entry'][0]['changes'][0]['value']['messages'][0]['context']))
      ) {
        $message_id = $data['entry'][0]['changes'][0]['value']['messages'][0]['context']['id'];
        // echo $message_id;
      }

      if (
        (array_key_exists('button', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0])) &&
        (array_key_exists('text', $data['entry'][0]['changes'][0]['value']['messages'][0]['button']) && isset($data['entry'][0]['changes'][0]['value']['messages'][0]['button']))
      ) {
        $message_text = $data['entry'][0]['changes'][0]['value']['messages'][0]['button']['text'];
        // echo $message_text;
      }

      if (
        (array_key_exists('profile', $data['entry'][0]['changes'][0]['value']['contacts'][0]) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0])) &&
        (array_key_exists('wa_id', $data['entry'][0]['changes'][0]['value']['contacts'][0]) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0])) &&
        (array_key_exists('name', $data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']))
      ) {
        $message_number = $data['entry'][0]['changes'][0]['value']['contacts'][0]['wa_id'];
        $name = $data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']['name'];
        // echo $number;
        // echo $name;
      }

      $result = $this->WebhookModel->getWebhookUrlWhatsappProV2($message_id, $message_number);

      if (!empty($result)) {
        $this->WebhookModel->updateStatusFromMessageId($message_id);

        $response = array(
          'webhook_url' => $result->vendor_webhook_url,
          'app_id' => $result->vendor_app_id,
          'transaction_id' => $result->transaction_id,
          'message_text' => $message_text,
          'wa_number' => $message_number,
          'wa_name' => $name
        );
        $response['error'] = false;
        $httpStatus = REST_Controller::HTTP_OK;

        // $this->sendDataToWebhookUrl($response , $result->vendor_webhook_url, "");

        $this->sendDataToFirebase($response);
      } else {
        $response['error'] = true;
        $response['message'] = "User han't responded to the text yet or already responded";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Key doesn't exists";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }

    $this->response($response, $httpStatus);
  }

  public function sendDataToFirebase($response)
  {
    $url = "https://zerootp-100-default-rtdb.firebaseio.com";
    $token = "GdQu9hJkcx9dSnr8TbtbGMFbtRqNZJvfMke8FVQf";
    $path = $response['app_id'];

    $firebase = new FirebaseLib($url, $token);
    $firebase->set($path . '/' . $response['transaction_id'], $response);
  }


  // -------------------------------------------------------------------- //
  // ------------Free Version Webhook Response (User-Initiated)---------- //
  // -------------------------------------------------------------------- //
  public function bypassfreeVersion($data)
  {
    $data1 = json_decode(json_encode($data));
    $data2 = preg_replace_callback('/\\\\u([0-9a-fA-F]{4})/', function ($matches) {
      return 'u' . $matches[1] . '|';
    }, $data1);
    $lastUnderscore = strrpos($data2, "|");
    $strWithoutLast = substr($data2, 0, $lastUnderscore);
    $strWithoutLast = str_replace("|", "", $strWithoutLast);
    $result = $strWithoutLast . "|" . substr($data2, $lastUnderscore + 1);
    // $data1 = str_replace("\\u", "u", $data1);
    $data = json_decode($result, true);

    $response = array();
    if (
      (array_key_exists('entry', $data) && isset($data['entry'])) &&
      (array_key_exists('changes', $data['entry'][0]) && isset($data['entry'][0])) &&
      (array_key_exists('value', $data['entry'][0]['changes'][0]) && isset($data['entry'][0]['changes'][0])) &&
      (array_key_exists('messages', $data['entry'][0]['changes'][0]['value']) && isset($data['entry'][0]['changes'][0]['value']))
    ) {
      if (
        (array_key_exists('type', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0])) &&
        (array_key_exists('timestamp', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0]))
      ) {
        $type = $data['entry'][0]['changes'][0]['value']['messages'][0]['type'];
        $timestamp = $data['entry'][0]['changes'][0]['value']['messages'][0]['timestamp'];
        // echo $type;
        // echo $timestamp;
      }

      if (
        (array_key_exists('profile', $data['entry'][0]['changes'][0]['value']['contacts'][0]) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0])) &&
        (array_key_exists('wa_id', $data['entry'][0]['changes'][0]['value']['contacts'][0]) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0])) &&
        (array_key_exists('name', $data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']) && isset($data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']))
      ) {
        $number = $data['entry'][0]['changes'][0]['value']['contacts'][0]['wa_id'];
        $name = $data['entry'][0]['changes'][0]['value']['contacts'][0]['profile']['name'];
        // echo $number;
        // echo $name;
      }

      if (
        (array_key_exists('text', $data['entry'][0]['changes'][0]['value']['messages'][0]) && isset($data['entry'][0]['changes'][0]['value']['messages'][0])) &&
        (array_key_exists('body', $data['entry'][0]['changes'][0]['value']['messages'][0]['text']) && isset($data['entry'][0]['changes'][0]['value']['messages'][0]['text']))
      ) {
        $message_text = $data['entry'][0]['changes'][0]['value']['messages'][0]['text']['body'];
        $message_text = explode('|', $message_text);
        // echo $message_text[0];
      }

      $result = $this->WebhookModel->getTransactionIdWhatsappFreeVendor($message_text[0], $number, $name);

      if (!empty($result)) {
        $this->hitWhatsappApiFreeVresion($result->transaction_id, $number);
        $response = array(
          'message' => 'Link sent to whatsapp'
        );
        $response['error'] = false;
        $httpStatus = REST_Controller::HTTP_OK;
      } else {
        $response['error'] = true;
        $response['message'] = "User han't responded to the text yet or already responded";
        $httpStatus = REST_Controller::HTTP_OK;
      }
    } else {
      $response['error'] = true;
      $response['message'] = "Key doesn't exists";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }

    $this->response($response, $httpStatus);
  }


  private function hitWhatsappApiFreeVresion($transaction_id, $number)
  {
    $authorization_key = "Bearer EAAau0u0nhZBsBAKAAGr5N3ntnZAjCBxcwMl6i9TeSiMZCCw8AH3E0mf9vujSNGflcrFHxNHqSMC2N29TyYuop6uqWZA5jtbZAZAMmGiZA9VRVGmxm9IHSi20ENZBuDb6sOufsKJU7BvN01ofh0yupNcRA72q6K3Tg97HFY1qgckSKLC8IR2EVhHc";

    // Set POST variables
    $url = 'https://graph.facebook.com/v15.0/101399809564251/messages';


    $dataForDeeplink = "{
                    'messaging_product': 'whatsapp',
                    'recipient_type': 'individual',
                    'to': $number,
                    'type': 'template',
                    'template': {
                        'name': 'button_deeplink',
                        'language': {
                            'code': 'en'
                        },
                        'components': [
                            {
                                'type': 'button',
                                'sub_type' : 'url',
                                'index': '0', 
                                'parameters': [
                                    {
                                        'type': 'text',
                                        'text': 'verifyfree/$transaction_id'
                                    }
                                ]
                            }
                        ]
                    }
                }";
    $headers = array(
      'Authorization: ' . $authorization_key,
      'Content-Type: application/json'
    );

    // Open connection
    $ch = curl_init();

    // Set the url, number of POST vars, POST data
    curl_setopt($ch, CURLOPT_URL, $url);

    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Disabling SSL Certificate support temporarily
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    curl_setopt($ch, CURLOPT_POSTFIELDS, $dataForDeeplink);

    // Execute post
    $curl_response = json_decode(curl_exec($ch));


    /**
     * $response = 
     *          '{
     *              "messaging_product": "whatsapp",
     *              "contacts": [
     *                            {
     *                              "input": "918447050052",
     *                              "wa_id": "918447050052"
     *                            }
     *                          ],
     *              "messages": [
     *                            {
     *                              "id": "wamid.HBgMOTE4NDQ3MDUwMDUyFQIAERgSMzBFMDY4QkY3RjFDRDkyM0RBAA=="
     *                            }
     *                          ]
     *          }';
     * */

    if (
      (property_exists($curl_response, 'contacts') && isset($curl_response->contacts)) &&
      (property_exists($curl_response, 'messages') && isset($curl_response->messages))
    ) {
      $message_number = $curl_response->contacts[0]->input;
      $message_id = $curl_response->messages[0]->id;
      $response = array(
        'message_number' => $message_number,
        'transaction_id' => $transaction_id
      );

      $response['error'] = false;
      $response['message'] = "Message sent to WhatsApp";
      $httpStatus = REST_Controller::HTTP_OK;
    } else {
      $response['error'] = true;
      $response['message'] = "WhatsApp Token Expired";
      $httpStatus = REST_Controller::HTTP_BAD_REQUEST;
    }

    // Close connection
    curl_close($ch);
  }




  // Common Methods

  private function sendDataToWebhookUrl($response, $url, $headerAuthKey)
  {
    if ($headerAuthKey != null) {
      $headers = array(
        'Authorization: key=' . $headerAuthKey,
        'Content-Type: application/json'
      );
    }


    // Open connection
    $ch = curl_init();

    // Set the url, number of POST vars, POST data
    curl_setopt($ch, CURLOPT_URL, $url);

    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Disabling SSL Certificate support temporarily
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    curl_setopt($ch, CURLOPT_POSTFIELDS, $response);

    // Execute post
    $data['result'] = curl_exec($ch);
    if ($data['result'] === FALSE) {
      die('Curl failed: ' . curl_error($ch));
    }

    // Close connection
    curl_close($ch);
  }
}