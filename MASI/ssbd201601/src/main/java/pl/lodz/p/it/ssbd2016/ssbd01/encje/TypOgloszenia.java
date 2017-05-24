/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import java.io.Serializable;
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
@Table(name = "typ_ogloszenia")
@NamedQueries({
    @NamedQuery(name = "TypOgloszenia.findAll", query = "SELECT t FROM TypOgloszenia t"),
    @NamedQuery(name = "TypOgloszenia.findById", query = "SELECT t FROM TypOgloszenia t WHERE t.id = :id"),
    @NamedQuery(name = "TypOgloszenia.findByNazwa", query = "SELECT t FROM TypOgloszenia t WHERE t.nazwa = :nazwa"),
    @NamedQuery(name = "TypOgloszenia.findByVersion", query = "SELECT t FROM TypOgloszenia t WHERE t.version = :version")})
public class TypOgloszenia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="typ_ogloszenia_id_seq",
                       sequenceName="typ_ogloszenia_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="typ_ogloszenia_id_seq")
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
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "typOgloszenia")
    private Collection<Ogloszenie> ogloszenieCollection = new ArrayList<Ogloszenie>();

    public TypOgloszenia() {
    }

    public TypOgloszenia(Integer id) {
        this.id = id;
    }

    public TypOgloszenia(Integer id, String nazwa, long version) {
        this.id = id;
        this.nazwa = nazwa;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Collection<Ogloszenie> getOgloszenieCollection() {
        return ogloszenieCollection;
    }

    public void setOgloszenieCollection(Collection<Ogloszenie> ogloszenieCollection) {
        this.ogloszenieCollection = ogloszenieCollection;
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
        if (!(object instanceof TypOgloszenia)) {
            return false;
        }
        TypOgloszenia other = (TypOgloszenia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.TypOgloszenia[ id=" + id + " ]";
    }
    
}
