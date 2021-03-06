package com.sip.api.domains.availableClass;

import com.sip.api.domains.TimeTrackable;
import com.sip.api.domains.activity.Activity;
import com.sip.api.domains.timeslot.Timeslot;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "available_class")
public class AvailableClass extends TimeTrackable {

    /**
     * The activity that is dictated for the class
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_id", nullable = false, referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Activity activity;

    /**
     * The timeslot that the class is available in
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeslot_id", nullable = false, referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Timeslot timeslot;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AvailableClass that = (AvailableClass) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}