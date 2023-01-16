package com.example.javamasters2.repository;

import com.example.javamasters2.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<Album, Integer> {

    Album findAlbumByAlbumName(String name);

    @Query("select a from Album a where a.albumName = :name")
    Album findAlbumByAlbumNameWithJpql(String name);

    @Query(value = "select * from album where album_name = :name", nativeQuery = true)
    Album findAlbumByAlbumNameWithNativeQuery(String name);
}
