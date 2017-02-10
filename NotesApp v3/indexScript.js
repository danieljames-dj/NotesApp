var flagReg = 0; // Flag for errors

// Login button click listener
document.getElementById('login_button').onclick = function() {
	var email = document.getElementById('email'), password = document.getElementById('pwd');
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (xhttp.responseText == "false" ) {
				alert("Wrong Email ID/Password");
				document.getElementById("email_div").className += " has-error";
				document.getElementById("pwd_div").className += " has-error";
			} else {
				localStorage.setItem("userID", xhttp.responseText);
				localStorage.setItem("noteID", -1);
				localStorage.setItem("mode", 4);
				window.location.replace("http://localhost:9999/NotesApp/home.html");
			}
		}
	}
	var pattEmail = new RegExp("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
	var pattPassword = new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
	if (pattEmail.test(email.value) && pattPassword.test(password.value)) {
		xhttp.open("POST", "noteServ?checkLogin=1&email="+email.value+"&password="+password.value, true);
		xhttp.send();
	} else {
		alert("Invalid Email ID/Password");
		document.getElementById("email_div").className += " has-error";
		document.getElementById("pwd_div").className += " has-error";
	}
}

// Register button click listener
document.getElementById('register_button').onclick = function() {
	var name = document.getElementById('name_r'), email = document.getElementById('email_r'),
	password = document.getElementById('pwd_r'), password1 = document.getElementById('pwd_r1');
	var pattEmail = new RegExp("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
	var pattPassword = new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			putErrors(this.responseText.charAt(0));
		}
	}
	if (name.value == "") {
		putErrors('1');
	} else if (!pattEmail.test(email.value)) {
		putErrors('2');
	} else if (!pattPassword.test(password.value)) {
		putErrors('4');
	} else if (password.value != password1.value) {
		putErrors('5');
	} else {
		xhttp.open("POST", "noteServ?checkReg=1&name="+name.value+"&email="+email.value+"&password="+password.value+"&password1="+password1.value, true);
		xhttp.send();
	}
}

function putErrors(flag) {
	switch(flag) {
		case '1':
			clearErrors(flagReg);
			flagReg = 1;
			document.getElementById('name_reg_error').innerHTML = "Invalid name";
			document.getElementById("name_divr").className += " has-error";
			break;
		case '2':
			clearErrors(flagReg);
			flagReg = 2;
			document.getElementById('email_reg_error').innerHTML = "Invalid email";
			document.getElementById("email_divr").className += " has-error";
			break;
		case '3':
			clearErrors(flagReg);
			flagReg = 3;
			document.getElementById('email_reg_error').innerHTML = "User already exists";
			document.getElementById("email_divr").className += " has-error";
			break;
		case '4':
			clearErrors(flagReg);
			flagReg = 4;
			document.getElementById('password_reg_error').innerHTML = "Invalid password";
			document.getElementById("pwd_divr").className += " has-error";
			break;
		case '5':
			clearErrors(flagReg);
			flagReg = 5;
			document.getElementById('password_reg1_error').innerHTML = "Password doesn't match";
			document.getElementById("pwd1_divr").className += " has-error";
			break;
		case '6':
			clearErrors(flagReg);
			flagReg = 0;
			alert("Registration successful");
			document.getElementById("register_form").reset();
			break;
	}
}

function clearErrors(flag) {
	switch(flag) {
		case 1:
			document.getElementById('name_reg_error').innerHTML = "";
			document.getElementById("name_divr").className = document.getElementById("name_divr").className.slice(0,-9);
			break;
		case 2:
			document.getElementById('email_reg_error').innerHTML = "";
			document.getElementById("email_divr").className = document.getElementById("email_divr").className.slice(0,-9);
			break;
		case 3:
			document.getElementById('email_reg_error').innerHTML = "";
			document.getElementById("email_divr").className = document.getElementById("email_divr").className.slice(0,-9);
			break;
		case 4:
			document.getElementById('password_reg_error').innerHTML = "";
			document.getElementById("pwd_divr").className = document.getElementById("pwd_divr").className.slice(0,-9);
			break;
		case 5:
			document.getElementById('password_reg1_error').innerHTML = "";
			document.getElementById("pwd1_divr").className = document.getElementById("pwd1_divr").className.slice(0,-9);
			break;
	}
}