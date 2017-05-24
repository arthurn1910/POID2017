/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author java
 */
@Entity
@Table(name = "typ_nieruchomosci")
@NamedQueries({
    @NamedQuery(name = "TypNieruchomosci.findAll", query = "SELECT t FROM TypNieruchomosci t"),
    @NamedQuery(name = "TypNieruchomosci.findById", query = "SELECT t FROM TypNieruchomosci t WHERE t.id = :id"),
    @NamedQuery(name = "TypNieruchomosci.findByNazwa", query = "SELECT t FROM TypNieruchomosci t WHERE t.nazwa = :nazwa"),
    @NamedQuery(name = "TypNieruchomosci.findByVersion", query = "SELECT t FROM TypNieruchomosci t WHERE t.version = :version"),
    @NamedQuery(name = "TypNieruchomosci.findBySredniaCenaMetraKwadratowego", query = "SELECT t FROM TypNieruchomosci t WHERE t.sredniaCenaMetraKwadratowego = :sredniaCenaMetraKwadratowego")})
public class TypNieruchomosci implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="typ_nieruchomosci_id_seq",
                       sequenceName="typ_nieruchomosci_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="typ_nieruchomosci_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 50)
    @Column(name = "nazwa", updatable = false)
    private String nazwa;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "srednia_cena_metra_kwadratowego")
    private BigDecimal sredniaCenaMetraKwadratowego;
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "typNieruchomosci")
    private Collection<Nieruchomosc> nieruchomoscCollection = new ArrayList<Nieruchomosc>();

    public TypNieruchomosci() {
    }

    public TypNieruchomosci(Integer id) {
        this.id = id;
    }

    public TypNieruchomosci(Integer id, String nazwa, long version, BigDecimal sredniaCenaMetraKwadratowego) {
        this.id = id;
        this.nazwa = nazwa;
        this.version = version;
        this.sredniaCenaMetraKwadratowego = sredniaCenaMetraKwadratowego;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public BigDecimal getSredniaCenaMetraKwadratowego() {
        return sredniaCenaMetraKwadratowego;
    }

    public void setSredniaCenaMetraKwadratowego(BigDecimal sredniaCenaMetraKwadratowego) {
        this.sredniaCenaMetraKwadratowego = sredniaCenaMetraKwadratowego;
    }

    public Collection<Nieruchomosc> getNieruchomoscCollection() {
        return nieruchomoscCollection;
    }

    public void setNieruchomoscCollection(Collection<Nieruchomosc> nieruchomoscCollection) {
        this.nieruchomoscCollection = nieruchomoscCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypNieruchomosci)) {
            return false;
        }
        TypNieruchomosci other = (TypNieruchomosci) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.TypNieruchomosci[ id=" + id + " ]";
    }
    
}
