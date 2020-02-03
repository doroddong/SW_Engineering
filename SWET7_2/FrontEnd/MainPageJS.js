var uploadlist = document.getElementById('imagelist');

$('.click').click(function () {
  console.log(this.value);

  while (uploadlist.hasChildNodes()) {
    uploadlist.removeChild(uploadlist.firstChild);
  }

  $.ajax({

    url: "http://localhost:8088/ICCTS/getImageIDs/" + this.value,
    type: "GET",
    dataType: 'text',
    success: function (result) {
      //console.log(result);

      var jsonData = JSON.parse(result);
      console.log(jsonData);

      //console.log(jsonData[0]);
      //console.log(jsonData.length);
      var ImageItemList = new Array();
      var jsonlength = 0;

      for (i = 0; i < jsonData.length; i++) {
        var ImageItem = new Object(jsonData[i]);
        if (ImageItem.title) {
          jsonlength++;
        }
      }
      //console.log(jsonlength);

      for (var i = 0; i < jsonData.length; i++) {

        var ImageItem = new Object(jsonData[i]);
        ImageItemList.push(ImageItem);
        //console.log(ImageItem.serverUrl);
      }
      var buttonList = new Array();

      for (var i = 0; i < jsonlength; i++) {
        //console.log(jsonlength);
        var preview = document.createElement('tr');
        preview.setAttribute("class", "preview");
        var img = document.createElement('img');
        var title = document.createTextNode(ImageItemList[i].title);
        //title=ImageItemList[i].title;
        //console.log(title);
        var button = document.createElement('button');
        var buttonText = document.createTextNode("Download!!");
        button.appendChild(buttonText);
        button.id = ImageItemList[i].imageID;
        button.value = button.id;
        //console.log(button.value);
        buttonList.push(button);
        button.onclick = function () {
          //console.log(this.value);
          $.ajax({
            url: "http://localhost:8088/ICCTS/downloadImage/" +this.value + "/{userID}?userID=1234",
            type: "GET",
            success: function (result) {
              console.log("Download success");
            }
          })
        };
        button.value = i;
        img.src = ImageItemList[i].serverUrl;
        img.imageID = ImageItemList[i].imageID;

        preview.append(img);
        preview.appendChild(title);
        preview.append(button);
        uploadlist.append(preview);
      }
    }
  })
})

//Serach 

//$('.click').click(function(){

var search = document.getElementById("search");

search.onkeyup = function (event) {
  if (event.which == 13) {
    var searchText = search.value;

    if (!searchText || searchText == "" || searchText == " ") return false;

    //console.log(this.value);

    while (uploadlist.hasChildNodes()) {
      uploadlist.removeChild(uploadlist.firstChild);
    }

    //console.log(searchText);
    $.ajax({

      url: "http://localhost:8088/ICCTS/searchImages/" + searchText,
      type: "GET",
      dataType: 'text',
      success: function (result) {
        //console.log(result);

        var jsonData = JSON.parse(result);
        //console.log(jsonData);

        //console.log(jsonData[0]);
        //console.log(jsonData.length);
        var ImageItemList = new Array();
        var jsonlength = 0;

        for (i = 0; i < jsonData.length; i++) {
          var ImageItem = new Object(jsonData[i]);
          if (ImageItem.title) {
            jsonlength++;
          }
        }
        //console.log(jsonlength);

        for (var i = 0; i < jsonData.length; i++) {

          var ImageItem = new Object(jsonData[i]);
          ImageItemList.push(ImageItem);
          //console.log(ImageItem.serverUrl);
        }
        var buttonList = new Array();

        for (var i = 0; i < jsonlength; i++) {
          //console.log(jsonlength);
          var preview = document.createElement('tr');
          preview.setAttribute("class", "preview");
          var img = document.createElement('img');
          var title = document.createTextNode(ImageItemList[i].title);
          //title=ImageItemList[i].title;
          //console.log(title);
          var button = document.createElement('button');
          var buttonText = document.createTextNode("Download!!");
          button.appendChild(buttonText);
          button.id = ImageItemList[i].imageID;
          button.value = button.id;
          //console.log(button.value);
          buttonList.push(button);
          button.onclick = function () {
            //console.log(this.value);
            $.ajax({
              url: "http://localhost:8088/ICCTS/downloadImage/" + ImageItemList[this.value].imageID + "/{userID}?userID=1234",
              type: "GET",
              success: function (result) {
                console.log("Download success");
              }
            })

          };
          button.value = i;
          img.src = ImageItemList[i].serverUrl;
          img.imageID = ImageItemList[i].imageID;

          preview.append(img);
          preview.appendChild(title);
          preview.append(button);
          uploadlist.append(preview);
        } 
      }
    })
  }
}