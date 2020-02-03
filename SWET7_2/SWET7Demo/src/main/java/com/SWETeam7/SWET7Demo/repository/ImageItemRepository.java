package com.SWETeam7.SWET7Demo.repository;
import com.SWETeam7.SWET7Demo.domain.ImageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Repository
public class ImageItemRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertImageItem(ImageItem imageItem){
        mongoTemplate.insert(imageItem);
    }

    public long countAll(){
        Query query = new Query();
        //query.addCriteria(Criteria.where("imageID").all());
        return mongoTemplate.count(query, ImageItem.class);
    }

    public ImageItem findOneImageItemByID(long imageID){
        Query query = new Query();
        query.addCriteria(Criteria.where("imageID").is(imageID));
        return mongoTemplate.findOne(query, ImageItem.class);
    }

    public List<ImageItem> findAllImageItems(){
        List<ImageItem> allImages = mongoTemplate.findAll(ImageItem.class);
        return allImages;
    }
/*
    public List<ImageItem> findImageItemsByTitle(String imageTitle){
        Query query = new Query();
        query.addCriteria(Criteria.where("imageTitle").is(imageTitle));
        List<ImageItem> foundImages = mongoTemplate.find(query, ImageItem.class);
        return foundImages;
    }
 */
}
