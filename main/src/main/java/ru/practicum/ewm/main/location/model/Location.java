package ru.practicum.ewm.main.location.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_lat")
    private float lat;

    @Column(name = "location_lot")
    private float lon;
}
