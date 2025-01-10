package com.volod.streaming.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import net.datafaker.Faker;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = Video.TABLE_NAME)
public class Video extends AbstractAuditPersistable {
    public static final String TABLE_NAME = "videos";

    @Column(name = "title")
    private String title;
    @Column(name = "synopsis")
    private String synopsis;
    @Column(name = "director")
    private String director;
    @Column(name = "main_actor")
    private String mainActor;
    @Type(ListArrayType.class)
    @Column(name = "actor_cast", columnDefinition = "text[]")
    private List<String> cast;
    @Column(name = "genre")
    private String genre;
    @Column(name = "year")
    private int year;
    @Column(name = "duration")
    private long duration;
    @Column(name = "hidden")
    private boolean hidden;

    public Video() {
    }

    public Video(
            String createdBy,
            String title,
            String synopsis,
            String director,
            String mainActor,
            List<String> cast,
            String genre,
            int year,
            long duration
    ) {
        super(
                createdBy
        );
        this.title = title;
        this.synopsis = synopsis;
        this.director = director;
        this.mainActor = mainActor;
        this.cast = cast;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
    }

    public static Video random() {
        var faker = new Faker();
        return new Video(
                faker.internet().username(),
                faker.book().title(),
                faker.text().text(25, 255),
                faker.name().name(),
                faker.name().name(),
                Arrays.asList(faker.name().name(), faker.name().name(), faker.name().name()),
                faker.book().genre(),
                faker.timeAndDate().birthday().getYear(),
                faker.duration().atMostHours(3).getSeconds()
        );
    }

    public static Video random(
            String title,
            String director,
            String mainActor
    ) {
        var faker = new Faker();
        return new Video(
                faker.internet().username(),
                title,
                faker.text().text(25, 255),
                director,
                mainActor,
                Arrays.asList(faker.name().name(), faker.name().name(), faker.name().name()),
                faker.book().genre(),
                faker.timeAndDate().birthday().getYear(),
                faker.duration().atMostHours(3).getSeconds()
        );
    }

    public static List<Video> dummies1() {
        return List.of(
                Video.random("Dune 2", "Denis Villeneuve", "Timothee Chalamet"),
                Video.random("Dune", "Denis Villeneuve", "Timothee Chalamet"),
                Video.random("Goodfellas", "Martin Scorsese", "Robert De Niro"),
                Video.random("The Departed", "Martin Scorsese", "Leonardo DiCaprio"),
                Video.random("Heat", "Michael Mann", "Al Pacino")
        );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        var oEffectiveClass = o instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        var thisEffectiveClass = this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        var that = (Video) o;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : this.getClass().hashCode();
    }
}
