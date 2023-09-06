package ru.practicum.stat.service.model;

import lombok.*;
import ru.practicum.stat.service.util.Generated;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "endpoint_hit")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    @Id
    @Column(name = "endpoint_hit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "app")
    String app;

    @Column(name = "uri")
    String uri;

    @Column(name = "ip")
    String ip;

    @Column(name = "time_stamp")
    LocalDateTime timestamp;

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndpointHit)) return false;
        EndpointHit that = (EndpointHit) o;
        return Objects.equals(getUri(), that.getUri()) && Objects.equals(getIp(), that.getIp()) && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getIp(), getTimestamp());
    }
}
