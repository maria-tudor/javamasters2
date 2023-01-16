package com.example.javamasters2.controller;

import com.example.javamasters2.model.Album;
import com.example.javamasters2.model.AlbumDetails;
import com.example.javamasters2.model.Artist;
import com.example.javamasters2.model.Shop;
import com.example.javamasters2.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    private final ShopService shopService;


    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/artist")
    public ResponseEntity<Artist> saveArtist(@RequestBody Artist artist) {
        return ResponseEntity.ok().body(shopService.saveNewArtist(artist));
    }

    @PostMapping("/albumDetails")
    public ResponseEntity<AlbumDetails> saveAlbumDetails(@RequestBody AlbumDetails albumDetails) {
        return ResponseEntity.ok().body(shopService.saveNewAlbumDetails(albumDetails));
    }

    @PostMapping("/album")
    public ResponseEntity<Album> saveAlbum(@RequestBody Album album,
                                           @RequestParam int albumDetailsId,
                                           @RequestParam int artistId) {
        return ResponseEntity.ok().body(shopService.saveNewAlbum(album, albumDetailsId, artistId));
    }

    @PostMapping
    public ResponseEntity<Shop> saveShop(@RequestBody Shop shop,
                                         @RequestParam List<Integer> albumIds) {
        return ResponseEntity.ok().body(shopService.saveShop(shop, albumIds));
    }

    @GetMapping
    public ResponseEntity<List<Shop>> retrieveShop() {
        return ResponseEntity.ok().body(shopService.retrieveShops());
    }

    @GetMapping("/album")
    public ResponseEntity<Album> retrieveByName(@RequestParam String albumName) {
        return ResponseEntity.ok().body(shopService.retrieveAlbumByName(albumName));
    }
}
