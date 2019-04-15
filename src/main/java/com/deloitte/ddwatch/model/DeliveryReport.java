package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "DeliveryReport")
public class DeliveryReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String field1;
    private String field2;
    private String field3;

    private LocalDateTime updateDate;

    @Override
    public String toString() {
        return "DeliveryReport{" +
                "id=" + id +
                ", project=" + project +
                ", field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                ", field3='" + field3 + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
