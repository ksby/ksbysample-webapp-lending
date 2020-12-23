package ksbysample.webapp.lending.entity;

import java.time.LocalDateTime;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

/**
 */
@SuppressWarnings({"PMD.TooManyFields"})
@Entity(metamodel = @Metamodel)
@Table(name = "user_info")
public class UserInfo {

    /** */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    /** */
    @Column(name = "username")
    String username;

    /** */
    @Column(name = "password")
    String password;

    /** */
    @Column(name = "mail_address")
    String mailAddress;

    /** */
    @Column(name = "enabled")
    Short enabled;

    /** */
    @Column(name = "cnt_badcredentials")
    Short cntBadcredentials;

    /** */
    @Column(name = "expired_account")
    LocalDateTime expiredAccount;

    /** */
    @Column(name = "expired_password")
    LocalDateTime expiredPassword;

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
     * Returns the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /** 
     * Sets the username.
     * 
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** 
     * Returns the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /** 
     * Sets the password.
     * 
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /** 
     * Returns the mailAddress.
     * 
     * @return the mailAddress
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /** 
     * Sets the mailAddress.
     * 
     * @param mailAddress the mailAddress
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    /** 
     * Returns the enabled.
     * 
     * @return the enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /** 
     * Sets the enabled.
     * 
     * @param enabled the enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /** 
     * Returns the cntBadcredentials.
     * 
     * @return the cntBadcredentials
     */
    public Short getCntBadcredentials() {
        return cntBadcredentials;
    }

    /** 
     * Sets the cntBadcredentials.
     * 
     * @param cntBadcredentials the cntBadcredentials
     */
    public void setCntBadcredentials(Short cntBadcredentials) {
        this.cntBadcredentials = cntBadcredentials;
    }

    /** 
     * Returns the expiredAccount.
     * 
     * @return the expiredAccount
     */
    public LocalDateTime getExpiredAccount() {
        return expiredAccount;
    }

    /** 
     * Sets the expiredAccount.
     * 
     * @param expiredAccount the expiredAccount
     */
    public void setExpiredAccount(LocalDateTime expiredAccount) {
        this.expiredAccount = expiredAccount;
    }

    /** 
     * Returns the expiredPassword.
     * 
     * @return the expiredPassword
     */
    public LocalDateTime getExpiredPassword() {
        return expiredPassword;
    }

    /** 
     * Sets the expiredPassword.
     * 
     * @param expiredPassword the expiredPassword
     */
    public void setExpiredPassword(LocalDateTime expiredPassword) {
        this.expiredPassword = expiredPassword;
    }
}
