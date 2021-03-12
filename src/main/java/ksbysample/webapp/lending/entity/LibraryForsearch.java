package ksbysample.webapp.lending.entity;

import org.seasar.doma.*;

/**
 * ???
 */
@SuppressWarnings({"PMD.TooManyFields"})
@Entity(metamodel = @Metamodel)
@Table(name = "library_forsearch")
public class LibraryForsearch {

    /**
     *
     */
    @Id
    @Column(name = "systemid")
    String systemid;

    /**
     *
     */
    @Column(name = "formal")
    String formal;

    /**
     * Returns the systemid.
     *
     * @return the systemid
     */
    public String getSystemid() {
        return systemid;
    }

    /**
     * Sets the systemid.
     *
     * @param systemid the systemid
     */
    public void setSystemid(String systemid) {
        this.systemid = systemid;
    }

    /**
     * Returns the formal.
     *
     * @return the formal
     */
    public String getFormal() {
        return formal;
    }

    /**
     * Sets the formal.
     *
     * @param formal the formal
     */
    public void setFormal(String formal) {
        this.formal = formal;
    }
}
