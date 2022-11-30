/**
 * script.js
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

// Time it takes for a session to become invalid
const sessionMaxTime = 15 * 60 * 1000;

const indexPage = "/";
const loginPage = "/Login";
const suggestPage = "//Suggestion?term=";

var frame;

window.onload = function () {
	var urlStr = window.location.href;
	var urlPattern = urlStr.substring(urlStr.lastIndexOf("/"));
	// Can only timeout if on a user restricted page
	if (urlPattern != indexPage && urlPattern != loginPage) {
		// Display session timeout warning message 1 minute from timeout
		window.setTimeout(AlertSessionTimeout, sessionMaxTime - 60 * 1000);
		// Auto redirect user 5 seconds after timeout
		window.setTimeout(SessionTimeout, sessionMaxTime+5000);
	}

	// Change which inputs are shown based on category selected
	var categorySelect =  document.getElementById('categorySelect');
	if (categorySelect != null) {
		SetCategory(categorySelect);
		categorySelect.addEventListener('change', function(e) {
			SetCategory(categorySelect);
		});
	}

	// Call displayFrame whenever Suggested Articles iFrame is loaded
	frame = document.getElementById('suggested-articles');
	if (frame != null) {
		frame.addEventListener('load', function(e) {
			displayFrame();
		});
		// Start as hidden
		frame.style.display = "none";
	}
}

// Method to validate Email and Password Login
function validateLogin() {
	if (document.getElementById("displayMessage") != null) {
		RemoveMessage("email");
		RemoveMessage("password");
	}
	
	var email = document.getElementById('email');
	var pass = document.getElementById('password');

	// Regex to check email format
	if (!/^[cC][0-9]{7}@uon.edu.au$/.test(email.value))
	{
		DisplayMessage("Please use your university email. (cXXXXXXX@uon.edu.au)", "email");
		return false;
	}
	// Check if pass matches email or if it contains User ID
	else if (pass.value == email.value || pass.value == email.value.substring(0,8))
	{
		DisplayMessage("Password can't contain student number.", "password");
		return false;
	}
	// Continue with form
	else
	{
		return true;
	}
}

// Method to change visibility of Category based inputs
function SetCategory(categorySelect) {
	switch (categorySelect.value) {
		case "network":
			document.getElementById('network').classList.remove('d-none');
			document.getElementById('software').classList.add('d-none');
			document.getElementById('hardware').classList.add('d-none');
			document.getElementById('email').classList.add('d-none');
			document.getElementById('account').classList.add('d-none');
			break;
		case "software":
			document.getElementById('network').classList.add('d-none');
			document.getElementById('software').classList.remove('d-none');
			document.getElementById('hardware').classList.add('d-none');
			document.getElementById('email').classList.add('d-none');
			document.getElementById('account').classList.add('d-none');
			break;
		case "hardware":
			document.getElementById('network').classList.add('d-none');
			document.getElementById('software').classList.add('d-none');
			document.getElementById('hardware').classList.remove('d-none');
			document.getElementById('email').classList.add('d-none');
			document.getElementById('account').classList.add('d-none');
			break;
		case "email":
			document.getElementById('network').classList.add('d-none');
			document.getElementById('software').classList.add('d-none');
			document.getElementById('hardware').classList.add('d-none');
			document.getElementById('email').classList.remove('d-none');
			document.getElementById('account').classList.add('d-none');
			break;
		case "account":
			document.getElementById('network').classList.add('d-none');
			document.getElementById('software').classList.add('d-none');
			document.getElementById('hardware').classList.add('d-none');
			document.getElementById('email').classList.add('d-none');
			document.getElementById('account').classList.remove('d-none');
			break;
		default:
			document.getElementById('network').classList.add('d-none');
			document.getElementById('software').classList.add('d-none');
			document.getElementById('hardware').classList.add('d-none');
			document.getElementById('email').classList.add('d-none');
			document.getElementById('account').classList.add('d-none');
			break;
	}
}

// Method to warn user of session timeout
function AlertSessionTimeout() {
	CreateMessage("info", "Your session will expire in 1 minute.");
}

// Method to redirect user if session has timed out
function SessionTimeout() {
	// Form used to redirect user and display error message
	var errorForm = document.createElement("form");
	errorForm.setAttribute('method',"post");
	errorForm.setAttribute('action',"Timeout");
	document.body.appendChild(errorForm);

	var errorInput = document.createElement("input");
	errorInput.setAttribute('type',"text");
	errorInput.setAttribute('name',"errorMessage");
	errorInput.setAttribute('value', "Your session has timed out.");
	errorForm.appendChild(errorInput);

	var errorSubmit = document.createElement("input");
	errorSubmit.setAttribute('type',"submit");
	errorSubmit.setAttribute('value',"Submit");
	errorForm.appendChild(errorSubmit);

	errorForm.submit();
}

// Form used to redirect user and display error message
function CreateMessage(type, message) {
	// Choose alert class color
	switch (type) {
		case "danger":
			type = "alert-danger";
			break;
		case "warning":
			type = "alert-warning";
			break;
		case "progress":
			type = "alert-progress";
			break;
		case "info":
			type = "alert-info";
			break;
		case "success":
			type = "alert-success";
			break;
	}

	var containerDiv = document.createElement("div");
	containerDiv.setAttribute('class',"container pt-2");
	
	// Add message after navBar
	var navBar = document.getElementById("navBar");
	navBar.parentNode.insertBefore(containerDiv, navBar.nextSibling);

	var alertDiv = document.createElement("div");
	alertDiv.setAttribute('class',"alert " + type + " alert-dismissible fade show text-center");
	alertDiv.setAttribute('role',"alert");
	alertDiv.innerHTML = message;
	containerDiv.appendChild(alertDiv);

	var alertButton = document.createElement("button");
	alertButton.setAttribute('type',"button");
	alertButton.setAttribute('class',"close");
	alertButton.setAttribute('data-dismiss',"alert");
	alertButton.setAttribute('aria-label',"Close");
	alertButton.innerHTML = "<span aria-hidden='true'>&times;</span>";
	alertDiv.appendChild(alertButton);
}

// Method to display error messages on login page
function DisplayMessage(message, inputId) {
	if (document.getElementById("displayMessage") == null) {
		// Add message after placeAfterId
		var input = document.getElementById(inputId);
		input.setAttribute('data-toggle',"tooltip");
		input.setAttribute('data-html',"true");
		input.setAttribute('data-placement',"bottom");
		input.setAttribute('title',"<span class='mx-1 fas fa-exclamation-triangle'></span> "+message+"");

		var containerDiv = document.createElement("div");
		containerDiv.id = "displayMessage";
		input.parentNode.insertBefore(containerDiv, input.nextSibling);

		$("#"+inputId).tooltip('show');
		input.addEventListener('keydown', function(e) {
			this.removeEventListener('keydown', arguments.callee);
			RemoveMessage(this.id);
		})
	}
}

function RemoveMessage(inputId) {
	var message = document.getElementById("displayMessage");
	if (message != null) {
		message.parentNode.removeChild(message);
		$("#"+inputId).tooltip('dispose');
	}
}




// Check if iFrame should make a GET request to Suggestion controller
function suggestArticles(url) {
	var descriptionElement = document.getElementById('title');
	var description = descriptionElement.value;
	// Only try displaying Suggestions if title is atleast 3 chars otherwise hide iFrame
	if (description.length >= 3) {
		loadFrame(url, description);
	} else {
		hideFrame();
	}
}

// Make iFrame send a GET request to Suggestion controlller
function loadFrame(url, description) {
	frame.src = url + suggestPage + encodeURIComponent(description);
}

// Hide iFrame from page
function hideFrame(url, description) {
	frame.style.display = "none";
}

// Check if iFrame should be shown or not
function displayFrame() {
	frame = document.getElementById('suggested-articles');
	var frameDoc = (frame.contentDocument) ? frame.contentDocument : frame.contentWindow.document;
	// Element with Id of empty only placed into html if not suggested articles
	if (frameDoc.getElementById('empty') != null) {
		frame.style.display = "none";
	} else {
		frame.style.display = "initial";
	}
};