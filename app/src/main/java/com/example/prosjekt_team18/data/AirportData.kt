package com.example.prosjekt_team18.data

import android.location.Location
import com.google.android.gms.maps.model.LatLng

class AirportData {
    public val latCoordinates = listOf(
        (69.9779), (69.3106), (58.5159), (69.0595), (60.2918), (70.8700), (67.2683), (65.4599),
        (70.6028), (61.5858), (61.3911), (70.6800), (68.4919), (70.4866), (59.3445), (71.0087),
        (60.2120), (69.7214), (59.9693), (58.2038), (63.1139), (70.0664), (68.1535), (71.0275),
        (66.3646), (62.7464), (65.7844), (64.4725), (59.5652), (78.9278), (62.6512), (60.1976),
        (59.3976), (62.5770), (64.8392), (67.5272), (61.8290), (59.1824), (65.9594), (59.1843),
        (61.1575), (58.8804), (68.5795), (59.7930), (78.2469), (68.2450), (69.7856), (69.6819),
        (63.4583), (70.0641), (70.3564), (63.6953), (62.1800), (62.5585)
    )
    public val lngCoordinates = listOf(
        (23.3466), (16.1239), (8.7027), (18.5378), (5.2220), (29.0303), (14.3622), (12.2092),
        (29.6923), (5.0242), (5.7642), (23.6755), (16.6841), (22.1471), (5.2166), (25.9775),
        (10.3180), (29.8830), (11.0395), (8.0838), (7.8254), (24.9819), (13.6145), (27.8291),
        (14.3034), (7.2608), (13.2170), (11.5717), (9.2183), (11.8749), (9.8504), (11.1004),
        (11.3468), (11.3530), (11.1443), (12.1058), (6.1071), (10.2569), (12.4755), (9.5699),
        (7.1374), (5.6314), (15.0315), (5.3424), (15.4933), (14.6668), (20.9576), (18.9163),
        (10.9226), (29.8385), (31.0397), (9.6035), (6.0795), (6.1153)
    )
    public val airportNames = listOf(
        ("Alta lufthavn"), ("Andøya lufthavn"), ("Arendal lufthavn"), ("Bardufoss lufthavn"),
        ("Bergen lufthavn"), ("Berlevåg lufthavn"), ("Bodø lufthavn"), ("Brønnøysund lufthavn, Brønnøy"),
        ("Båtsfjord lufthavn"), ("Florø lufthamn"), ("Førde lufthamn, Bringeland"), ("Hammerfest lufthavn"),
        ("Harstad/Narvik lufthavn, Evenes"), ("Hasvik lufthavn"), ("Haugesund lufthavn, Karmøy"),
        ("Honningsvåg lufthavn, Valan"), ("Hønefoss flyplass, Eggemoen"), ("Kirkenes lufthavn, Høybuktmoen"),
        ("Kjeller flyplass"), ("Kristiansand lufthavn, Kjevik"), ("Kristiansund lufthavn, Kvernberget"),
        ("Lakselv lufthavn, Banak"), ("Leknes lufthavn"), ("Mehamn lufthavn"), ("Mo i Rana lufthavn, Røssvoll"),
        ("Molde lufthavn, Årø"), ("Mosjøen lufthavn, Kjærstad"), ("Namsos lufthavn"), ("Notodden lufthavn"),
        ("Ny-Ålesund flyplass, Havnerabben"), ("Oppdal flyplass, Fagerhaug"), ("Oslo lufthavn, Gardermoen"),
        ("Rakkestad flyplass, Åstorp"), ("Røros lufthavn"), ("Rørvik lufthavn, Ryum"),
        ("Røst lufthavn"), ("Sandane lufthamn, Anda"), ("Sandefjord lufthavn, Torp"),
        ("Sandnessjøen lufthavn, Stokka"), ("Skien flyplass, Geiteryggen"), ("Sogndal lufthavn, Haukåsen"),
        ("Stavanger lufthavn, Sola"), ("Stokmarknes lufthavn, Skagen"), ("Stord lufthamn, Sørstokken"),
        ("Svalbard lufthavn, Longyear"), ("Svolvær lufthavn, Helle"), ("Sørkjosen lufthavn"),
        ("Tromsø lufthavn, Langnes"), ("Trondheim lufthavn, Værnes"), ("Vadsø lufthavn"),
        ("Vardø lufthavn, Svartnes"), ("Ørland lufthavn"), ("Ørsta/Volda lufthamn, Hovden"),
        ("Ålesund lufthavn, Vigra")
    )

    fun locationWithin5km(selectedLocation: LatLng): Boolean {
        for (i in latCoordinates.indices) {
            val airportLocation = LatLng(latCoordinates[i], lngCoordinates[i])

            val results = FloatArray(1)
            Location.distanceBetween(
                selectedLocation.latitude, selectedLocation.longitude,
                airportLocation.latitude, airportLocation.longitude, results
            )

            if (results[0] <= 5000) {
                return false
            }

        }
        return true
    }


}