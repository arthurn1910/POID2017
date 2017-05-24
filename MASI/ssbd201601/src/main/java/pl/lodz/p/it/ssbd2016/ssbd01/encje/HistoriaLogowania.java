package pl.lodz.p.it.ssbd2016.ssbd01.encje;

import javax.persistence.*;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 * Created by Kamil Rogowski on 01.05.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "HistoriaLogowania.findAll", query = "SELECT k FROM HistoriaLogowania k"),
        @NamedQuery(name = "HistoriaLogowania.findById", query = "SELECT k FROM HistoriaLogowania k WHERE k.id = :id"),
        @NamedQuery(name = "HistoriaLogowania.findByAdresIp", query = "SELECT k FROM HistoriaLogowania k WHERE k.adresIp=:adresIp"),
        @NamedQuery(name = "HistoriaLogowania.findByIdKonta", query = "SELECT k FROM HistoriaLogowania k WHERE k.idKonta.id = :idKonta")
})
@Table(name = "historia_logowania", schema = "public", catalog = "ssbd01")
public class HistoriaLogowania {
    @Id
    @SequenceGenerator(name = "historia_logowania_id_seq",
            sequenceName = "historia_logowania_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "historia_logowania_id_seq")
    @Column(name = "id")
    private Long id;
    @NotNull(message="{walidacja.notNull}")
    @Basic
    @Column(name = "adres_ip")
    private String adresIp;
    @NotNull(message="{walidacja.notNull}")
    @Basic
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLogowania;
    @JoinColumn(name = "id_konta", referencedColumnName = "id",  updatable = false)
    @ManyToOne(optional = false)
    private Konto idKonta;


    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresIp() {
        return adresIp;
    }

    public void setAdresIp(String adresIp) {
        this.adresIp = adresIp;
    }


    public Date getDataLogowania() {
        return dataLogowania;
    }

    public void setDataLogowania(Date data) {
        this.dataLogowania = data;
    }

    public Konto getidKonta() {
        return idKonta;
    }

    public void setIdKonta(Konto idUzytkownika) {
        this.idKonta = idUzytkownika;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KategoriaWyposazeniaNieruchomosci)) {
            return false;
        }
        HistoriaLogowania other = (HistoriaLogowania) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
}
