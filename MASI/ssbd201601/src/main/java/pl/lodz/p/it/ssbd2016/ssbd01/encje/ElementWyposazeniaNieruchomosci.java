/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author java
 */
@Entity
@Table(name = "element_wyposazenia_nieruchomosci")
@NamedQueries({
        @NamedQuery(name = "ElementWyposazeniaNieruchomosci.findAll", query = "SELECT e FROM ElementWyposazeniaNieruchomosci e"),
        @NamedQuery(name = "ElementWyposazeniaNieruchomosci.findById", query = "SELECT e FROM ElementWyposazeniaNieruchomosci e WHERE e.id = :id"),
        @NamedQuery(name = "ElementWyposazeniaNieruchomosci.findByNazwa", query = "SELECT e FROM ElementWyposazeniaNieruchomosci e WHERE e.nazwa = :nazwa"),
        @NamedQuery(name = "ElementWyposazeniaNieruchomosci.findByVersion", query = "SELECT e FROM ElementWyposazeniaNieruchomosci e WHERE e.version = :version")})
public class ElementWyposazeniaNieruchomosci implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "element_wyposazenia_nieruchomosci_seq",
            sequenceName = "element_wyposazenia_nieruchomosci_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "element_wyposazenia_nieruchomosci_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull(message = "{walidacja.notNull}")
    @Size(min = 1, max = 64, message = "{walidacja.size}")
    @Column(name = "nazwa", updatable = false)
    private String nazwa;
    @Basic(optional = false)
    @NotNull(message = "{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id")
    @JoinTable(name = "wyposazenie_nieruchomosci", joinColumns = {
            @JoinColumn(name = "id_wartosci", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "id_nieruchomosci", referencedColumnName = "id")})
    private Collection<Nieruchomosc> nieruchomoscWyposazonaCollection;
    @JoinColumn(name = "id_kategorii", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private KategoriaWyposazeniaNieruchomosci idKategorii;

    public ElementWyposazeniaNieruchomosci() {
    }

    public ElementWyposazeniaNieruchomosci(Long id) {
        this.id = id;
    }

    public ElementWyposazeniaNieruchomosci(Long id, String nazwa, long version) {
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

    public Collection<Nieruchomosc> getNieruchomoscWyposazona() {
        return nieruchomoscWyposazonaCollection;
    }

    public void setNieruchomoscWyposazonaCollection(Collection<Nieruchomosc> nieruchomoscWyposazona) {
        this.nieruchomoscWyposazonaCollection = nieruchomoscWyposazona;
    }

    public KategoriaWyposazeniaNieruchomosci getIdKategorii() {
        return idKategorii;
    }

    public void setIdKategorii(KategoriaWyposazeniaNieruchomosci idKategorii) {
        this.idKategorii = idKategorii;
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
        if (!(object instanceof ElementWyposazeniaNieruchomosci)) {
            return false;
        }
        ElementWyposazeniaNieruchomosci other = (ElementWyposazeniaNieruchomosci) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.ElementWyposazeniaNieruchomosci[ id=" + id + " ]";
    }

}
