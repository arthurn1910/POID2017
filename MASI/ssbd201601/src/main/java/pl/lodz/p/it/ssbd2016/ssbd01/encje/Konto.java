/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "konto")
@SecondaryTable(name="dane_personalne")
@NamedQueries({
    @NamedQuery(name = "Konto.findAll", query = "SELECT k FROM Konto k"),
    @NamedQuery(name = "Konto.findById", query = "SELECT k FROM Konto k WHERE k.id = :id"),
    @NamedQuery(name = "Konto.findByLogin", query = "SELECT k FROM Konto k WHERE k.login = :login"),
    @NamedQuery(name = "Konto.findByHaslo", query = "SELECT k FROM Konto k WHERE k.haslo = :haslo"),
    @NamedQuery(name = "Konto.findByPotwierdzone", query = "SELECT k FROM Konto k WHERE k.potwierdzone = :potwierdzone"),
    @NamedQuery(name = "Konto.findByAktywne", query = "SELECT k FROM Konto k WHERE k.aktywne = :aktywne"),
    @NamedQuery(name = "Konto.findByVersion", query = "SELECT k FROM Konto k WHERE k.version = :version"),
    @NamedQuery(name = "Konto.findSimilar", query = "SELECT k FROM Konto k WHERE lower(k.imie) like :imie AND lower(k.nazwisko) like :nazwisko AND lower(k.email) like :email AND k.telefon like :telefon AND k.potwierdzone = :potwierdzone AND k.aktywne = :aktywne")})
public class Konto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="konto_id_seq",
                       sequenceName="konto_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="konto_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 32, message = "{walidacja.size}")
    @Column(name = "login", updatable = false)
    private String login;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 8, max = 64, message = "{walidacja.size}")
    @Column(name = "haslo")
    private String haslo;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "potwierdzone")
    private boolean potwierdzone;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "aktywne")
    private boolean aktywne;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    @JoinTable(name = "ulubione_ogloszenie", joinColumns = {
        @JoinColumn(name = "id_konta", referencedColumnName = "id", updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_ogloszenia", referencedColumnName = "id", updatable = false)})
    @ManyToMany
    private Collection<Ogloszenie> ogloszenieUlubioneCollection = new ArrayList<Ogloszenie>();
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "idAgenta")
    private Collection<Ogloszenie> ogloszenieAgentaCollection = new ArrayList<Ogloszenie>();
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "idWlasciciela")
    private Collection<Ogloszenie> ogloszenieWlascicielaCollection = new ArrayList<Ogloszenie>();
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "kontoId")
    private Collection<PoziomDostepu> poziomDostepuCollection = new ArrayList<PoziomDostepu>();
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "idUzytkownika")
    private Collection<Spotkanie> spotkanieCollection = new ArrayList<Spotkanie>();

    // Dane z tabeli dane_personalne
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 32, message = "{walidacja.size}")
    @Column(name = "imie", table="dane_personalne")
    private String imie;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 32, message = "{walidacja.size}")
    @Column(name = "nazwisko", table="dane_personalne")
    private String nazwisko;
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="{walidacja.pattern}")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 64, message = "{walidacja.size}")
    @Column(name = "email", table="dane_personalne")
    private String email;
    @Pattern(regexp = "^[0-9]*$", message="{walidacja.pattern}")
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 9, max = 9, message = "{walidacja.size}")
    @Column(name = "telefon", table="dane_personalne")
    private String telefon;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "data_utworzenia", table="dane_personalne")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUtworzenia;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "version", table="dane_personalne")
    private long versionA;
    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Date getDataUtworzenia() {
        return dataUtworzenia;
    }

    public void setDataUtworzenia(Date dataUtworzenia) {
        this.dataUtworzenia = dataUtworzenia;
    }
    
    public Konto() {
    }

    public Konto(Long id) {
        this.id = id;
    }

    public Konto(Long id, String login, String haslo, boolean potwierdzone, boolean aktywne, long version) {
        this.id = id;
        this.login = login;
        this.haslo = haslo;
        this.potwierdzone = potwierdzone;
        this.aktywne = aktywne;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public boolean getPotwierdzone() {
        return potwierdzone;
    }

    public void setPotwierdzone(boolean potwierdzone) {
        this.potwierdzone = potwierdzone;
    }

    public boolean getAktywne() {
        return aktywne;
    }

    public void setAktywne(boolean aktywne) {
        this.aktywne = aktywne;
    }

    public Collection<Ogloszenie> getOgloszenieUlubioneCollection() {
        return ogloszenieUlubioneCollection;
    }

    public void setOgloszenieUlbioneCollection(Collection<Ogloszenie> ogloszenieUlubioneCollection) {
        this.ogloszenieUlubioneCollection = ogloszenieUlubioneCollection;
    }

    public Collection<Ogloszenie> getOgloszenieAgentaCollection() {
        return ogloszenieAgentaCollection;
    }

    public void setOgloszenieAgentaCollection(Collection<Ogloszenie> ogloszenieAgentaCollection) {
        this.ogloszenieAgentaCollection = ogloszenieAgentaCollection;
    }

    public Collection<Ogloszenie> getOgloszenieWlascicielaCollection() {
        return ogloszenieWlascicielaCollection;
    }

    public void setOgloszenieWlascicielaCollection(Collection<Ogloszenie> ogloszenieWlascicielaCollection) {
        this.ogloszenieWlascicielaCollection = ogloszenieWlascicielaCollection;
    }

    public Collection<PoziomDostepu> getPoziomDostepuCollection() {
        return poziomDostepuCollection;
    }

    public void setPoziomDostepuCollection(Collection<PoziomDostepu> poziomDostepuCollection) {
        this.poziomDostepuCollection = poziomDostepuCollection;
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
        if (!(object instanceof Konto)) {
            return false;
        }
        Konto other = (Konto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.Konto[ id=" + id + " ]";
    }

}
