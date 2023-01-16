package com.example.javamasters2.service;

import com.example.javamasters2.model.Album;
import com.example.javamasters2.model.AlbumDetails;
import com.example.javamasters2.model.Artist;
import com.example.javamasters2.model.Shop;
import com.example.javamasters2.repository.AlbumDetailsRepository;
import com.example.javamasters2.repository.AlbumRepository;
import com.example.javamasters2.repository.ArtistRepository;
import com.example.javamasters2.repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private AlbumDetailsRepository albumDetailsRepository;
    private AlbumRepository albumRepository;
    private ShopRepository shopRepository;
    private ArtistRepository artistRepository;

    public ShopService(AlbumDetailsRepository albumDetailsRepository, AlbumRepository albumRepository, ShopRepository shopRepository, ArtistRepository artistRepository) {
        this.albumDetailsRepository = albumDetailsRepository;
        this.albumRepository = albumRepository;
        this.shopRepository = shopRepository;
        this.artistRepository = artistRepository;
    }

    public Artist saveNewArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public AlbumDetails saveNewAlbumDetails(AlbumDetails albumDetails) {
        return albumDetailsRepository.save(albumDetails);
    }

    public Album saveNewAlbum(Album album, int albumDetailsId, int artistId) {
        AlbumDetails albumDetails = albumDetailsRepository.findById(albumDetailsId)
                .orElseThrow(() -> new RuntimeException("Album details with this id is not found!"));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist with this id not found!"));

        album.setArtist(artist);
        album.setAlbumDetails(albumDetails);

        return albumRepository.save(album);
    }

    public Shop saveShop(Shop shop, List<Integer> albumIds) {
        List<Album> albumList = albumRepository.findAllById(albumIds);
        shop.setAlbumList(albumList);
        return shopRepository.save(shop);
    }

    public List<Shop> retrieveShops() {
        return shopRepository.findAll();
    }

    public Album retrieveAlbumByName(String albumName) {
        return albumRepository.findAlbumByAlbumNameWithNativeQuery(albumName);
    }
}
