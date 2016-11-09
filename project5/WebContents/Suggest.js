      

function AutoSuggestControl(textBox) {
   this.textBox = textBox;
   this.layer = null;
   this.origText = "";
   this.cur = -1;
   this.init();
}

AutoSuggestControl.prototype.init = function(){
   var that = this;
   
   //assign the onkeyup event handler
   this.textBox.onkeyup = function (evt) {
      evt = evt || window.event; 
      that.handleKeyUp(evt);
   };
   this.textBox.onkeydown = function (evt) {
      evt = evt || window.event;   
      that.handleKeyDown(evt);
   };
   this.textBox.onblur = function () {
      that.hideSuggestions();
   };
   this.createDropDown();
}

AutoSuggestControl.prototype.createDropDown = function() {
   var that = this;

   //create the layer and assign styles
   this.layer = document.createElement("div");
   this.layer.className = "suggestions";
   this.layer.style.visibility = "hidden";
   this.layer.style.width = this.textBox.offsetWidth;
   
   //when the user clicks on the a suggestion, get the text (innerHTML)
   //and place it into a textbox
   this.layer.onmousedown = 
   this.layer.onmouseup = 
   this.layer.onmouseover =function (evt) {
 
      evt = evt || window.event;
      var target = evt.target || evt.srcElement;
      
      if (evt.type == "mousedown") {
         that.textBox.value = target.firstChild.nodeValue;
         that.hideSuggestions();
      } else if (evt.type == "mouseover") {
         // update current index      
         var target = evt.target || evt.srcElement;
         for (var i=0; i < that.layer.childNodes.length; i++) {
            var node = that.layer.childNodes[i];
            if (target == node) {
               that.cur = i+1;
            }
         }
         that.updateSuggestions();
      } else {
         that.textBox.focus();
      }
   };

   document.body.appendChild(this.layer);
}
AutoSuggestControl.prototype.handleKeyUp = function(evt){        
  var key = evt.keyCode;

   //for backspace (8) and delete (46), shows suggestions without typeahead
   if (key == 8 || key == 46) {
      this.requestSuggestions();
   //make sure not to interfere with non-character keys
   } else if (key < 32 || (key >= 33 && key < 46) || (key >= 112 && key <= 123)) {
     //ignore
   } else {

     //request suggestions from the suggestion provider with typeahead
     this.requestSuggestions();
   }
};

AutoSuggestControl.prototype.handleKeyDown = function(evt){        
   switch(evt.keyCode) {
      case 38: //up arrow
         this.cur--;
         if(this.cur < 0) {
            this.cur = this.layer.childNodes.length;
         }
         this.updateSuggestions();
         break;
      case 40: //down arrow 
         
         this.cur++;
         if(this.cur > this.layer.childNodes.length) {
            this.cur = 0;
         }
         this.updateSuggestions();
         break;
      case 13: //enter
         this.hideSuggestions();
         break;
   }
};

AutoSuggestControl.prototype.requestSuggestions = function(){
   this.origText = this.textBox.value;
   this.cur = 0;
   
   var that = this;
   // get current textbox value
   requestSuggestions(this.origText, function(result){ 
      that.onSuggestComplete(result)
   });
}

// callback when request suggest returns
AutoSuggestControl.prototype.onSuggestComplete = function(result){ 
   
   //make sure there's at least one suggestion
   if (result.length > 0) {
      this.showSuggestions(result);
   } else {
      this.hideSuggestions();
   }
}

AutoSuggestControl.prototype.updateSuggestions = function(){
  
   // if selected at 0, show previously saved input string
   if (this.cur == 0){
      this.textBox.value = this.origText;  
      this.highlightSuggestion(null);
      
   }else if (this.layer.childNodes.length > 0 && this.cur > 0) {
      var node = this.layer.childNodes[this.cur-1];
      this.highlightSuggestion(node);
      this.textBox.value = node.firstChild.nodeValue;   
   }
}

// clear all visible highlights and only highlight the selected one
AutoSuggestControl.prototype.highlightSuggestion = function(node){
   for (var i=0; i < this.layer.childNodes.length; i++) {
      var oNode = this.layer.childNodes[i];
      if (oNode == node) {
         oNode.className = "current"
      } else if (oNode.className == "current") {
         oNode.className = "";
      }
   }
}

AutoSuggestControl.prototype.showSuggestions = function(result){        
   var div = null;
   this.layer.innerHTML = "";  //clear contents of the layer
   
   for (var i=0; i < result.length; i++) {
      div = document.createElement("div");
      div.appendChild(document.createTextNode(result[i]));
      this.layer.appendChild(div);
   }
   
   var offset = this.getLeftAndTop();
   this.layer.style.left = offset[0] + "px";
   this.layer.style.top = (offset[1] + this.textBox.offsetHeight) + "px";
   this.layer.style.visibility = "visible";
};

AutoSuggestControl.prototype.hideSuggestions = function(){        
   this.layer.style.visibility = "hidden";
};

// styling purposes
AutoSuggestControl.prototype.getLeftAndTop = function(){ 
   var node = this.textBox;
   var left = 0, top =0;
   
   while(node.tagName != "BODY") {
      left += node.offsetLeft;
      top += node.offsetTop;
      node = node.offsetParent;        
   }
   return [left, top];
}

           
function requestSuggestions(q, callback) {
   var result = [];
   console.l
   var xmlHttp = new XMLHttpRequest(); // works only for Firefox, Safari, ...
   if (xmlHttp == null) {
      alert("Your browser does not support AJAX!");
      callback([]);
      return;
   }

   var requestURL = "/eBay/suggest?q="+encodeURI(q);
   
   xmlHttp.open("GET", requestURL);
   xmlHttp.onreadystatechange = function(){
      if (xmlHttp.readyState == 4 && xmlHttp.status==200) {
         var xmlDoc = xmlHttp.responseText;
         
         var xmldom = (new DOMParser()).parseFromString(xmlDoc, 'text/xml');
         var resultList = xmldom.getElementsByTagName("suggestion");
         for (var i=0; i < resultList.length; i++) {
            result.push(resultList[i].attributes[0].nodeValue);
         }
         callback(result);

      }
   
   }
   xmlHttp.send(null);
}     
      

// initial load
window.onload = function () {
   var textBox = new AutoSuggestControl(document.getElementById("searchTextBox"));        
}

