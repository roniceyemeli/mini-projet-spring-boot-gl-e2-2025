package com.service.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "user_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions; // Format: "USER_READ,USER_WRITE,STUDENT_CREATE"

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "is_system")
    private Boolean isSystem = false;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Business methods
    public boolean hasPermission(String permission) {
        if (permissions == null || permissions.isEmpty()) return false;
        return permissions.contains(permission);
    }

    public void addPermission(String permission) {
        if (permissions == null) permissions = "";
        if (!hasPermission(permission)) {
            permissions = permissions.isEmpty() ? permission : permissions + "," + permission;
        }
    }

    public void removePermission(String permission) {
        if (permissions != null && permissions.contains(permission)) {
            permissions = permissions.replace(permission, "").replace(",,", ",").trim();
            if (permissions.endsWith(",")) {
                permissions = permissions.substring(0, permissions.length() - 1);
            }
        }
    }
}