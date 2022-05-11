package com.sip.api.domains.reservation;

import com.sip.api.domains.TimeTrackable;
import com.sip.api.domains.availableClass.AvailableClass;
import com.sip.api.domains.user.User;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation extends TimeTrackable {
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("availableClassId")
    @Fetch(FetchMode.JOIN)
    private AvailableClass availableClass;

    @ManyToMany
    @JoinTable(name = "reservation_attendees",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_reservation_id")),
            inverseJoinColumns = @JoinColumn(name = "attendees_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_attendees_id")))
    @Fetch(FetchMode.JOIN)
    private Set<User> attendees = new java.util.LinkedHashSet<>();
}