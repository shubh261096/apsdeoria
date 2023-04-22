// -------------------------------------------------------------------- //
// --This code is for Free version where no phone number is required--- //
// -------------------------------------------------------------------- //


// Define a function to handle the media query changes
function handleViewportChange(event, container) {
  if (event.matches) {
    // Handle viewport change for small screens
    container.style.position = 'absolute';
    container.style.bottom = '16px';
    container.style.top = 'auto';
  } else {
    // Handle viewport change for large screens
    container.style.position = 'fixed';
    container.style.bottom = 'auto';
    container.style.top = '16px';
  }
}


// Define a function to create the button and image
function createOverlayButtonWithImage(onFreeMessageSent, onFreeMessageReceived) {
  const container = document.createElement('div');
  container.style.backgroundColor = '#ffffff';
  container.style.borderRadius = '5px';
  container.style.display = 'flex';
  container.style.boxShadow = '0px 4px 6px rgba(0, 0, 0, 0.3)';
  container.style.flexDirection = 'column';
  container.style.zIndex = '999';
  container.style.position = 'absolute';
  container.style.top = '16px';
  container.style.right = '-100%';
  container.style.width = 'auto';
  container.style.height = 'auto';
  container.style.margin = 'auto';
  container.style.transition = 'all 0.5s ease-in-out';
  container.style.transform = 'translateX(100%)';

  // Wait for 5 seconds before showing the container and animating it to the right position
  setTimeout(() => {
    container.style.right = '16px';
    container.style.transform = 'translateX(0)';
  }, 500);

  const mediaQuery = window.matchMedia('(max-width: 640px)');
  if (mediaQuery.matches) {
    handleViewportChange(mediaQuery, container);
    mediaQuery.addListener(handleViewportChange);
  }


  const containerHeading = document.createElement('div');
  containerHeading.style.backgroundColor = '#ffffff';
  containerHeading.style.display = 'flex';
  containerHeading.style.borderRadius = '5px';
  containerHeading.style.marginTop = '5px';
  containerHeading.style.flexDirection = 'row';
  containerHeading.style.justifyContent = 'space-between';
  containerHeading.style.alignItems = 'center';

  const app_id = window.location.hostname.split('.').slice(-2)[0];

  // Create heading element
  const heading = document.createElement('h4');
  heading.textContent = `Sign in to ${app_id} with Lazyclick`;
  heading.style.fontSize = '16px';
  heading.style.marginLeft = '10px';
  heading.style.color = '#3c4043';
  heading.style.fontFamily = 'Roboto-Medium';

  // Create cross element
  const crossimage = document.createElement('img');
  crossimage.src = 'https://lazyclick.in/assets/img/close.png';
  crossimage.alt = 'Cross Image';
  crossimage.style.width = '12px';
  crossimage.style.marginRight = '10px';
  crossimage.style.marginLeft = '10px';

  // Remove modal when clicked outside
  crossimage.addEventListener('click', function () {
    document.body.removeChild(container);
  });



  // Add heading and cross image to container
  containerHeading.appendChild(heading);
  containerHeading.appendChild(crossimage);


  // Create line element
  const line = document.createElement('hr');
  line.style.width = '100%';
  line.style.border = 'none';
  line.style.borderTop = '0.2px solid grey';
  line.style.margin = '0';

  // Add heading and line to container
  container.appendChild(containerHeading);
  // container.appendChild(crossimage);
  container.appendChild(line);

  // Create image element
  const image = document.createElement('img');
  image.src = 'https://lazyclick.in/assets/img/whatsapp.png';
  image.alt = 'My Image';
  image.style.width = '30px';
  image.style.marginRight = '10px';

  // Create text element
  const text = document.createElement('span');
  text.textContent = 'Continue with Whatsapp';
  text.style.fontSize = '16px';
  text.style.color = 'white';

  const button = document.createElement('button');
  button.style.backgroundColor = '#25d366';
  button.style.borderRadius = '5px';
  button.style.border = 'none';
  button.style.cursor = 'pointer';
  button.style.padding = '5px';
  button.style.margin = '10px';


  // Add image and button to container
  button.appendChild(image);
  button.appendChild(text);
  container.appendChild(button);

  // Add container and overlay to body
  // overlay.appendChild(containerHeading);

  // overlay.appendChild(container);
  document.body.appendChild(container);


  // Remove modal when clicked outside
  button.addEventListener('click', function () {
    initFree(onFreeMessageSent, onFreeMessageReceived);
  });

}


// Define a function to create the button and image
function createButtonWithImage(onFreeMessageSent, onFreeMessageReceived) {

  // Create image element
  const image = document.createElement('img');
  image.src = 'https://lazyclick.in/assets/img/whatsapp.png';
  image.alt = 'My Image';
  image.style.width = '30px';
  image.style.marginRight = '10px';

  // Create text element
  const text = document.createElement('span');
  text.textContent = 'Continue with Whatsapp';
  text.style.fontSize = '16px';
  text.style.color = 'white';

  const buttonContainer = document.createElement('div');
  buttonContainer.style.display = 'flex';
  buttonContainer.style.width = 'auto';
  buttonContainer.style.display = 'flex';
  buttonContainer.style.justifyContent = 'center';
  buttonContainer.style.alignItems = 'center';

  const button = document.createElement('button');
  button.style.backgroundColor = '#25d366';
  button.style.borderRadius = '5px';
  button.style.border = 'none';
  button.style.cursor = 'pointer';
  button.style.padding = '5px';
  button.style.margin = '10px';

  // Add image and button to button container
  button.appendChild(image);
  button.appendChild(text);
  buttonContainer.appendChild(button);


  // Add container and overlay to body
  document.body.appendChild(buttonContainer);

  // Remove modal when clicked outside
  button.addEventListener('click', function () {
    initFree(onFreeMessageSent, onFreeMessageReceived);
  });
}



function free_db(app_id, transaction_id, onFreeMessageReceived, firebaseConfig) {
  loadScriptsFree(function () {
    // Initialize Firebase
    firebase.initializeApp(firebaseConfig);
    // Initalize Database
    var database = firebase.database();
    var addRef = database.ref('/' + app_id + '/' + transaction_id);
    addRef.on('value', function (snapshot) {
      var data = snapshot.val();
      if (data != null) {
        onFreeMessageReceived(data);
        addRef.off("value");
        addRef.remove();
      }
    })
  });

}


// Define a function to load two JavaScript files and call a callback when both are loaded
function loadScriptsFree(callback) {

  // Create the first script element
  var scriptElem1 = document.createElement('script');
  scriptElem1.src = "https://www.gstatic.com/firebasejs/7.18.0/firebase-app.js";

  // Create the second script element
  var scriptElem2 = document.createElement('script');
  scriptElem2.src = "https://www.gstatic.com/firebasejs/7.18.0/firebase-database.js";

  var scriptElem3 = document.createElement('script');
  scriptElem3.src = "https://code.jquery.com/jquery-3.6.0.min.js";

  // Set up a counter to track the number of scripts that have loaded
  var loadedCount = 0;

  // Define a function to check if both scripts have loaded and call the callback
  function checkLoaded() {
    loadedCount++;
    if (loadedCount === 3) {
      callback();
    }
  }

  // Attach an event listener to the first script element to check when it has loaded
  scriptElem1.addEventListener('load', checkLoaded);

  // Attach an event listener to the second script element to check when it has loaded
  scriptElem2.addEventListener('load', checkLoaded);

  // Attach an event listener to the second script element to check when it has loaded
  scriptElem3.addEventListener('load', checkLoaded);

  // Get a reference to the head or body element of the HTML document
  var body = document.getElementsByTagName('body')[0];

  // Get a reference to the head or body element of the HTML document
  var head = document.getElementsByTagName('head')[0];

  // Append the first script element to the head or body element
  body.appendChild(scriptElem1);

  // Append the second script element to the head or body element
  body.appendChild(scriptElem2);

  // Append the second script element to the head or body element
  head.appendChild(scriptElem3);
}

function generateRandomVisibleUnicodes() {
  const unicodeList = [
    '\u200b', // ZERO WIDTH SPACE
    '\u200c', // ZERO WIDTH NON-JOINER
    '\u200d', // ZERO WIDTH JOINER
    '\u200e', // LEFT-TO-RIGHT MARK
  ];
  let result = '\u200e\u200c\u200d\u200b\u200c\u200d';
  for (let i = 0; i < 7; i++) {
    const randomIndex = Math.floor(Math.random() * unicodeList.length);
    result += unicodeList[randomIndex];
  }
  return encodeURIComponent(result);
}

// TODO Not opening in ios devices safari or brave browser
function openWhatsAppFree(encodedResult, app_id) {
  const phone = '15550020663';
  const url = `https://api.whatsapp.com/send?phone=${phone}&text=${encodedResult}Send this message to continue to ${app_id}`;
  window.open(url, '_blank') || window.location.assign(url);
}

function initFree(onFreeMessageSent, onFreeMessageReceived) {
  // Generate app id 
  const app_id = window.location.hostname.split('.').slice(-2)[0];
  const encodedResult = generateRandomVisibleUnicodes();

  const decodedResult = decodeURIComponent(encodedResult);

  let unicodeList = '';
  for (let i = 0; i < decodedResult.length; i++) {
    const codePoint = decodedResult.codePointAt(i).toString(16);
    unicodeList += '\\u' + '0'.repeat(4 - codePoint.length) + codePoint;
  }

  let result = unicodeList.replace(/\\/g, '');

  var form = new FormData();
  form.append("app_id", app_id);
  form.append("redirect_url", window.location.href);
  form.append("platform", "web");
  form.append("unicode_char", result);

  var settings = {
    // "url": "http://localhost/apsdeoria/api/v2/qa/test/vendor/free",
    "url": "https://www.apsdeoria.com/apszone/api/v2/qa/test/vendor/free",
    "method": "POST",
    "timeout": 0,
    "processData": false,
    "mimeType": "multipart/form-data",
    "contentType": false,
    "data": form
  };

  $.ajax(settings).
    done(function (response) {
      var dataToSend = JSON.parse(response);
      delete dataToSend['firebaseConfig'];
      onFreeMessageSent(dataToSend);
      openWhatsAppFree(encodedResult, app_id);
      var data = JSON.parse(response);
      var transaction_id = data['transaction_id'];
      var firebaseConfig = data['firebaseConfig'];
      free_db(app_id, transaction_id, onFreeMessageReceived, firebaseConfig);
    })
    .fail(function (response) {
      onFreeMessageSent(response.responseText);
    });
}



function initButton(onFreeMessageSent, onFreeMessageReceived) {
  // Create container element
  createOverlayButtonWithImage(onFreeMessageSent, onFreeMessageReceived);
  // createButtonWithImage(onFreeMessageSent, onFreeMessageReceived);
}