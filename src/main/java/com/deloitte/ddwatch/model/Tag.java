package com.deloitte.ddwatch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.andreinc.jbvext.annotations.str.Alpha;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Tag")
@Builder
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToMany(mappedBy = "tags")
    private Set<Project> projects = new HashSet<>();

    @NaturalId
    @Column(name = "name", length = 32)
    private String name;


    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(name);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag that = (Tag) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(name, that.name);
        return eb.isEquals();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", projects=" + projects +
                ", name='" + name + '\'' +
                '}';
    }
}
