package ksbysample.webapp.lending.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

/**
 */
@Entity
@Table(name = "lending_book")
public class LendingBook {

    /** */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lending_book_id")
    Long lendingBookId;

    /** */
    @Column(name = "lending_app_id")
    Long lendingAppId;

    /** */
    @Column(name = "isbn")
    String isbn;

    /** */
    @Column(name = "book_name")
    String bookName;

    /** */
    @Column(name = "lending_state")
    String lendingState;

    /** */
    @Column(name = "lending_app_flg")
    String lendingAppFlg;

    /** */
    @Column(name = "lending_app_reason")
    String lendingAppReason;

    /** */
    @Column(name = "approval_result")
    String approvalResult;

    /** */
    @Column(name = "approval_reason")
    String approvalReason;

    /** */
    @Version
    @Column(name = "version")
    Long version;

    /** 
     * Returns the lendingBookId.
     * 
     * @return the lendingBookId
     */
    public Long getLendingBookId() {
        return lendingBookId;
    }

    /** 
     * Sets the lendingBookId.
     * 
     * @param lendingBookId the lendingBookId
     */
    public void setLendingBookId(Long lendingBookId) {
        this.lendingBookId = lendingBookId;
    }

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
     * Returns the isbn.
     * 
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /** 
     * Sets the isbn.
     * 
     * @param isbn the isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /** 
     * Returns the bookName.
     * 
     * @return the bookName
     */
    public String getBookName() {
        return bookName;
    }

    /** 
     * Sets the bookName.
     * 
     * @param bookName the bookName
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /** 
     * Returns the lendingState.
     * 
     * @return the lendingState
     */
    public String getLendingState() {
        return lendingState;
    }

    /** 
     * Sets the lendingState.
     * 
     * @param lendingState the lendingState
     */
    public void setLendingState(String lendingState) {
        this.lendingState = lendingState;
    }

    /** 
     * Returns the lendingAppFlg.
     * 
     * @return the lendingAppFlg
     */
    public String getLendingAppFlg() {
        return lendingAppFlg;
    }

    /** 
     * Sets the lendingAppFlg.
     * 
     * @param lendingAppFlg the lendingAppFlg
     */
    public void setLendingAppFlg(String lendingAppFlg) {
        this.lendingAppFlg = lendingAppFlg;
    }

    /** 
     * Returns the lendingAppReason.
     * 
     * @return the lendingAppReason
     */
    public String getLendingAppReason() {
        return lendingAppReason;
    }

    /** 
     * Sets the lendingAppReason.
     * 
     * @param lendingAppReason the lendingAppReason
     */
    public void setLendingAppReason(String lendingAppReason) {
        this.lendingAppReason = lendingAppReason;
    }

    /** 
     * Returns the approvalResult.
     * 
     * @return the approvalResult
     */
    public String getApprovalResult() {
        return approvalResult;
    }

    /** 
     * Sets the approvalResult.
     * 
     * @param approvalResult the approvalResult
     */
    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    /** 
     * Returns the approvalReason.
     * 
     * @return the approvalReason
     */
    public String getApprovalReason() {
        return approvalReason;
    }

    /** 
     * Sets the approvalReason.
     * 
     * @param approvalReason the approvalReason
     */
    public void setApprovalReason(String approvalReason) {
        this.approvalReason = approvalReason;
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