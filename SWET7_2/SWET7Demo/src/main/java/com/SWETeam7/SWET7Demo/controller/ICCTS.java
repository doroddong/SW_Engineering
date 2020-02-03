package com.SWETeam7.SWET7Demo.controller;

import com.SWETeam7.SWET7Demo.domain.ImageItem;
import com.SWETeam7.SWET7Demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jnlp.DownloadService;
import java.util.List;

@RestController
public class ICCTS {
    @Autowired
    private ImageService imageService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/ICCTS/getImageIDs/{pageNum}")
    public List<ImageItem> getImageItems(@PathVariable int pageNum){
        return imageService.getImageItems(pageNum);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/ICCTS/uploadImage")
    public void requestImageUpload(@RequestBody ImageItem imageItem){
        imageService.enterItem(imageItem);
        return;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/ICCTS/downloadImage/{imageID}/{userID}")
    public void requestImageDownload(@PathVariable long imageID, String userID){
        String serverUrl = imageService.getImageURL(imageID, userID);
        if(serverUrl != null){
            imageService.imageDownload(serverUrl);
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/ICCTS/searchImages/{imageTitle}")
    public List<ImageItem> searchImages(@PathVariable String imageTitle){
        return imageService.searchImages(imageTitle);
    }
}
