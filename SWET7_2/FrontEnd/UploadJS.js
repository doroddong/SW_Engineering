function makeNewItem(list, ImageTitle, ImageDescription, ImagePrice) {
    var date = new Date();
    var id = "" + date.getHours() + date.getMinutes() + date.getSeconds() + date.getMilliseconds();

    var listItem = document.createElement('div');
    listItem.id = 'li_' + id;
    listItem.setAttribute("width","304");
    listItem.setAttribute("height","228");

    var span1 = document.createElement('span');
    span1.innerText = "Title " + ImageTitle + "\n";

    var span2 = document.createElement('span');
    span2.innerText = "Description :" + ImageDescription + "\n";

    var span3 = document.createElement('span');
    span3.innerText = "Price : " + ImagePrice + "\n";


    var file = document.getElementById('image');
    var filePath = file.value;

    console.log(filePath);

    listItem.appendChild(span1);
    listItem.appendChild(span2);
    listItem.appendChild(span3);
    listItem.appendChild(image);
    
    var ex1 = JSON.stringify($("form").serializeObject());
    alert(ex1);

    list.appendChild(listItem);
}


var Image;

var file = document.getElementById('image');
file.onchange = function(e) {
    var fileReader = new FileReader();
    fileReader.readAsDataURL(e.target.files[0]);
    fileReader.onload = function(e) { 
      //console.log(e.target.result+"");
      Image = e.target.result;
    }
    //console.log(Image);
  }

  //console.log(Image);



var inputSubmit =document.getElementById("submit");
    
inputSubmit.onclick = function () {
    var ImageItemTitle = document.getElementById('title');
    var ImageTitle = ImageItemTitle.value
    //console.log(ImageTitle);

    var ImageItemDescription = document.getElementById('description');
    var ImageDescription = ImageItemDescription.value;
    //console.log(ImageDescription);
    var ImageItemPrice = document.getElementById('price');
    var ImagePrice = ImageItemPrice.value;
    ImagePrice*=1;
    //console.log(ImagePrice);

    var uploadImage = {
        "title" : ImageTitle,
        "description" : ImageDescription,
        "price" : ImagePrice,
        "imageID" : -1,
        "localUrl": Image,
        "author":"1234",
        "serverUrl":"server",
        
    }

    var JSUploadImage = JSON.stringify(uploadImage);
    //console.log(JSUploadImage);
    //console.log(JSON.stringify(JSUploadImage));
    $.ajax({
        url:"http://localhost:8088/ICCTS/uploadImage",
        dataType:'json',
        type:"POST",
        data: JSUploadImage,
        processData : true,
        contentType: 'application/json; charset=UTF-8',
        
    })
    
    inputSubmit.focus();
    inputSubmit.select();
}
