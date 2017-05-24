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
 *
 * @author java
 */
@Entity
@Table(name = "ogloszenie")
@NamedQueries({
    @NamedQuery(name = "Ogloszenie.findAll", query = "SELECT o FROM Ogloszenie o"),
    @NamedQuery(name = "Ogloszenie.findById", query = "SELECT o FROM Ogloszenie o WHERE o.id = :id"),
    @NamedQuery(name = "Ogloszenie.findByTytul", query = "SELECT o FROM Ogloszenie o WHERE o.tytul = :tytul"),
    @NamedQuery(name = "Ogloszenie.findByCena", query = "SELECT o FROM Ogloszenie o WHERE o.cena = :cena"),
    @NamedQuery(name = "Ogloszenie.findByRynekPierwotny", query = "SELECT o FROM Ogloszenie o WHERE o.rynekPierwotny = :rynekPierwotny"),
    @NamedQuery(name = "Ogloszenie.findByDataDodania", query = "SELECT o FROM Ogloszenie o WHERE o.dataDodania = :dataDodania"),
    @NamedQuery(name = "Ogloszenie.findByVersion", query = "SELECT o FROM Ogloszenie o WHERE o.version = :version"),
    @NamedQuery(name = "Ogloszenie.findByAktywne", query = "SELECT o FROM Ogloszenie o WHERE o.aktywne = :aktywne"),
    @NamedQuery(name = "OgloszenieID.findByAgent", query = "SELECT o From Ogloszenie o WHERE o.idAgenta = :agent")})
public class Ogloszenie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="ogloszenie_id_seq",
                       sequenceName="ogloszenie_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="ogloszenie_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 80, message="{walidacja.notNull}")
    @Column(name = "tytul")
    private String tytul;

    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Min(value = 1,message = "{walidacja.cena}")
    @Column(name = "cena")
    private int cena;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "rynek_pierwotny")
    private boolean rynekPierwotny;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "data_dodania")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDodania;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "aktywne")
    private boolean aktywne;
    @ManyToMany(mappedBy = "ogloszenieUlubioneCollection")
    private Collection<Konto> kontoCollection = new ArrayList<Konto>();
    @JoinColumn(name = "id_agenta", referencedColumnName = "id", updatable = true)
    @ManyToOne(optional = false)
    private Konto idAgenta;
    @JoinColumn(name = "id_wlasciciela", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private Konto idWlasciciela;

    @PrimaryKeyJoinColumn( name = "id", referencedColumnName = "id" )
    @OneToOne(optional = false, cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    private Nieruchomosc nieruchomosc;
    @JoinColumn(name = "typ_ogloszenia", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TypOgloszenia typOgloszenia;
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "idOgloszenia")
    private Collection<Spotkanie> spotkanieCollection = new ArrayList<Spotkanie>();

    public Ogloszenie() {
    }

    public Ogloszenie(Long id) {
        this.id = id;
    }

    public Ogloszenie(Long id, String tytul, int cena, boolean rynekPierwotny, Date dataDodania, long version, boolean aktywne) {
        this.id = id;
        this.tytul = tytul;
        this.cena = cena;
        this.rynekPierwotny = rynekPierwotny;
        this.dataDodania = dataDodania;
        this.version = version;
        this.aktywne = aktywne;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public boolean getRynekPierwotny() {
        return rynekPierwotny;
    }

    public void setRynekPierwotny(boolean rynekPierwotny) {
        this.rynekPierwotny = rynekPierwotny;
    }

    public Date getDataDodania() {
        return dataDodania;
    }

    public void setDataDodania(Date dataDodania) {
        this.dataDodania = dataDodania;
    }

    public boolean getAktywne() {
        return aktywne;
    }

    public void setAktywne(boolean aktywne) {
        this.aktywne = aktywne;
    }

    public Collection<Konto> getKontoCollection() {
        return kontoCollection;
    }

    public void setKontoCollection(Collection<Konto> kontoCollection) {
        this.kontoCollection = kontoCollection;
    }

    public Konto getIdAgenta() {
        return idAgenta;
    }

    public void setIdAgenta(Konto idAgenta) {
        this.idAgenta = idAgenta;
    }

    public Konto getIdWlasciciela() {
        return idWlasciciela;
    }

    public void setIdWlasciciela(Konto idWlasciciela) {
        this.idWlasciciela = idWlasciciela;
    }

    public Nieruchomosc getNieruchomosc() {
        return nieruchomosc;
    }

    public void setNieruchomosc(Nieruchomosc nieruchomosc) {
        this.nieruchomosc = nieruchomosc;
    }

    public TypOgloszenia getTypOgloszenia() {
        return typOgloszenia;
    }

    public void setTypOgloszenia(TypOgloszenia typOgloszenia) {
        this.typOgloszenia = typOgloszenia;
    }

    public Collection<Spotkanie> getSpotkanieCollection() {
        return spotkanieCollection;
    }

    public void setSpotkanieCollection(Collection<Spotkanie> spotkanieCollection) {
        this.spotkanieCollection = spotkanieCollection;
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
        if (!(object instanceof Ogloszenie)) {
            return false;
        }
        Ogloszenie other = (Ogloszenie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.Ogloszenie[ id=" + id + " ]";
    }

    /**
     * Przeszukuje kontoCollection i sprawdza czy istnieje konto o podanym loginie
     * @param login
     * @return 
     */
    public boolean isUlubione(String login) {
        for (Konto konto:kontoCollection) {
            if (konto.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }    
}
