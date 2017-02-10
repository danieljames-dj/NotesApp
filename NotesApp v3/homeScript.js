// Code for Modal used in Share
document.getElementById("myModal").style.display = 'none';
document.getElementById("modalClose").onclick = function() {
	document.getElementById("myModal").style.display = 'none';
}
document.getElementById('logout').onclick = function() {
	window.location.replace("http://localhost:9999/NotesApp/index.html");
}
var userID = localStorage.getItem("userID");
var noteID = localStorage.getItem("noteID");
var mode = localStorage.getItem("mode");
var shareID = noteID;

// Disabling edit for read only mode
if (mode == 0) {
	document.getElementById("title").disabled = true;
	document.getElementById("content").disabled = true;
}
document.getElementById('shareSubmit').onclick = function() {
	if (shareFormValid()) {
		var m = 0;
		if (document.getElementById("r2").checked == true) m=2;
		else if (document.getElementById("r3").checked == true) m=3;
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				alert(this.responseText);
			}
		}
		xhttp.open("POST", "noteServ?share=1&noteID="+shareID+"&email="+document.getElementById("mailID").value+"&mode="+m.toString(), true);
		xhttp.send();
	} else {
		alert("Please fill all the fields");
	}
};

if (noteID == -1) { // No note is selected
	document.getElementById('button3').style.display = 'none';
	document.getElementById('button4').style.display = 'none';
	document.getElementById('button5').style.display = 'none';
	document.getElementById('button1').innerHTML = 'CREATE';
	document.getElementById('button1').onclick = function() {
		if (document.getElementById("title").value == "") {
			alert("Title cannot be left blank");
		} else {
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					window.location.replace("http://localhost:9999/NotesApp/home.html");
				}
			}
			xhttp.open("POST", "noteServ?createNote=1&userID="+userID+"&title="+document.getElementById("title").value+"&content="+document.getElementById("content").value, true);
			xhttp.send();
		}
	}
	document.getElementById('button2').innerHTML = 'IMPORT';
	document.getElementById('button2').onclick = function() {
		document.getElementById('my_file').click();
		document.getElementById("my_file").addEventListener('change',function(e) {
			var file = document.getElementById("my_file").files[0];
			var textType = /text.*/;
			if (file.type.match(textType)) {
				var reader = new FileReader();
				reader.onload = function(e) {
					var xhttp = new XMLHttpRequest();
					xhttp.onreadystatechange = function() {
						if (this.readyState == 4 && this.status == 200) {
							noteID = parseInt(this.responseText);
							localStorage.setItem("noteID",noteID);
							window.location.replace("http://localhost:9999/NotesApp/home.html");
						}
					}
					var string = e.target.result;
					string = string.replace(/ /g,"%20");
					string = string.replace(/\n/g,"%0A");
					xhttp.open("POST", "noteServ?createNote=1&userID="+userID+"&title="+file.name+"&content="+string, true);
					xhttp.send();
				}
				reader.readAsText(file);
			} else {
				alert("Format not supported");
			}
		});
	}
} else { // A note is selected
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var lines = this.responseText.split('\n'), string = "";
			document.getElementById("title").value = lines[0];
			for (var i = 1; i < lines.length; i++) {
				string += lines[i] + "\n";
			}
			document.getElementById('title').innerHTML = lines[0];
			document.getElementById('content').innerHTML = string;
			document.getElementById('button1').onclick = function() {
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						alert("Saved Successfully");
						window.location.replace("http://localhost:9999/NotesApp/home.html");
					}
				}
				string = document.getElementById("content").value;
				string = string.replace(/ /g,"%20");
				string = string.replace(/\n/g,"%0A");
				xhttp.open("POST", "noteServ?updateNote=1&noteID="+noteID+"&title="+document.getElementById("title").value+"&content="+string, true);
				xhttp.send();

			}
			document.getElementById('button2').onclick = function() {
				download(document.getElementById("title").value+".txt",document.getElementById("content").value);
			}
			document.getElementById('button3').onclick = function() {
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						document.getElementById('button5').click();
					}
				}
				xhttp.open("POST", "noteServ?deleteNote=1&noteID="+noteID, true);
				xhttp.send();
			}
			document.getElementById('button4').onclick = function() {
				shareID = noteID;
				document.getElementById("myModal").style.display = 'block';
			}
			document.getElementById('button5').onclick = function() {
				localStorage.setItem("noteID",-1);
				localStorage.setItem("mode",1);
				window.location.replace("http://localhost:9999/NotesApp/home.html");
			}
			if (mode == 0) {
				document.getElementById('button1').style.display = 'none';
				document.getElementById('button3').style.display = 'none';
				document.getElementById('button4').style.display = 'none';
			} else if (mode == 2) {
				document.getElementById('button3').style.display = 'none';
				document.getElementById('button4').style.display = 'none';
			} else if (mode == 3) {
				document.getElementById('button3').style.display = 'none';
			}
		}
	}
	xhttp.open("POST", "noteServ?readNote=1&noteID="+noteID.toString(), true);
	xhttp.send();
}

// Filling the list
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		str = this.responseText;
		str = JSON.parse(str);
		var table = document.getElementById("list");
		for (var i = 0; i < str.length; i++) {
			var newRow = table.insertRow(table.rows.length);
			var cell0 = newRow.insertCell(0), cell1 = newRow.insertCell(1), cell2 = newRow.insertCell(2),
				cell3 = newRow.insertCell(3), cell4 = newRow.insertCell(4), cell5 = newRow.insertCell(5);
			var note = document.createTextNode(str[i].noteName);
			var access;
			if (str[i].mode == "4")
				access = document.createTextNode("Owner");
			else if (str[i].mode == "0")
				access = document.createTextNode("Read only");
			else if (str[i].mode == "2")
				access = document.createTextNode("Read write");
			else if (str[i].mode == "3")
				access = document.createTextNode("Co owner");
			var view = document.createElement("view"), edit = document.createElement("edit"),
				share = document.createElement("share"), delt = document.createElement("delt");
			view.innerHTML = "VIEW"; edit.innerHTML = "EDIT";
			share.innerHTML = "SHARE"; delt.innerHTML = "DELETE";
			view.className = "btn btn-default";
			view.setAttribute("id",""+i.toString());
			edit.className = "btn btn-default";
			edit.setAttribute("id",""+i.toString());
			share.className = "btn btn-default";
			share.setAttribute("id",""+i.toString());
			delt.className = "btn btn-default";
			delt.setAttribute("id",""+i.toString());
			cell0.appendChild(note); cell1.appendChild(access); cell2.appendChild(view);
			cell3.appendChild(edit); cell4.appendChild(share); cell5.appendChild(delt);
			view.onclick = function() {
				localStorage.setItem("mode",0);
				localStorage.setItem("noteID",str[parseInt(this.getAttribute("id"))].noteID);
				window.location.replace("http://localhost:9999/NotesApp/home.html");
			}
			edit.onclick = function() {
				localStorage.setItem("mode",str[parseInt(this.getAttribute("id"))].mode);
				localStorage.setItem("noteID",str[parseInt(this.getAttribute("id"))].noteID);
				window.location.replace("http://localhost:9999/NotesApp/home.html");
			}
			delt.onclick = function() {
				var id = str[parseInt(this.getAttribute("id"))].noteID;
					console.log(id);
					var xhttp = new XMLHttpRequest();
					xhttp.onreadystatechange = function() {
						if (this.readyState == 4 && this.status == 200) {
							if (noteID != str[parseInt(delt.getAttribute("id"))].noteID) {
								window.location.replace("http://localhost:9999/NotesApp/home.html");
							}
							else
								document.getElementById('button5').click();
						}
					}
					xhttp.open("POST", "noteServ?deleteNote=1&noteID="+id.toString(), true);
					xhttp.send();
			}
			share.onclick = function() {
				shareID = str[parseInt(this.getAttribute("id"))].noteID;
				document.getElementById("myModal").style.display = 'block';
			}
			if (str[i].mode == "0") {
				edit.setAttribute("disabled","disabled");
				edit.onclick = null;
				share.setAttribute("disabled","disabled");
				share.onclick = null;
				delt.setAttribute("disabled","disabled");
				delt.onclick = null;
			} else if (str[i].mode == "2") {
				share.setAttribute("disabled","disabled");
				share.onclick = null;
				delt.setAttribute("disabled","disabled");
				delt.onclick = null;
			} else if (str[i].mode == "3") {
				delt.setAttribute("disabled","disabled");
				delt.onclick = null;
			}
		}
	}
}
xhttp.open("POST", "noteServ?getNotes=1&userID="+userID.toString(), true);
xhttp.send();

function shareFormValid() {
	if (document.getElementById("mailID").value == "") return false;
	else if (document.getElementById("r1").checked == false && document.getElementById("r2").checked == false && document.getElementById("r3").checked == false)
		return false;
	else return true;
}

function download(filename, text) {
	var element = document.createElement('a');
	element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
	element.setAttribute('download', filename);
	element.style.display = 'none';
	document.body.appendChild(element);
	element.click();
	document.body.removeChild(element);
}