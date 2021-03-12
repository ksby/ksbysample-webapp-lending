package ksbysample.webapp.lending.entity;

import org.seasar.doma.*;

/**
 * ???
 */
@SuppressWarnings({"PMD.TooManyFields"})
@Entity(metamodel = @Metamodel)
@Table(name = "user_role")
public class UserRole {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    Long roleId;

    /**
     *
     */
    @Column(name = "user_id")
    Long userId;

    /**
     *
     */
    @Column(name = "role")
    String role;

    /**
     * Returns the roleId.
     *
     * @return the roleId
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Sets the roleId.
     *
     * @param roleId the roleId
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * Returns the userId.
     *
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the userId.
     *
     * @param userId the userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the role
     */
    public void setRole(String role) {
        this.role = role;
    }
}
