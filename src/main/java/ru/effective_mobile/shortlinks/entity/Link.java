package ru.effective_mobile.shortlinks.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_url",
            nullable = false,
            unique = true)
    private String originalUrl;

    @Column(name = "short_url",
            nullable = false,
            unique = true)
    private String shortUrl;

    @Column(nullable = false,
            unique = true)
    private String alias;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Temporal(TemporalType.DATE)
    private LocalDate ttl;
}