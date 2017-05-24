/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author java
 */
@Entity
@Table(name = "nieruchomosc")
@NamedQueries({
        @NamedQuery(name = "Nieruchomosc.findAll", query = "SELECT n FROM Nieruchomosc n"),
        @NamedQuery(name = "Nieruchomosc.findById", query = "SELECT n FROM Nieruchomosc n WHERE n.id = :id"),
        @NamedQuery(name = "Nieruchomosc.findByMiejscowosc", query = "SELECT n FROM Nieruchomosc n WHERE n.miejscowosc = :miejscowosc"),
        @NamedQuery(name = "Nieruchomosc.findByUlica", query = "SELECT n FROM Nieruchomosc n WHERE n.ulica = :ulica"),
        @NamedQuery(name = "Nieruchomosc.findByLiczbaPokoi", query = "SELECT n FROM Nieruchomosc n WHERE n.liczbaPokoi = :liczbaPokoi"),
        @NamedQuery(name = "Nieruchomosc.findByLiczbaPieter", query = "SELECT n FROM Nieruchomosc n WHERE n.liczbaPieter = :liczbaPieter"),
        @NamedQuery(name = "Nieruchomosc.findByRokBudowy", query = "SELECT n FROM Nieruchomosc n WHERE n.rokBudowy = :rokBudowy"),
        @NamedQuery(name = "Nieruchomosc.findByPowierzchniaDzialki", query = "SELECT n FROM Nieruchomosc n WHERE n.powierzchniaDzialki = :powierzchniaDzialki"),
        @NamedQuery(name = "Nieruchomosc.findByTypNieruchomosci", query = "SELECT n FROM Nieruchomosc n WHERE n.typNieruchomosci = :typNieruchomosci"),
        @NamedQuery(name = "Nieruchomosc.findByVersion", query = "SELECT n FROM Nieruchomosc n WHERE n.version = :version")})
public class Nieruchomosc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "nieruchomosc_id_seq",
            sequenceName = "nieruchomosc_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "nieruchomosc_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull(message = "{walidacja.notNull}")
    @Size(min = 1, max = 64, message = "{walidacja.size}")
    @Column(name = "miejscowosc")
    private String miejscowosc;
    @Basic(optional = false)
    @NotNull(message = "{walidacja.notNull}")
    @Size(min = 1, max = 64, message = "{walidacja.size}")
    @Column(name = "ulica")
    private String ulica;
    @NotNull(message = "{walidacja.notNull}")
    @Column(name = "liczba_pokoi")
    private Integer liczbaPokoi;
    @NotNull(message = "{walidacja.notNull}")
    @Column(name = "liczba_pieter")
    private Integer liczbaPieter;
    @Basic(optional = false)
    @NotNull(message = "{walidacja.notNull}")
    @Column(name = "rok_budowy")
    @Temporal(TemporalType.DATE)
    private Date rokBudowy;
    @NotNull(message = "{walidacja.notNull}")
    @Min(value = 1, message = "{walidacja.powierzchnia}")
    @Column(name = "powierzchnia_dzialki")
    private Integer powierzchniaDzialki;
    @NotNull(message = "{walidacja.notNull}")
    @Min(value = 1, message = "{walidacja.powierzchnia}")
    @Column(name = "powierzchnia_nieruchomosci")
    private Integer powierzchniaNieruchomosci;
    @Basic(optional = false)
    @NotNull(message = "{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    @ManyToMany(fetch = FetchType.EAGER,  mappedBy = "nieruchomoscWyposazonaCollection")
    private Collection<ElementWyposazeniaNieruchomosci> elementWyposazeniaNieruchomosciCollection = new ArrayList<ElementWyposazeniaNieruchomosci>();
    @JoinColumn(name = "typ_nieruchomosci", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private TypNieruchomosci typNieruchomosci;
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "nieruchomosc")
    private Ogloszenie ogloszenie;

    public Nieruchomosc() {
    }

    public Nieruchomosc(Long id) {
        this.id = id;
    }

    public Nieruchomosc(Long id, String miejscowosc, String ulica, Date rokBudowy, long version) {
        this.id = id;
        this.miejscowosc = miejscowosc;
        this.ulica = ulica;
        this.rokBudowy = rokBudowy;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMiejscowosc() {
        return miejscowosc;
    }

    public void setMiejscowosc(String miejscowosc) {
        this.miejscowosc = miejscowosc;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public Integer getLiczbaPokoi() {
        return liczbaPokoi;
    }

    public void setLiczbaPokoi(Integer liczbaPokoi) {
        this.liczbaPokoi = liczbaPokoi;
    }

    public Integer getLiczbaPieter() {
        return liczbaPieter;
    }

    public void setLiczbaPieter(Integer liczbaPieter) {
        this.liczbaPieter = liczbaPieter;
    }

    public Date getRokBudowy() {
        return rokBudowy;
    }

    public void setRokBudowy(Date rokBudowy) {
        this.rokBudowy = rokBudowy;
    }

    public Integer getPowierzchniaDzialki() {
        return powierzchniaDzialki;
    }

    public void setPowierzchniaDzialki(Integer powierzchniaDzialki) {
        this.powierzchniaDzialki = powierzchniaDzialki;
    }

    public Integer getPowierzchniaNieruchomosci() {
        return powierzchniaNieruchomosci;
    }

    public void setPowierzchniaNieruchomosci(Integer powierzchniaNieruchomosci) {
        this.powierzchniaNieruchomosci = powierzchniaNieruchomosci;
    }

    public Collection<ElementWyposazeniaNieruchomosci> getElementWyposazeniaNieruchomosciCollection() {
        return elementWyposazeniaNieruchomosciCollection;
    }

    public void setElementWyposazeniaNieruchomosciCollection(Collection<ElementWyposazeniaNieruchomosci> elementWyposazeniaNieruchomosciCollection) {
        this.elementWyposazeniaNieruchomosciCollection = elementWyposazeniaNieruchomosciCollection;
    }



    public TypNieruchomosci getTypNieruchomosci() {
        return typNieruchomosci;
    }

    public void setTypNieruchomosci(TypNieruchomosci typNieruchomosci) {
        this.typNieruchomosci = typNieruchomosci;
    }

    public Ogloszenie getOgloszenie() {
        return ogloszenie;
    }

    public void setOgloszenie(Ogloszenie ogloszenie) {
        this.ogloszenie = ogloszenie;
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
        if (!(object instanceof Nieruchomosc)) {
            return false;
        }
        Nieruchomosc other = (Nieruchomosc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.Nieruchomosc[ id=" + id + " ]";
    }

}
