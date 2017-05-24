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
@Table(name = "kategoria_wyposazenia_nieruchomosci")
@NamedQueries({
    @NamedQuery(name = "KategoriaWyposazeniaNieruchomosci.findAll", query = "SELECT k FROM KategoriaWyposazeniaNieruchomosci k"),
    @NamedQuery(name = "KategoriaWyposazeniaNieruchomosci.findById", query = "SELECT k FROM KategoriaWyposazeniaNieruchomosci k WHERE k.id = :id"),
    @NamedQuery(name = "KategoriaWyposazeniaNieruchomosci.findByNazwa", query = "SELECT k FROM KategoriaWyposazeniaNieruchomosci k WHERE k.nazwa = :nazwa"),
    @NamedQuery(name = "KategoriaWyposazeniaNieruchomosci.findByVersion", query = "SELECT k FROM KategoriaWyposazeniaNieruchomosci k WHERE k.version = :version")})
public class KategoriaWyposazeniaNieruchomosci implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="kategoria_wyposazenia_nieruchomosci_id_seq",
                       sequenceName="kategoria_wyposazenia_nieruchomosci_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="kategoria_wyposazenia_nieruchomosci_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 64, message = "{walidacja.size}")
    @Column(name = "nazwa", updatable = false)
    private String nazwa;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "idKategorii")
    private Collection<ElementWyposazeniaNieruchomosci> elementWyposazeniaNieruchomosciCollection = new ArrayList<ElementWyposazeniaNieruchomosci>();

    public KategoriaWyposazeniaNieruchomosci() {
    }

    public KategoriaWyposazeniaNieruchomosci(Long id) {
        this.id = id;
    }

    public KategoriaWyposazeniaNieruchomosci(Long id, String nazwa, long version) {
        this.id = id;
        this.nazwa = nazwa;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Collection<ElementWyposazeniaNieruchomosci> getElementWyposazeniaNieruchomosciCollection() {
        return elementWyposazeniaNieruchomosciCollection;
    }

    public void setElementWyposazeniaNieruchomosciCollection(Collection<ElementWyposazeniaNieruchomosci> elementWyposazeniaNieruchomosciCollection) {
        this.elementWyposazeniaNieruchomosciCollection = elementWyposazeniaNieruchomosciCollection;
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
        if (!(object instanceof KategoriaWyposazeniaNieruchomosci)) {
            return false;
        }
        KategoriaWyposazeniaNieruchomosci other = (KategoriaWyposazeniaNieruchomosci) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.KategoriaWyposazeniaNieruchomosci[ id=" + id + " ]";
    }
    
}
