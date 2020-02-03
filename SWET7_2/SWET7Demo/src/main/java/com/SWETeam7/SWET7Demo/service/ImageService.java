package com.SWETeam7.SWET7Demo.service;

import com.SWETeam7.SWET7Demo.domain.*;
import com.SWETeam7.SWET7Demo.repository.ImageItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class ImageService {
    private List<User> users = new ArrayList<User>();
    //서버 상대 경로
    String serverPath = "C:/Users/dongp/OneDrive - 아주대학교/바탕 화면/Team 7 Source Code/SWET7-2/SWET7Demo/src/main/java/com/SWETeam7/SWET7Demo/images/";
    //다운로드 될 절대 경로
    String savePath = "C:/Users/dongp/OneDrive - 아주대학교/바탕 화면/Team 7 Source Code/wantedDirectory/";
    @Autowired
    private ImageItemRepository imageItemRepository = null;

    @PostConstruct
    public void loadUsers(){
        User user1 = new User();
        User.ChocoMush chocoMush1 = new User.ChocoMush();   chocoMush1.setBalance(100000);
        Record record1 = new Record();
        List<Sale> saleList1 = new ArrayList<Sale>();  List<Upload> uploadList1 = new ArrayList<Upload>();
        record1.setSaleList(saleList1);     record1.setUploadList(uploadList1);

        User user2 = new User();
        User.ChocoMush chocoMush2 = new User.ChocoMush();   chocoMush2.setBalance(200);
        Record record2 = new Record();
        List<Sale> saleList2 = new ArrayList<Sale>();  List<Upload> uploadList2 = new ArrayList<Upload>();
        record2.setSaleList(saleList2);     record2.setUploadList(uploadList2);

        user1.setUserID("1234");
        user1.setChocoMush(chocoMush1);
        user1.setRecord(record1);
        user2.setUserID("9876");
        user2.setChocoMush(chocoMush2);
        user2.setRecord(record2);
        users.add(user1);   users.add(user2);
    }

    public List<ImageItem> getImageItems(int pageNum){
        int start = (pageNum-1)*5;
        int end = pageNum*5;
        List<Long> imageIDList = new ArrayList<Long>();
        List<ImageItem> imageItems = new ArrayList<ImageItem>();
        for(long i = start; i<end; i++){
            ImageItem currentImage = imageItemRepository.findOneImageItemByID(i);
            if(currentImage != null)
                imageIDList.add(i);
                imageItems.add(currentImage);
        }
        return imageItems;
    }

    public void enterItem(ImageItem imageItem){
        //업로드 기록 저장
        int userIndex = -1;
        for(int i = 0; i<2; i++){
            if(users.get(i).getUserID().equals(imageItem.getAuthor())) {
                userIndex = i;
                break;
            }
        }
        if(userIndex == -1){ //유저아이디가 없는경우
            return;
        }
        //imageItem.getLocalUrl() == 이게 데이터 형식 (data://~~)
        //원래 : /test.png

        //이걸 파일로 변형 후 저장시켜야함
        List<Upload> uploadList = users.get(userIndex).getRecord().getUploadList();;
        Upload upload = new Upload();   upload.setImageID(imageItem.getImageID());
        upload.setUploadID(uploadList.size());

        Calendar cal = Calendar.getInstance();  upload.setUploadTime(cal);
        uploadList.add(upload);
        users.get(userIndex).getRecord().setUploadList(uploadList);
            //현재까지 업로드 시간들 출력
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd-hh:mm:ss");
        for(int i = 0; i<uploadList.size(); i++){
            String datetime = sdf.format(
                    users.get(userIndex).getRecord().getUploadList().get(i).getUploadTime().getTime());
            log.info((i+1)+" Upload Time : " + datetime);
        }

        //이미지ID할당
        long DBsize = imageItemRepository.countAll();
        imageItem.setImageID(DBsize);

        //서버URL할당
        //String imageFileName = imageItem.getLocalUrl().substring(imageItem.getLocalUrl().lastIndexOf("/")+1);
        String imageFileName = imageItem.getTitle()+".png";
        String format = imageFileName.substring(imageFileName.lastIndexOf(".")+1);

        imageItem.setServerUrl(serverPath + imageFileName);
        imageItemRepository.insertImageItem(imageItem);

        //서버에 이미지저장
        String base64Data = imageItem.getLocalUrl().substring(imageItem.getLocalUrl().indexOf(",")+1);
        FileOutputStream fos = null;
        byte[] binary = Base64.decodeBase64(base64Data);
        try{
            fos = new FileOutputStream(serverPath+imageFileName);
            fos.write ( binary , 0 , binary.length );
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //File saveFile = new File(imageItem.getServerUrl());
        //saveImage(imageItem.getLocalUrl(), saveFile, format);
    }

    public String getImageURL(long imageID, String userID){
        String url = null;
        int userIndex = -1;

        ImageItem currentImage =imageItemRepository.findOneImageItemByID(imageID);
        if(currentImage != null){
            for(int i = 0; i<2; i++){
                if(users.get(i).getUserID().equals(userID)) {
                    userIndex = i;
                    break;
                }
            }
            if(userIndex == -1){ //유저아이디가 없는경우

                return url;
            }
            //초코머시 차감
            int result = users.get(userIndex).getChocoMush().getBalance() - currentImage.getPrice();
            if(result < 0){ //잔액부족
                return url;
            }
            users.get(userIndex).getChocoMush().setBalance(result);
            log.info(users.get(userIndex).getUserID() + " user balance : " + users.get(userIndex).getChocoMush().getBalance());

            //Sale 기록
            List<Sale> saleList = users.get(userIndex).getRecord().getSaleList();;
            Sale sale = new Sale();   sale.setImageID(currentImage.getImageID());
            sale.setSaleID(saleList.size());    sale.setPrice(currentImage.getPrice());

            Calendar cal = Calendar.getInstance();  sale.setDownloadTime(cal);
            saleList.add(sale);
            users.get(userIndex).getRecord().setSaleList(saleList);
                //현재까지 다운로드 시간들 출력
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd-hh:mm:ss");
            for(int i = 0; i<saleList.size(); i++){
                String datetime = sdf.format(
                        users.get(userIndex).getRecord().getSaleList().get(i).getDownloadTime().getTime());
                log.info((i+1)+" Download Time : " + datetime);
            }
            url = currentImage.getServerUrl();
        }
        return url;
    }
    public void imageDownload(String serverUrl){
        String imageUrl = serverUrl;

        String imageFileName = serverUrl;
        String fileFormat = imageFileName.substring(imageFileName.lastIndexOf(".")+1);;
        imageFileName = imageFileName.substring(imageFileName.lastIndexOf("/")+1);
        String saveFileName = imageFileName; //다운로드 된 이미지 파일 이름

        File saveFile = new File(savePath + saveFileName);

        saveImage(imageUrl, saveFile, fileFormat);
    }
    public void saveImage(String imageUrl, File saveFile, String fileFormat) {
        //URL url = null;
        BufferedImage bi = null;
        try {
            //url = new File(imageUrl).toURI().toURL();; // 다운로드 할 이미지 URL
            File inputFile = new File(imageUrl);    //다운로드 할 이미지 파일
            bi = ImageIO.read(inputFile); // 임시이미지객체
            ImageIO.write(bi, fileFormat, saveFile); // bi ,저장할 파일 형식, 이미지파일(경로+Name)
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ImageItem> searchImages(String imageTitle){
        List<ImageItem> allImages = imageItemRepository.findAllImageItems();
        List<ImageItem> foundImages = new ArrayList<ImageItem>();
        for(int i = 0; i < allImages.size(); i++){
            String target = allImages.get(i).getTitle();
            if (target.toLowerCase().contains(imageTitle.toLowerCase())){
                foundImages.add(allImages.get(i));
            }
        }
        return foundImages;
    }
}
