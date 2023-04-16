
const url = 'https://asd.co.in/';
const words = ['apple', 'banana', 'cherry', 'orange', 'pear'];

const headers = {
    'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
    'accept-encoding': 'gzip, deflate, br',
    'accept-language': 'en-US,en;q=0.9',
    'cache-control': 'max-age=0',
    'content-length': '1177',
    'content-type': 'multipart/form-data; boundary=----WebKitFormBoundary7TAz05wRRzPAPpjw',
    'origin': 'https://sakshigogia.co.in',
    'referer': 'https://sakshigogia.co.in/',
    'sec-ch-ua': '"Google Chrome";v="111", "Not(A:Brand";v="8", "Chromium";v="111"',
    'sec-ch-ua-mobile': '?1',
    'sec-ch-ua-platform': '"Android"',
    'sec-fetch-dest': 'document',
    'sec-fetch-mode': 'navigate',
    'sec-fetch-site': 'same-origin',
    'sec-fetch-user': '?1',
    'upgrade-insecure-requests': '1',
    'user-agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Mobile Safari/537.36'
};

const data = {
    'wpforms[fields][8][first]': generateRandomWord(),
    'wpforms[fields][8][last]': generateRandomWord(),
    'wpforms[fields][9]': generateRandomWord() + '@gmail.com',
    'wpforms[fields][2]': 'message:' + generateRandomWord(),
    'wpforms[hp]': '',
    'wpforms[id]': '6',
    'wpforms[author]': '1',
    'wpforms[post_id]': '617',
    'wpforms[submit]': 'wpforms-submit',
    'wpforms[token]': 'df59563b89598e06c1b00723a319a25d'
};

async function sendRequest() {
    try {
        const response = await axios.post(url, data, { headers });
        console.log(`Response status: ${response.status}`);
    } catch (error) {
        console.error(error);
    }
}

async function run() {
    for (let i = 1; i <= 1000; i++) {
        console.log(`Sending request ${i}`);
        await sendRequest();
    }
}



function generateRandomWord() {
    const randomIndex = Math.floor(Math.random() * words.length);
    return words[randomIndex];
}

function initR() {
    run();
}
