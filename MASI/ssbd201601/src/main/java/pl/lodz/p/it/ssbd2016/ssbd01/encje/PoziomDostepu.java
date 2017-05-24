/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "poziom_dostepu")
@NamedQueries({
    @NamedQuery(name = "PoziomDostepu.findAll", query = "SELECT p FROM PoziomDostepu p"),
    @NamedQuery(name = "PoziomDostepu.findById", query = "SELECT p FROM PoziomDostepu p WHERE p.id = :id"),
    @NamedQuery(name = "PoziomDostepu.findByPoziom", query = "SELECT p FROM PoziomDostepu p WHERE p.poziom = :poziom"),
    @NamedQuery(name = "PoziomDostepu.findByAktywny", query = "SELECT p FROM PoziomDostepu p WHERE p.aktywny = :aktywny"),
    @NamedQuery(name = "PoziomDostepu.findByVersion", query = "SELECT p FROM PoziomDostepu p WHERE p.version = :version")})
public class PoziomDostepu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="poziom_dostepu_id_seq",
                       sequenceName="poziom_dostepu_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="poziom_dostepu_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Size(min = 1, max = 16)
    @Column(name = "poziom")
    private String poziom;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "aktywny")
    private boolean aktywny;
    @Basic(optional = false)
    @NotNull(message="{walidacja.notNull}")
    @Column(name = "version")
    @Version
    private long version;
    @JoinColumn(name = "konto_id", referencedColumnName = "id", updatable=false)
    @ManyToOne(optional = false)
    private Konto kontoId;

    public PoziomDostepu() {
    }

    public PoziomDostepu(Long id) {
        this.id = id;
    }

    public PoziomDostepu(Long id, String poziom, boolean aktywny, long version) {
        this.id = id;
        this.poziom = poziom;
        this.aktywny = aktywny;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoziom() {
        return poziom;
    }

    public void setPoziom(String poziom) {
        this.poziom = poziom;
    }

    public boolean getAktywny() {
        return aktywny;
    }

    public void setAktywny(boolean aktywny) {
        this.aktywny = aktywny;
    }

    public Konto getKontoId() {
        return kontoId;
    }

    public void setKontoId(Konto kontoId) {
        this.kontoId = kontoId;
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
        if (!(object instanceof PoziomDostepu)) {
            return false;
        }
        PoziomDostepu other = (PoziomDostepu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2016.ssbd01.encje.PoziomDostepu[ id=" + id + " ]";
    }
    
}
