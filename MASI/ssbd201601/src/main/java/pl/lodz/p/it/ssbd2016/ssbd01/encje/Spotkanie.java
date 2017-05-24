/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author java
 */
@Entity
@Table(name = "spotkanie")
@NamedQueries({
    @NamedQuery(name = "Spotkanie.findAll", query = "SELECT s FROM Spotkanie s"),
    @NamedQuery(name = "Spotkanie.findById", query = "SELECT s FROM Spotkanie s WHERE s.id = :id"),
    @NamedQuery(name = "Spotkanie.findByDataSpotkania", query = "SELECT s FROM Spotkanie s WHERE s.dataSpotkania = :dataSpotkania"),
    @NamedQuery(name = "Spotkanie.findByVersion", query = "SELECT s FROM Spotkanie s WHERE s.version = :version"),
    @NamedQuery(name = "Spotkanie.findByDlugoscSpotkania", query = "SELECT s FROM Spotkanie s WHERE s.dlugoscSpotkania = :dlugoscSpotkania"),
    @NamedQuery(name = "Spotkanie.findByOgloszenie", query = "SELECT s FROM Spotkanie s WHERE s.idOgloszenia = :ogloszenie"),
    @NamedQuery(name = "Spotkanie.findByIdUzytkownika", query = "SELECT s FROM Spotkanie s WHERE s.idUzytkownika= :idUzytkownika")
})
public class Spotkanie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="spotkanie_id_seq",
                       sequenceName="spotkanie_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="spotkanie_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "data_spotkania")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSpotkania;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "dlugosc_spotkania")
    private int dlugoscSpotkania;
    @JoinColumn(name = "id_uzytkownika", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private Konto idUzytkownika;
    @JoinColumn(name = "id_ogloszenia", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    private Ogloszenie idOgloszenia;

    public Spotkanie() {
    }

    public Spotkanie(Long id) {
        this.id = id;
    }

    public Spotkanie(Long id, Date dataSpotkania, long version, int dlugoscSpotkania) {
        this.id = id;
        this.dataSpotkania = dataSpotkania;
        this.version = version;
        this.dlugoscSpotkania = dlugoscSpotkania;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataSpotkania() {
        return dataSpotkania;
    }

    public void setDataSpotkania(Date dataSpotkania) {
        this.dataSpotkania = dataSpotkania;
    }

    public int getDlugoscSpotkania() {
        return dlugoscSpotkania;
    }

    public void setDlugoscSpotkania(int dlugoscSpotkania) {
        this.dlugoscSpotkania = dlugoscSpotkania;
    }

    public Konto getIdUzytkownika() {
        return idUzytkownika;
    }

    public void setIdUzytkownika(Konto idUzytkownika) {
        this.idUzytkownika = idUzytkownika;
    }

    public Ogloszenie getIdOgloszenia() {
        return idOgloszenia;
    }

    public void setIdOgloszenia(Ogloszenie idOgloszenia) {
        this.idOgloszenia = idOgloszenia;
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
        if (!(object instanceof Spotkanie)) {
            return false;
        }
        Spotkanie other = (Spotkanie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.Spotkanie[ id=" + id + " ]";
    }
    
}
