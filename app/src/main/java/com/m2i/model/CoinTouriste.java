package com.m2i.model;

/**
 * Created by Formation on 18/01/2018.
 */

public class CoinTouriste {
    private String nom;
    private String id;
    private Double lat;
    private Double lng;

    public CoinTouriste() {
    }

    public String getNom() {
        return nom;
    }

    public CoinTouriste setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getId() {
        return id;
    }

    public CoinTouriste setId(String id) {
        this.id = id;
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public CoinTouriste setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public Double getLng() {
        return lng;
    }

    public CoinTouriste setLng(Double lng) {
        this.lng = lng;
        return this;
    }
}
