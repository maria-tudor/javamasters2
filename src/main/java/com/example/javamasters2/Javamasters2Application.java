package com.example.javamasters2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Javamasters2Application implements CommandLineRunner {
    /**
     * Jpa -> java persistence api
     * <p>
     * JPQL -> java persistence query language
     * <p>
     * Shop - album -> many to many
     * album - album details - one to one
     * artist - album -> one to many
     *
     * @param args
     * @OneToOne, @ManyToMany, @OneToMany, @ManyToOne
     * <p>
     * relatiile se pot defini:
     * - unidirectional
     * - bidirectional
     */
/*    @Autowired
    private AlbumDetailsRepository albumDetailsRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ArtistRepository artistRepository;*/

    public static void main(String[] args) {
        SpringApplication.run(Javamasters2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        /*Artist artist1 = new Artist("Artist 1");
        Artist artist2 = new Artist("Artist2");

        artistRepository.save(artist1);
        artistRepository.save(artist2);

        AlbumDetails albumDetails1 = new AlbumDetails("Some details for album 1!");
        AlbumDetails albumDetails2 = new AlbumDetails("Some details for album 2!");

        albumDetailsRepository.save(albumDetails1);
        albumDetailsRepository.save(albumDetails2);

        Album album1 = new Album("Album1", 5);
        Album album2 = new Album("Album2", 6);

        album1.setAlbumDetails(albumDetails1);
        album2.setAlbumDetails(albumDetails2);

        album1.setArtist(artist1);
        album2.setArtist(artist2);

        albumRepository.save(album1);
        albumRepository.save(album2);

        Shop shop = new Shop("Bucuresti sector 3");

        shop.setAlbumList(List.of(album1, album2));
        shopRepository.save(shop);*/

    }
}
