package com.concretepage.controller;

import com.concretepage.entity.Album;
import com.concretepage.entity.Photo;
import com.concretepage.service.IAlbumService;
import com.concretepage.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("album")
public class AlbumController {

    @Autowired
    private IAlbumService albumService;
    @Autowired
    private IPhotoService photoService;

    @RequestMapping(value="albums", method= RequestMethod.GET)
    public ResponseEntity<List<Album>> getAlbumsByUserId(
            @RequestParam(value = "user_id") Integer id) {
        List<Album> list = albumService.getAllAlbum(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value="album", method= RequestMethod.GET)
    public ResponseEntity<Album> getAlbumById(
            @RequestParam(value = "album_id") Integer id) {
        Album userInfo = albumService.getAlbumById(id);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PostMapping("album")
    public ResponseEntity<Void> addAlbum(Album album) {
        boolean flag = albumService.addAlbum(album);
        if (!flag) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value="album")
    public ResponseEntity<List<Photo>> deleteAlbum(
            @RequestParam(value = "album_id") Integer albumId) {
        List<Photo> photoList = photoService.getAllPhotos(albumId);
        for(Photo photo: photoList){
            photoService.deletePhoto(photo.getPhotoId());
        }
        String albumPathOnServer = albumService.getAlbumPathOnServer(albumId);
        new File(albumPathOnServer).delete();
        albumService.deleteAlbum(albumId);
        return new ResponseEntity<>(photoList, HttpStatus.OK);
    }
}
