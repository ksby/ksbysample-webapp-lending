package ksbysample.webapp.lending.entity;

import org.seasar.doma.*;

/**
 * ???
 */
@Entity
@Table(name = "lending_app")
public class LendingApp {

    /** */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lending_app_id")
    Long lendingAppId;

    /** */
    @Column(name = "status")
    String status;

    /** */
    @Column(name = "lending_user_id")
    Long lendingUserId;

    /** */
    @Column(name = "approval_user_id")
    Long approvalUserId;

    /** */
    @Version
    @Column(name = "version")
    Long version;

    /**
     * Returns the lendingAppId.
     *
     * @return the lendingAppId
     */
    public Long getLendingAppId() {
        return lendingAppId;
    }

    /**
     * Sets the lendingAppId.
     *
     * @param lendingAppId the lendingAppId
     */
    public void setLendingAppId(Long lendingAppId) {
        this.lendingAppId = lendingAppId;
    }

    /**
     * Returns the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the lendingUserId.
     *
     * @return the lendingUserId
     */
    public Long getLendingUserId() {
        return lendingUserId;
    }

    /**
     * Sets the lendingUserId.
     *
     * @param lendingUserId the lendingUserId
     */
    public void setLendingUserId(Long lendingUserId) {
        this.lendingUserId = lendingUserId;
    }

    /**
     * Returns the approvalUserId.
     *
     * @return the approvalUserId
     */
    public Long getApprovalUserId() {
        return approvalUserId;
    }

    /**
     * Sets the approvalUserId.
     *
     * @param approvalUserId the approvalUserId
     */
    public void setApprovalUserId(Long approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}